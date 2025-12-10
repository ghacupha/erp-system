# Lease amortization schedule links to lease contracts

## Background
Batch uploads of amortization schedules were failing with `UK_ku26m0jh5gcgu3sde52hv815p` unique-constraint violations. The `lease_amortization_schedule` table enforced a one-to-one link to `ifrs16lease_contract`, even though multiple schedules can exist for the same contract when liabilities are recalculated.

## Change summary
- Switched the JPA mapping from `@OneToOne` to `@ManyToOne` so several schedules can reference the same IFRS16 lease contract while still requiring the reference.
- Dropped the database-level unique constraint so the schema aligns with the new mapping.
- Updated the JHipster entity metadata to keep future entity regenerations consistent.

## Operational considerations
Existing data is unaffected aside from removing the uniqueness restriction, and API payloads remain unchanged. No UI changes are required because the association continues to be populated through the lease liability workflow.
