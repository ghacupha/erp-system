----------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------
-- Data on the lease_period will be exactly the same as the data on amortization_period
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------
INSERT INTO lease_period (id, sequence_number, start_date, end_date, period_code, fiscal_month_id)
-- Use the sequence_generator sequence to generate the next-val which is used as the id
SELECT nextval('sequence_generator'), sequence_number, start_date, end_date, period_code, fiscal_month_id
FROM amortization_period;
