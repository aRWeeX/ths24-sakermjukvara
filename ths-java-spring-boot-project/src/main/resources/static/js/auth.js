// js/auth.js

// Logout function: remove token and redirect to login
export function logout() {
    localStorage.removeItem("jwt_token");
    window.location.href = "/login";
}
