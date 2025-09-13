// js/datetime-utils.js

/**
 * Formats an ISO timestamp into a locale-based string (date + time).
 * Automatically respects the user's browser locale and timezone.
 *
 * @param {string} isoString - The ISO 8601 timestamp from backend.
 * @returns {string} Formatted date and time string.
 */
export function formatDateTime(isoString) {
    const date = new Date(isoString);

    return date.toLocaleString(undefined, {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit"
    });
}
