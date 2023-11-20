SELECT
wipr.id,
wipr.sequence_number,
wipr.particulars,
d.dealer_name,
c.iso_4217_currency_code,
COALESCE(wipr.total_instalment_amount, 0) as total_instalment_amount,
COALESCE(wipt.total_transfer_amount, 0) AS total_transfer_amount,
(COALESCE(wipr.total_instalment_amount, 0) - COALESCE(wipt.total_transfer_amount, 0)) AS total_outstanding_amount
FROM (SELECT
wp.id,
wp.sequence_number,
wp.particulars,
wp.settlement_currency_id,
wp.settlement_transaction_id,
wp.dealer_id,
SUM(wp.instalment_amount) as total_instalment_amount
FROM work_in_progress_registration wp
GROUP BY wp.id, wp.sequence_number, wp.particulars, wp.settlement_currency_id, wp.settlement_transaction_id, wp.dealer_id ) wipr
LEFT JOIN dealer d ON d.id = wipr.dealer_id
LEFT JOIN settlement_currency c ON c.id = wipr.settlement_currency_id
LEFT JOIN (SELECT
wipr.id AS wipr_id,
SUM(wpt.transfer_amount) AS total_transfer_amount
FROM work_in_progress_registration wipr
LEFT JOIN work_in_progress_transfer wpt ON wipr.id = wpt.work_in_progress_registration_id
GROUP BY wipr.id) wipt ON wipt.wipr_id = wipr.id
LEFT JOIN settlement s ON s.id = wipr.settlement_transaction_id
WHERE s.payment_date <= :reportDate
ORDER BY s.payment_date
