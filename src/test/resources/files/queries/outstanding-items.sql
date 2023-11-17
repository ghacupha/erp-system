SELECT 
         wipr.id, 
         wipr.sequence_number, 
         s.payment_number, 
         s.payment_date, 
         wipr.particulars, 
         d.dealer_name, 
         c.iso_4217_currency_code, 
         COALESCE(wipr.total_instalment_amount, 0) as total_instalment_amount, 
         COALESCE(wipt.total_transfer_amount, 0) AS total_transfer_amount, 
         (COALESCE(wipr.total_instalment_amount, 0) - COALESCE(wipt.total_transfer_amount, 0)) AS total_outstanding_amount 
         FROM (SELECT 
         w.id, 
         w.sequence_number, 
         w.particulars, 
         w.settlement_currency_id, 
         w.settlement_transaction_id, 
         w.dealer_id, 
         SUM(w.instalment_amount) as total_instalment_amount 
         FROM work_in_progress_registration w 
         GROUP BY w.id, w.sequence_number, w.particulars, w.settlement_currency_id, w.settlement_transaction_id, w.dealer_id) wipr 
         LEFT JOIN dealer d ON d.id = wipr.dealer_id 
         LEFT JOIN settlement_currency c ON c.id = wipr.settlement_currency_id 
         LEFT JOIN (SELECT 
         work_in_progress_registration_id, 
         SUM(transfer_amount) AS total_transfer_amount 
         FROM work_in_progress_transfer 
         GROUP BY work_in_progress_registration_id) wipt ON wipt.work_in_progress_registration_id = wipr.id 
         LEFT JOIN settlement s ON s.id = wipr.settlement_transaction_id 
         WHERE s.payment_date <= '2023-11-30' 
         ORDER BY s.payment_date ,
        countQuery =  SELECT COUNT(*) FROM (SELECT 
             w.id, 
             w.sequence_number, 
             w.particulars, 
             w.settlement_currency_id, 
             w.settlement_transaction_id, 
             w.dealer_id, 
             SUM(w.instalment_amount) as total_instalment_amount 
             FROM work_in_progress_registration w 
             GROUP BY w.id, w.sequence_number, w.particulars, w.settlement_currency_id, w.settlement_transaction_id, w.dealer_id) wipr 
             LEFT JOIN settlement s ON s.id = wipr.settlement_transaction_id 
             WHERE s.payment_date <= '2023-11-30'