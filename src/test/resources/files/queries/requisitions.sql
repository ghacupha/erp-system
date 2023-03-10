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

-- Pending transactions
SELECT
    r.id,
    description,
    time_of_requisition,
    requisition_number,
    s.iso_4217_currency_code,
    payment_amount,
    payment_status,
    d.dealer_name as biller,
    a.application_identity as current_owner,
    n.application_identity as native_owner,
    nd.dealer_name as native_department
FROM public.settlement_requisition r,
     public.settlement_currency s,
     public.dealer d,
     public.application_user a,
     public.application_user n,
     public.dealer nd
WHERE r.settlement_currency_id = s.id
  AND r.native_department_id = nd.id
  AND r.native_owner_id = n.id
  AND r.current_owner_id = a.id
  AND r.biller_id = d.id
  AND r.payment_status <> 'PROCESSED';
