# Posting Rule Search Pagination

## Persona
Ledger administrator managing lease posting rules.

## Scenario
Keep searching through posting rules when the results span multiple pages.

## Steps
1. Open **Ledgers → Posting Rules** and focus on the search input.
2. Enter a keyword or account identifier to find matching posting rules.
3. Scroll to the end of the initial results and request more results (pagination/infinite scroll).
4. Continue scrolling until the relevant posting rule appears.

## Expected Outcome
- The search results continue loading beyond the first page when more matches exist.
- The total number of matching posting rules reflects the full result set, not just the current page.
- Any rules deleted from the database are omitted from the list, even if they still appear in the search index.
