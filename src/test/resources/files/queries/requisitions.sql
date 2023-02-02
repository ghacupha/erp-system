-- Items in requisition today
SELECT id, description, serial_number, time_of_requisition, requisition_number, payment_amount, payment_status, settlement_currency_id, current_owner_id, native_owner_id, native_department_id, biller_id
FROM public.settlement_requisition r
WHERE r.time_of_requisition >= timestamp '2023-02-02 00:00:00'
  AND r.time_of_requisition < timestamp '2023-02-02 13:31:00'
  AND r.biller_id = '8155';

-- Items in requisition processed today
SELECT id, description, serial_number, time_of_requisition, requisition_number, payment_amount, payment_status, settlement_currency_id, current_owner_id, native_owner_id, native_department_id, biller_id
FROM public.settlement_requisition r
WHERE r.time_of_requisition >= timestamp '2023-02-02 00:00:00'
  AND r.time_of_requisition < timestamp '2023-02-02 13:31:00'
  AND r.payment_status = 'PROCESSED';


-- Items sent for further approval
SELECT id, description, serial_number, time_of_requisition, requisition_number, payment_amount, payment_status, settlement_currency_id, current_owner_id, native_owner_id, native_department_id, biller_id
FROM public.settlement_requisition r
WHERE r.time_of_requisition >= timestamp '2023-02-02 00:00:00'
  AND r.time_of_requisition < timestamp '2023-02-02 13:31:00'
  AND r.payment_status = 'SENT_FOR_FURTHER_APPROVAL';
