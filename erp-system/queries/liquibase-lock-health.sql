--
-- Erp System - Mark X No 12 (Kadar Series) Server ver 1.9.0
-- Copyright © 2021 - 2026 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program. If not, see <http://www.gnu.org/licenses/>.
--

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
