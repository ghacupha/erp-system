SELECT
    w.id,
    w.sequence_number,
    s.payment_number,
    s.payment_date,
    w.particulars,
    d.dealer_name,
    c.iso_4217_currency_code,
    w.instalment_amount as im,
    COALESCE(SUM(ta.transfer_amount), 0) AS total_transfer_amount,
    (w.instalment_amount - COALESCE(SUM(ta.transfer_amount), 0)) AS outstanding_amount
FROM work_in_progress_registration w
         JOIN public.dealer d ON d.id = w.dealer_id
         JOIN settlement_currency c ON c.id = w.settlement_currency_id
         JOIN settlement s ON s.id = w.settlement_transaction_id
         LEFT JOIN work_in_progress_transfer ta ON ta.work_in_progress_registration_id = w.id
WHERE s.payment_date <= '2014-12-31'
  AND (ta.transfer_date IS NULL OR ta.transfer_date <= '2014-12-31') -- Only consider transfers on or before the reporting date
GROUP BY w.id, w.sequence_number, w.particulars, d.dealer_name, c.iso_4217_currency_code, w.instalment_amount, s.payment_date, s.payment_number
ORDER BY s.payment_date;

-- -- Unallocated Transfers
--
-- SELECT *
-- FROM work_in_progress_transfer w
-- WHERE W.work_in_progress_registration_id IS NULL
--   AND w.transfer_date <= '2014-12-31';
