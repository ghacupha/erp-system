SELECT
    ta.id,
    ta.transfer_date,
    ta.transfer_type,
    ta.description,
    d.dealer_name,
    ta.transfer_amount
FROM work_in_progress_transfer ta
         JOIN settlement s ON s.id = ta.settlement_id
         JOIN dealer d ON d.id = s.biller_id
WHERE ta.transfer_date <= '2014-12-31'
  AND ta.work_in_progress_registration_id IS NULL
