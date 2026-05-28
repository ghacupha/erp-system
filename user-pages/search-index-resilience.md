# Search Index Resilience

## Searching Settlement Records

Use the existing Settlement search page and filters as before. Search results are matched by Elasticsearch, then checked against the primary database before they are shown by the API.

If an old search document remains in Elasticsearch after a failed or rolled-back database operation, it is ignored in the response. Pagination totals reflect the records that still exist in PostgreSQL. A normal reindex can still be run later to clean the index, but users should not see records that are missing from PostgreSQL.
