// signup.js

/**
 * Initializes the signup form by attaching a submit event listener.
 * On submission, it prevents default form behavior, collects form data,
 * sends an async POST request to the registration API,
 * shows any error messages, and redirects on success.
 */
export function initSignupForm() {
    const form = document.getElementById("signupForm");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());

        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        const result = await response.json();
        const errorDiv = document.getElementById("error");
        errorDiv.textContent = "";

        if (!response.ok) {
            errorDiv.textContent = result.error;
        } else {
            // Registration successful
            window.location.href = "/login?registered";
        }
    });
}
