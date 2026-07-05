# Posting Rule Search Pagination Alignment

## Overview
Search results for Transaction Account Posting Rules are sourced from Elasticsearch and then reconciled against the relational database to ensure only valid entities are returned to the API consumer. This reconciliation guards against stale or deleted records in the search index.

## Issue Observed
The paging wrapper used to return `new PageImpl<>(results, pageable, results.size())`, which resets the total element count to the size of the current page. When Elasticsearch returned more than one page of results, the API response incorrectly reported that only a single page existed. Infinite scroll and pagination controls then stopped early even though more results were available.

## Resolution
The search workflow now preserves the total element count reported by Elasticsearch (`searchPage.getTotalElements()`) while still filtering out any entities that no longer exist in the database. This keeps pagination metadata accurate without reintroducing stale records into the response payload.

## Design Notes
- The API response still orders records based on Elasticsearch IDs so the search results remain deterministic.
- Missing entities continue to be filtered out, but the total count reflects the full search index result set to avoid truncating pagination.
- If further accuracy is required, a follow-up improvement could compute a database count over the filtered IDs; however, this adds another query and may not be necessary for the current workflow.
