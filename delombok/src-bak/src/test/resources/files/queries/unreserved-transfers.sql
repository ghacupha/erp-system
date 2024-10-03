SELECT 
wt.id, 
wt.description, 
s.outlet_code, 
st.payment_number, 
st.payment_date, 
wt.transfer_amount, 
wt.transfer_type
FROM public.work_in_progress_transfer wt 
LEFT JOIN service_outlet s ON s.id = wt.service_outlet_id 
LEFT JOIN settlement st ON st.id = wt.settlement_id 
WHERE wt.work_in_progress_registration_id IS NULL;