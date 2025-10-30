-- Lease Liability Maturity Summary
-- --------------------------------
-- Buckets the outstanding lease liability principal and interest payable into short-, medium- and long-term maturity bands
-- relative to the selected lease repayment period. This mirrors the native query defined in
-- InternalLeaseLiabilityScheduleReportItemRepository#getLeaseLiabilityMaturitySummary.

WITH target_period AS (
    SELECT lp.end_date AS target_end_date
    FROM lease_repayment_period lp
    WHERE lp.id = :leasePeriodId
), maturity_data AS (
    SELECT CASE
               WHEN maturity_days <= 365 THEN '≤365 days'
               WHEN maturity_days BETWEEN 366 AND 1824 THEN '366–1824 days'
               ELSE '≥1825 days'
           END AS maturity_label,
           COALESCE(llsi.outstanding_balance, 0) AS lease_principal,
           COALESCE(llsi.interest_payable_closing, 0) AS interest_payable
    FROM lease_liability_schedule_item llsi
    JOIN lease_liability ll ON ll.id = llsi.lease_liability_id
    CROSS JOIN target_period tp
    CROSS JOIN LATERAL (
        SELECT
            GREATEST(
                COALESCE(
                    DATE_PART('day', ll.end_date::timestamp - tp.target_end_date::timestamp),
                    0
                ),
                0
            )::bigint AS maturity_days
    ) maturity
    WHERE llsi.lease_period_id = :leasePeriodId
)
SELECT maturity_label,
       SUM(lease_principal) AS lease_principal,
       SUM(interest_payable) AS interest_payable,
       SUM(lease_principal + interest_payable) AS total_amount
FROM maturity_data
GROUP BY maturity_label
HAVING SUM(lease_principal) <> 0 OR SUM(interest_payable) <> 0
ORDER BY CASE maturity_label
            WHEN '≤365 days' THEN 1
            WHEN '366–1824 days' THEN 2
            ELSE 3
         END;
