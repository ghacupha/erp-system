-- Pending transactions
SELECT DISTINCT ON (time_of_requisition, requisition_number, r.id)
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
  AND r.payment_status <> 'PROCESSED'
  AND r.id = (SELECT MAX(sr.id) FROM public.settlement_requisition sr WHERE sr.requisition_number = r.requisition_number )
  AND time_of_requisition = (SELECT MAX(sr.time_of_requisition) FROM public.settlement_requisition sr WHERE sr.requisition_number = r.requisition_number )
ORDER BY  time_of_requisition DESC, requisition_number;
