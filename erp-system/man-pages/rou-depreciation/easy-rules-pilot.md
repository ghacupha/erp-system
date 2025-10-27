# ROU Depreciation Validation Rules Engine Pilot

## Background

The ROU depreciation validation service coordinates invalidate/revalidate batch jobs for a depreciation request. Prior to this change, the service triggered Spring Batch jobs directly with inline conditional logic. That path suffered from two gaps:

* Duplicate invalidation requests silently retriggered processing even when the request had already been flagged as invalidated.
* Both invalidate and revalidate flows dereferenced the previous batch job identifier without checking for null values, risking runtime exceptions when the identifier was not yet assigned (common for a first-time invalidation).

## Easy Rules integration

We embedded the [Easy Rules](https://github.com/j-easy/easy-rules) engine to evaluate the invariants that gate each transition. `RouDepreciationValidationRuleEngine` assembles two rule sets:

* **Invalidation rule set** – prevents double invalidation and backfills a placeholder previous batch identifier when the request is invalidated for the first time.
* **Revalidation rule set** – ensures only invalidated requests can be revalidated and demands an existing batch identifier so the Spring Batch pipeline can locate the prior run.

Each call to `RouDepreciationValidationServiceImpl.invalidate(...)` or `.revalidate(...)` converts the current database state to a DTO, feeds it into the rules engine, and logs any informational guidance emitted by the rules. If a rule vetoes the transition, the service now throws an `IllegalStateException` with the aggregated error messages.

The asynchronous batch launchers use a helper that resolves the “previous” batch identifier from the rule result, guaranteeing that the Spring Batch job parameters never contain a `null` value.

## Operational impact

* **Safer transitions** – Invalidations that would have previously double-processed a request are intercepted before any state change or job launch occurs.
* **Deterministic job parameters** – Batch jobs always receive a valid UUID for the `previousBatchJobIdentifier` parameter, avoiding the null pointer scenario observed in lower environments.
* **Extensibility** – Additional validation rules can be appended to either rule set without touching the orchestration code, providing a template for future policy changes (e.g., segregation-of-duty approvals or scheduling windows).

## Next steps

1. Instrument rule outcomes with metrics once we have enough history to identify the most common violations.
2. Catalogue future candidates (amortisation, NBV policy enforcement) that can reuse the rule engine infrastructure introduced here.
3. Evaluate whether rule definitions should be externalised (e.g., YAML/decision tables) if business stakeholders request runtime configurability.
