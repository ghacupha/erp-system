# Posting Rule Indexing Resilience and Stack Overflow Mitigation

## Overview
When saving lease posting rules we observed stack overflow errors inside the Elasticsearch mapping converter. The root cause was recursive entity graphs (posting rule → templates/conditions → posting rule) being indexed synchronously in the same request lifecycle. This document describes the updated indexing flow that prevents recursive serialization and keeps Elasticsearch in sync without blocking the UI.

## What Changed
### Asynchronous indexing via Kafka
Posting rule changes now enqueue an indexing message after the database transaction commits. A dedicated Kafka consumer handles Elasticsearch updates for:
- `TransactionAccountPostingRule`
- `TransactionAccountPostingRuleTemplate`
- `TransactionAccountPostingRuleCondition`

This decouples Elasticsearch writes from the REST request and prevents stack overflow errors from bubbling to users.

### Sanitized entities for Elasticsearch
The consumer and startup indexers create shallow copies of posting rule entities before persisting to Elasticsearch. The sanitized versions:
- omit recursive references (templates/conditions do not point back to the parent rule),
- include only key searchable fields on related entities (e.g., account name/number and category metadata).

This avoids the recursive traversal that previously triggered stack overflows.

### Startup reindexing
New startup indexing services load posting rules, templates, and conditions from the database and hydrate Elasticsearch at application launch. This mirrors the transaction account indexing process and ensures the search index is repopulated even after a failed or delayed Kafka update.

### Search result validation
Search endpoints now reconcile Elasticsearch results with the database by reloading entity instances by ID. This prevents stale documents (present in Elasticsearch but absent from the database) from reaching dynamic UI components.

## Workflow Summary
1. User saves a posting rule in the UI.
2. The rule is persisted in the relational database.
3. After commit, an indexing message is published to Kafka.
4. The consumer fetches entities from the database, sanitizes them, and updates Elasticsearch.
5. Searches query Elasticsearch, then rehydrate matching IDs from the database.

## Implementation Notes
- The Kafka queue uses a dedicated topic and consumer group for posting rule indexing.
- Delete operations emit messages that remove the posting rule and its templates/conditions from the search index.
- Startup indexing services only add missing entries, allowing incremental reindexing without clobbering existing documents.

## Rationale
This approach preserves UI responsiveness, avoids recursive indexing crashes, and ensures users never see “phantom” search results that no longer exist in the database.
