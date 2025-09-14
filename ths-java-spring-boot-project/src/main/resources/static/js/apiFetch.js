// apiFetch.js
const originalFetch = window.fetch;

export async function apiFetch(url, options = {}) {
    // --- Request Interceptor ---
    const token = localStorage.getItem("jwt_token");

    const headers = {
        "Content-Type": "application/json",
        ...options.headers,
        ...(token ? { "Authorization": `Bearer ${token}` } : {}),
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
        console.error("Network error:", error);
        throw error;
    }
}
