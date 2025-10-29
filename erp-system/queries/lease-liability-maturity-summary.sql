-- Native query backing the lease liability maturity summary endpoint.
WITH target_period AS (
    SELECT id, sequence_number
    FROM lease_repayment_period
    WHERE id = :leasePeriodId
),
schedule AS (
    SELECT
        COALESCE(contract.booking_id, '') AS lease_id,
        COALESCE(dealer.dealer_name, '') AS dealer_name,
        rp.sequence_number,
        tp.sequence_number AS anchor_sequence,
        COALESCE(llsi.cash_payment, 0) AS cash_payment
    FROM lease_liability_schedule_item llsi
    JOIN ifrs16lease_contract contract ON contract.id = llsi.lease_contract_id
    LEFT JOIN dealer ON dealer.id = contract.main_dealer_id
    JOIN lease_repayment_period rp ON rp.id = llsi.lease_period_id
    CROSS JOIN target_period tp
    WHERE rp.sequence_number >= tp.sequence_number
)
SELECT
    lease_id,
    dealer_name,
    SUM(CASE WHEN sequence_number = anchor_sequence THEN cash_payment ELSE 0 END) AS current_period,
    SUM(
        CASE
            WHEN sequence_number > anchor_sequence AND sequence_number <= anchor_sequence + 12 THEN cash_payment
            ELSE 0
        END
    ) AS next_twelve_months,
    SUM(
        CASE
            WHEN sequence_number > anchor_sequence + 12 THEN cash_payment
            ELSE 0
        END
    ) AS beyond_twelve_months,
    SUM(cash_payment) AS total_undiscounted
FROM schedule
GROUP BY lease_id, dealer_name
HAVING SUM(cash_payment) <> 0
ORDER BY lease_id;
