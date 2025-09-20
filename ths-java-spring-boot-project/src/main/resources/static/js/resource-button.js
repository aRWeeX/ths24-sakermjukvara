// resource-button.js

import { apiFetch } from "./apiFetch.js";

export function setupResourceButton(
    buttonId, listId, paginationId, endpoint, renderItem, showCondition = () => true) {

    // Wait for DOM to be fully loaded
    document.addEventListener("DOMContentLoaded", () => {
        const resourceButton = document.getElementById(buttonId);
        const resourceList = document.getElementById(listId);
        const paginationContainer = document.getElementById(paginationId);

        if (!resourceButton || !resourceList || !paginationContainer) {
            console.error(`${endpoint} button, list, or pagination container not found`);
            return;
        }

        // Hide button if condition is false
        if (!showCondition()) {
            resourceButton.style.display = "none";
            return;
        }

        let currentPage = 0;
        const pageSize = 10;

        // Load a page of resource, endpoint
        async function loadResource(page) {
            resourceList.innerHTML = "Loading...";

            try {
                paginationContainer.querySelectorAll("button")
                    .forEach(b => b.disabled = true);

                const response = await apiFetch(`/api/${endpoint}?page=${page}&size=${pageSize}`);

                if (!response.ok) {
                    console.error(`Failed to fetch ${endpoint}:`, response.status);
                    return;
                }

                const data = await response.json();  // PageDto
                const items = data.content;

                // Clear previous results
                resourceList.innerHTML = "";

                // Render items
                items.forEach(item => {
                    const li = document.createElement("li");
                    li.innerHTML = renderItem(item);
                    resourceList.appendChild(li);
                });

                // Update current page and render pagination
                currentPage = page;
                renderPagination(currentPage, data.totalPages);

                resourceList.scrollIntoView({ behavior: "smooth" });
            } catch (error) {
                resourceList.innerHTML = `Error loading ${endpoint}.`;
                console.error(`Error fetching ${endpoint}:`, error);
            }
        }

        // Render pagination controls
        function renderPagination(current, total) {
            paginationContainer.innerHTML = "";

            // Prev button
            const prev = document.createElement("button");

            prev.textContent = "Prev";
            prev.disabled = current === 0;
            prev.style.margin = "0 4px";
            prev.addEventListener("click", () => loadResource(current - 1));

            paginationContainer.appendChild(prev);

            // Page number buttons
            for (let i = 0; i < total; i++) {
                const button = document.createElement("button");
                button.textContent = `${i + 1}`;
                button.disabled = i === current;
                button.style.margin = "0 4px";

                // Highlight current page
                button.style.fontWeight = i === current ? "bold" : "normal";

                button.addEventListener("click", () => loadResource(i));
                paginationContainer.appendChild(button);
            }

            // Next button
            const next = document.createElement("button");

            next.textContent = "Next";
            next.disabled = current === total - 1;
            next.style.margin = "0 4px";
            next.addEventListener("click", () => loadResource(current + 1));

            paginationContainer.appendChild(next);
        }

        // Initial load when button is clicked
        resourceButton.addEventListener("click", () => loadResource(0))
    });
}
