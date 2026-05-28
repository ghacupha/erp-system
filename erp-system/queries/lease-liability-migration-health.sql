-- Review liability enumeration migration state after deploying the deferred constraints.
select table_name
from information_schema.tables
where table_schema = 'public'
  and table_name in ('liability_enumeration', 'present_value_enumeration', 'lease_payment_upload');

select conname as constraint_name,
       conrelid::regclass as table_name,
       confrelid::regclass as referenced_table
from pg_constraint
where conname in (
    'fk_liability_enumeration__ifrs16lease_contract_id',
    'fk_liability_enumeration__lease_payment_upload_id',
    'fk_present_value_enumeration__ifrs16lease_contract_id',
    'fk_present_value_enumeration__liability_enumeration_id'
)
order by conname;
