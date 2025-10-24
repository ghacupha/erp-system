-- Sample verification query for the ROU Asset Balance by Account report
-- Bind the :lease_period_id parameter with the identifier supplied in the
-- /api/leases/rou-account-balance-report-items/reports/{leasePeriodId} request.

SELECT
    rabi.id,
    rabi.asset_account_number,
    rabi.asset_account_name,
    rabi.opening_balance,
    rabi.depreciation_for_period,
    rabi.closing_balance,
    rabi.lease_period_id
FROM
    rou_account_balance_report_item rabi
WHERE
    rabi.lease_period_id = :lease_period_id
ORDER BY
    rabi.asset_account_number;
