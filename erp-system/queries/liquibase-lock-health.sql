-- Liquibase lock review and stale-lock recovery.
-- Use this only after confirming no ERP server instance is still starting or migrating.

select
    id,
    locked,
    lockgranted,
    lockedby
from databasechangeloglock;

select
    pid,
    usename,
    client_addr,
    application_name,
    state,
    query_start,
    wait_event_type,
    wait_event,
    query
from pg_stat_activity
where datname = current_database()
  and (
      query ilike '%databasechangelog%'
      or application_name ilike '%postgresql jdbc%'
      or state <> 'idle'
  )
order by query_start nulls last;

-- If the lock is stale and the pg_stat_activity review shows no active Liquibase migration,
-- release it with:
--
-- update databasechangeloglock
-- set locked = false,
--     lockgranted = null,
--     lockedby = null
-- where id = 1
--   and locked = true;
