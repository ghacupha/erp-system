-- Manually toggle the activation flag for all schedule items linked to a compilation
-- Substitute :compilation_id with the primary key of the lease_liability_compilation record.

-- Deactivate an entire compilation
UPDATE lease_liability_schedule_item
SET active = FALSE
WHERE lease_liability_compilation_id = :compilation_id;

-- Reactivate a compilation when the schedule needs to be promoted again
UPDATE lease_liability_schedule_item
SET active = TRUE
WHERE lease_liability_compilation_id = :compilation_id;
