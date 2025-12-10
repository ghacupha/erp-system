# Lease payment upload activation (client)

## Scope
Details the UI work for paging recent lease payment uploads and exposing activation controls.

## Changes
- The uploads table now requests paged data (size 5, sorted by `createdAt` DESC) and renders pagination controls with item counts derived from JHipster pagination headers.
- Each upload row conditionally displays an **Activate** or **Deactivate** button. The activate button calls the new `/activate` endpoint and replaces the row with the response payload.
- Error surfaces remain inline on the upload screen, and pagination respects the current page when refreshing after activation/deactivation.

## UX impact
Operators can review uploads five at a time in reverse chronological order, quickly see whether an upload is active, and toggle activation without leaving the page.
