
-- These are just notes on how we intend to update the wip table to include
-- instalment-date column.
-- This is necessary in order to facilitate sorting because if you wanted such
-- a list to be sorted in order of transactional precedence it's going to be hard.
-- It was thought easier to have such a column even if the update is itself
-- internally managed, within the service. For now we just need to copy this info
-- From the settlement table and apply a copy to the wip registration table.
UPDATE work_in_progress_registration wpr
SET instalment_date = (
    SELECT s.payment_date
    FROM settlement s
    WHERE s.id = wpr.settlement_transaction_id
);
