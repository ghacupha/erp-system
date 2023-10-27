SELECT
    w.id,
    w.sequence_number,
    w.particulars,
    d.dealer_name,
    c.iso_4217_currency_code,
    w.instalment_amount as im,
    COALESCE(SUM(ta.transfer_amount), 0) AS total_transfer_amount,
    (w.instalment_amount - COALESCE(SUM(ta.transfer_amount), 0)) AS outstanding_amount
FROM work_in_progress_registration w
         JOIN public.dealer d ON d.id = w.dealer_id
         JOIN settlement_currency c ON c.id = w.settlement_currency_id
         LEFT JOIN work_in_progress_transfer ta ON ta.work_in_progress_registration_id = w.id
GROUP BY w.sequence_number,
         w.particulars,
         d.dealer_name,
         c.iso_4217_currency_code,
         w.instalment_amount,
         w.id
ORDER BY w.id;

