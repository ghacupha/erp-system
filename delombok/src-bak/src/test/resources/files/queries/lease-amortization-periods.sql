WITH RECURSIVE LeasePeriods AS (
    SELECT *
    FROM lease_period
    WHERE start_date <= '2023-06-15'
      AND end_date >= '2023-06-15'

    UNION ALL

    SELECT lp.*
    FROM LeasePeriods prev
             JOIN lease_period lp ON lp.sequence_number = prev.sequence_number + 1
    WHERE prev.end_date < '2030-06-15'
)
SELECT * FROM LeasePeriods;

-- Adjustment to use specified number of periods
WITH RECURSIVE LeasePeriods AS (
    SELECT *,
           1 AS iteration
    FROM lease_period
    WHERE start_date <= '2023-06-15'
      AND end_date >= '2023-06-15'

    UNION ALL

    SELECT lp.*,
           prev.iteration + 1 AS iteration
    FROM LeasePeriods prev
             JOIN lease_period lp ON lp.sequence_number = prev.sequence_number + 1
    WHERE prev.end_date < '2030-06-15'  -- Adjust the end date condition as needed
      AND prev.iteration < 5  -- Limit the number of iterations
)
SELECT * FROM LeasePeriods;

WITH RECURSIVE LeasePeriods AS (
    SELECT *,
           1 AS iteration
    FROM lease_period
    WHERE start_date <= '2023-06-15'
      AND end_date >= '2023-06-15'

    UNION ALL

    SELECT lp.*,
           prev.iteration + 1 AS iteration
    FROM LeasePeriods prev
             JOIN lease_period lp ON lp.sequence_number = prev.sequence_number + 1
    WHERE prev.end_date < (
        SELECT MAX(end_date) FROM lease_period -- This computes the end date condition so that the query never exceeds the periods available currently on the leaseTermPeriods table
    )
      AND prev.iteration < 5  -- Limit the number of iterations
)
SELECT * FROM LeasePeriods;

-- lease periods given the rou-model-metadata
WITH RouMetadata AS (
    SELECT
           lease_term_periods AS leaseTermPeriods,
           commencement_date AS commencementDate
    FROM rou_model_metadata
    WHERE id = :rouModelMetadataId
),
LeasePeriods AS (
 SELECT *,
        1 AS iteration
 FROM lease_period
 WHERE start_date <= (SELECT commencementDate FROM RouMetadata)
   AND end_date >= (SELECT commencementDate FROM RouMetadata)

 UNION ALL

 SELECT lp.*,
        prev.iteration + 1 AS iteration
 FROM LeasePeriods prev
          JOIN lease_period lp ON lp.sequence_number = prev.sequence_number + 1
 WHERE prev.end_date < (
     SELECT MAX(end_date) FROM lease_period
 )
   AND prev.iteration < (SELECT leaseTermPeriods FROM RouMetadata)
)
SELECT * FROM LeasePeriods;
