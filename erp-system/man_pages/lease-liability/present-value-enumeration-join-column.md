# Present Value Enumeration join column fix

## Context
Processing Kafka events for present value enumerations failed with `SQLGrammarException` because Hibernate attempted to insert into the non-existent `lease_contract_id` column. The Liquibase changelog creates `present_value_enumeration` with an `ifrs16lease_contract_id` foreign key to `ifrs16lease_contract`, so the mismatch between the entity mapping and the table definition caused the batch insert to abort.

## Decision
Explicitly map the `leaseContract` relationship on `PresentValueEnumeration` to the `ifrs16lease_contract_id` column. The relationship remains required, and the join keeps the existing foreign key and index structure defined by Liquibase.

## Outcome
* Hibernate now targets the correct column, avoiding batch insert failures during Kafka consumer processing.
* No database schema changes are required; the entity mapping now reflects the existing Liquibase definition.
