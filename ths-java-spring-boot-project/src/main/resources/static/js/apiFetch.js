// apiFetch.js

import { logout } from "./auth-utils";

/**
 * apiFetch - A wrapper around the native fetch function with JWT handling and automatic token refresh.
 *
 * Features:
 * 1. Automatically attaches a JWT access token from localStorage to the Authorization header if available.
 * 2. Sets "Content-Type" to "application/json" by default (can be overridden via options).
 * 3. Handles HTTP 401/403 responses by automatically attempting to refresh the JWT using the HttpOnly refresh token
 * cookie.
 * 4. Queues multiple requests while a token refresh is in progress to prevent multiple simultaneous refresh calls.
 * 5. Logs the user out and redirects to the login page if the refresh token is invalid or expired.
 * 6. Catches and logs network errors, then rethrows them for further handling.
 *
 * Security:
 * - The refresh token is stored in an HttpOnly cookie and is never accessible to JavaScript, protecting against XSS
 * attacks.
 *
 * Usage:
 *   const response = await apiFetch("/api/data", { method: "GET" });
 */
const originalFetch = window.fetch;

let isRefreshing = false;
let refreshQueue = [];

export async function handleRefresh() {
    const refreshRes = await originalFetch("/api/auth/refresh", {
        method: "POST",
        credentials: "include",  // important: send cookies (HttpOnly refresh token)
        headers: { "Content-Type": "application/json" },
    });

    if (!refreshRes.ok) throw new Error("Refresh failed");

    const { jwt } = await refreshRes.json();  // server returns new JWT
    localStorage.setItem("jwt_token", jwt)
    return jwt;
}

export async function apiFetch(url, options = {}) {
    // --- Request Interceptor ---
    const token = localStorage.getItem("jwt_token");

    const headers = {
        "Content-Type": "application/json",  // default header
        ...options.headers,                  // allow caller overrides
        ...(token ? {"Authorization": `Bearer ${token}`} : {}),  // attach JWT if available
    };

    try {
        const response = await originalFetch(url, {...options, headers, credentials: "include"});

        // 401/403 → try refresh
        if (!isRefreshing) {
            isRefreshing = true;

            try {
                const newToken = await handleRefresh();
                isRefreshing = false;

                // Retry queued requests
                refreshQueue.forEach(cb => cb(newToken))
                refreshQueue = [];

                // Retry original request with new token
                options.headers = {...options.headers, Authorization: `Bearer ${newToken}`};
                return originalFetch(url, {...options, headers: options.headers, credentials: "include"});
            } catch (error) {
                isRefreshing = false;
                refreshQueue = [];
                return logout();
            }
        } else {
            // Queue requests while refresh is in progress
            return new Promise((resolve) => {
                refreshQueue.push((newToken) => {
                    options.headers = {...options.headers, Authorization: `Bearer ${newToken}`};
                    resolve(originalFetch(url, {...options, headers: options.headers, credentials: "include"}));
                });
            });
        }
    } catch (error) {
        console.error("Network error:", error);
        throw error;
    }
}
