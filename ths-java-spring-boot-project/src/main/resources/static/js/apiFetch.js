// apiFetch.js

/**
 * apiFetch - A wrapper around the native fetch function with JWT handling and automatic redirection.
 *
 * Features:
 * 1. Automatically attaches a JWT token from localStorage to the Authorization header if available.
 * 2. Sets "Content-Type" to "application/json" by default (can be overridden via options).
 * 3. Handles HTTP 401/403 responses by redirecting the user to the login page.
 * 4. Catches and logs network errors, then rethrows them for further handling.
 *
 * Usage:
 *   const response = await apiFetch("/api/data", { method: "GET" });
 */
const originalFetch = window.fetch;

export async function apiFetch(url, options = {}) {
    // --- Request Interceptor ---
    const token = localStorage.getItem("jwt_token");

    const headers = {
        "Content-Type": "application/json",  // default header
        ...options.headers,                  // allow caller overrides
        ...(token ? { "Authorization": `Bearer ${token}` } : {}),  // attach JWT if available
    };

    try {
        const response = await originalFetch(url, { ...options, headers });

        // --- Response Interceptor ---
        if (response.status === 401 || response.status === 403) {
            // Unauthorized → redirect to login
            window.location.href = "/login";
        }

        return response;
    } catch (error) {
        console.error("Network error:", error);  // log for debugging
        throw error;  // let caller handle it
    }
}
