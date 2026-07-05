# Settlement Search Resilience User Story

## Persona

An accounts officer searches settlement records while reviewing payments and supporting invoices.

## Steps

1. The officer opens the Settlement list.
2. They enter a payment number, description or related keyword in the search field.
3. The system returns matching settlement rows.

## Expected Result

Only settlement records that still exist in the PostgreSQL database are returned. Stale Elasticsearch documents are filtered out before the response reaches the UI, and pagination totals reflect the filtered PostgreSQL-valid result set.
