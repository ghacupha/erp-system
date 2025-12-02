# User story: Reactivate and page through lease payment uploads

- **Persona:** Finance operations analyst managing lease payment imports.
- **Goal:** Review recent lease payment uploads five at a time in newest-first order, and reactivate an upload that was deactivated by mistake while keeping search results consistent.
- **Flow:**
  1. Open *Lease Payment Upload* and scroll to *Previous uploads*.
  2. Use the pagination controls to move through five-record pages of uploads sorted by created date descending.
  3. For an inactive upload, click **Activate** in the action column; for an active upload, click **Deactivate**.
  4. Confirm the status badge updates, and the list keeps the current page position.
- **Outcome:** The selected upload (and its lease payments) is marked active or inactive accordingly, and the search index is updated via the backend reindex queue.
