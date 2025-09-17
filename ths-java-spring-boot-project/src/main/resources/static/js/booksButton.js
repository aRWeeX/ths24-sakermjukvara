// booksButton.js

import { apiFetch } from "./apiFetch.js";

export function setupBooksButton(buttonId, listId, paginationId) {
    // Wait for DOM to be fully loaded
    document.addEventListener("DOMContentLoaded", () => {
        const booksButton = document.getElementById(buttonId);
        const booksList = document.getElementById(listId);
        const paginationContainer = document.getElementById(paginationId);

        if (!booksButton || !booksList || !paginationContainer) {
            console.error("Books button, list, or pagination container not found");
            return;
        }

        let currentPage = 0;
        const pageSize = 10;

        // Load a page of books
        async function loadBooks(page) {
            booksList.innerHTML = "Loading...";

            try {
                paginationContainer.querySelectorAll("button")
                    .forEach(b => b.disabled = true);

                const response = await apiFetch(`/api/books?page=${page}&size=${pageSize}`);

                if (!response.ok) {
                    console.error("Failed to fetch books:", response.status);
                    return;
                }

                const books = await response.json();  // PageDto<BookDto>
                const booksArray = books.content;

                // Clear previous results
                booksList.innerHTML = "";

                // Render books
                booksArray.forEach(book => {
                    const li = document.createElement("li");
                    li.textContent = `${book.title} by ${book.authorFirstName} ${book.authorLastName}`;
                    booksList.appendChild(li);
                });

                // Update current page and render pagination
                currentPage = page;
                renderPagination(currentPage, books.totalPages);

                booksList.scrollIntoView({ behavior: "smooth" });
            } catch (error) {
                booksList.innerHTML = "Error loading books.";
                console.error("Error fetching books:", error);
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
            prev.addEventListener("click", () => loadBooks(current - 1));

            paginationContainer.appendChild(prev);

            // Page number buttons
            for (let i = 0; i < total; i++) {
                const button = document.createElement("button");
                button.textContent = `${i + 1}`;
                button.disabled = i === current;
                button.style.margin = "0 4px";

                // Highlight current page
                button.style.fontWeight = i === current ? "bold" : "normal";

                button.addEventListener("click", () => loadBooks(i));
                paginationContainer.appendChild(button);
            }

            // Next button
            const next = document.createElement("button");

            next.textContent = "Next";
            next.disabled = current === total - 1;
            next.style.margin = "0 4px";
            next.addEventListener("click", () => loadBooks(current + 1));

            paginationContainer.appendChild(next);
        }

        // Initial load when button is clicked
        booksButton.addEventListener("click", () => loadBooks(0))
    });
}
