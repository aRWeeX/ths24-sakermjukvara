// auth-utils.js

export function getJwtPayload() {
    const token = localStorage.getItem("jwt_token");
    if (!token) return null;

    try {
        const payloadBase64 = token.split(".")[1];
        const payloadJson = atob(payloadBase64);
        return JSON.parse(payloadJson);
    } catch (e) {
        console.error("Failed to parse JWT token", e);
        return null;
    }
}

export function isAdmin() {
    const payload = getJwtPayload();
    if (!payload || !payload.role) return false;
    return payload.role === "ROLE_ADMIN";
}

// Logout function: remove token and redirect to login
export function logout() {
    localStorage.removeItem("jwt_token");
    window.location.href = "/login";
}
