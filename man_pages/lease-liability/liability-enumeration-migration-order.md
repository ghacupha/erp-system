# Liability Enumeration Migration Ordering

## Context

Production migration to `jdbc:postgresql://10.60.27.22:5435/erpSystem` failed while applying the liability enumeration foreign key to `lease_payment_upload`. The failing change set was executed before the changelog that creates `lease_payment_upload`, so PostgreSQL correctly rejected the constraint with `relation "public.lease_payment_upload" does not exist`.

## Change

The original liability enumeration constraint change set is now a guarded no-op with `validCheckSum=ANY`. A deferred changelog runs after `LeasePaymentUpload` and the `lease_payment` upload column are created, then adds the four liability enumeration constraints only when they do not already exist.

This keeps databases that already migrated successfully from receiving duplicate constraints, while allowing partially migrated production databases to continue forward without manual table surgery.

## Operational Notes

After deployment, Liquibase should create or confirm these constraints:

- `fk_liability_enumeration__ifrs16lease_contract_id`
- `fk_liability_enumeration__lease_payment_upload_id`
- `fk_present_value_enumeration__ifrs16lease_contract_id`
- `fk_present_value_enumeration__liability_enumeration_id`

The JHipster registry error seen in development logs is separate from this database failure. It indicates the registry process is trying to contact a Eureka endpoint that is not accepting connections. For the isolated development stack, use the dev registry published on port `8771` and the dev Elasticsearch endpoint on port `8840`.

If a previous failed startup left Liquibase locked, review `erp-system/queries/liquibase-lock-health.sql`. Release the lock only after confirming that no ERP server process is still running a migration against the same PostgreSQL database.

For repository-driven Liquibase operations, use `erp-system/scripts/liquibase-compose.cmd`. It resolves the same database connection variables used by the Docker Compose production server before running Maven Liquibase commands.
