// signup.js

// Sets up signup form submission: sends data to the API, shows errors, redirects on success.
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

        let result;

        try {
            result = await response.json();
        } catch {
            const errorDiv = document.getElementById("error");
            errorDiv.textContent = "Unexpected server response. Please try again.";
            return;
        }

        if (!result.success) {
            const errorDiv = document.getElementById("error");
            errorDiv.style.display = "block";
            errorDiv.innerHTML = "";

            // If there are field errors, show them
            if (result.data) {
                const fieldErrors = [];
                const globalErrors = [];

                Object.entries(result.data).forEach(([key, messages]) => {
                    // If the key matches a field in the form, treat it as a field error
                    const fieldInput = document.querySelector(`[name="${key}"]`);

                    if (fieldInput) {
                        fieldErrors.push(`<strong>${key}:</strong> ${messages.join(", ")}`);
                    } else {
                        // Otherwise treat as a global/class-level error
                        globalErrors.push(...messages);
                    }
                });

                // Combine and render
                errorDiv.innerHTML = [
                    ...fieldErrors,
                    ...globalErrors.map(msg => `<em>${msg}</em>`),
                ].join("<br />");
            } else {
                // Generic error
                errorDiv.textContent = result.message;
            }
        } else {
            // Registration successful
            window.location.href = "/login?registered";
        }
    });
}
