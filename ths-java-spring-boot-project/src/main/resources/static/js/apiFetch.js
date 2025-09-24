// apiFetch.js

import { logout } from "./auth-utils.js";

/**
 * apiFetch - A wrapper around the native fetch function with JWT handling and automatic token refresh.
 *
 * Features:
 * 1. Automatically attaches a JWT access token from localStorage to the Authorization header if available.
 * 2. Skips attaching a token if none is present (prevents "Bearer undefined/null").
 * 3. Sets "Content-Type" to "application/json" by default (can be overridden via options).
 * 4. Handles HTTP 401/403 responses by automatically attempting to refresh the JWT using the HttpOnly refresh token
 *    cookie.
 * 5. Queues multiple requests while a token refresh is in progress to prevent simultaneous refresh calls.
 * 6. Logs the user out and redirects to the login page if the refresh token is invalid or expired.
 * 7. Catches and logs network errors, then rethrows them for further handling.
 *
 * Security:
 * - The refresh token is stored in an HttpOnly cookie and is never accessible to JavaScript, protecting against XSS.
 * - Only the short-lived access token is stored in localStorage.
 *
 * Usage:
 *   const response = await apiFetch("/api/data", { method: "GET" });
 */
const originalFetch = window.fetch;

let isRefreshing = false;
let refreshQueue = [];

/**
 * Safely get JWT from localStorage
 */
function getToken() {
    const token = localStorage.getItem("jwt_token");
    return token && token !== "undefined" ? token : null;
}

/**
 * Handle refresh token request
 */
async function handleRefresh() {
    try {
        const res = await originalFetch("/api/auth/refresh", {
            method: "POST",
            credentials: "include",  // important: send cookies (HttpOnly refresh token)
            headers: {"Content-Type": "application/json"},
        });

        if (!res.ok) throw new Error("Refresh failed");

        const data = await res.json();  // server returns new JWT

        if (!data.accessToken) throw new Error("Refresh endpoint did not return JWT");

        localStorage.setItem("jwt_token", data.accessToken)
        return data.accessToken;
    } catch (error) {
        console.error("Refresh failed:", error);
        logout();
        throw error;
    }
}

/**
 * Main fetch wrapper with JWT + refresh support
 */
export async function apiFetch(url, options = {}) {
    // --- Request Interceptor ---
    let token = getToken();

    const headers = {
        "Content-Type": "application/json",  // default header
        ...options.headers,                  // allow caller overrides
        ...(token ? {"Authorization": `Bearer ${token}` } : {}),  // attach JWT if available
    };

    // Only add Content-Type if there's a body and caller didn't set it
    if (options.body && !headers["Content-Type"]) {
        headers["Content-Type"] = "application/json";
    }

    try {
        let response = await originalFetch(url, { ...options, headers, credentials: "include" });

        // 401/403 → try refresh
        if ((response.status === 401 || response.status === 403)) {
            if (!token) {
                // No token to refresh → force logout
                return logout();
            }

            if (!isRefreshing) {
                isRefreshing = true;

                try {
                    const newToken = await handleRefresh();
                    isRefreshing = false;

                    // Retry queued requests
                    refreshQueue.forEach(cb => cb(newToken))
                    refreshQueue = [];

                    // Retry original request with new token
                    headers["Authorization"] = `Bearer ${newToken}`;
                    return originalFetch(url, { ...options, headers, credentials: "include" });
                } catch (error) {
                    isRefreshing = false;
                    refreshQueue = [];
                    return logout();
                }
            } else {
                // Queue requests while refresh is in progress
                return new Promise((resolve, reject) => {
                    refreshQueue.push((newToken) => {
                        headers["Authorization"] = `Bearer ${newToken}`;
                        originalFetch(url, { ...options, headers, credentials: "include" })
                            .then(resolve)
                            .catch(reject);
                    });
                });
            }
        }

        return response;
    } catch (error) {
        console.error("Network error:", error);
        throw error;
    }
}
