--
-- Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
-- Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

-- Manually toggle the activation flag for all schedule items linked to a compilation
-- Substitute :compilation_id with the primary key of the lease_liability_compilation record.

-- Deactivate an entire compilation
UPDATE lease_liability_schedule_item
SET active = FALSE
WHERE lease_liability_compilation_id = :compilation_id;

-- Reactivate a compilation when the schedule needs to be promoted again
UPDATE lease_liability_schedule_item
SET active = TRUE
WHERE lease_liability_compilation_id = :compilation_id;
