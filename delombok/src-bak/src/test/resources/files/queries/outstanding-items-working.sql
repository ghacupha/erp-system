--
-- Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
FROM (
    SELECT
        w.id,
        w.sequence_number,
        w.particulars,
	    w.settlement_currency_id,
	    w.settlement_transaction_id,
	    w.dealer_id,
        SUM(w.instalment_amount) as total_instalment_amount
    FROM work_in_progress_registration w
    GROUP BY w.id, w.sequence_number, w.particulars
) wipr
LEFT JOIN dealer d ON d.id = wipr.dealer_id
LEFT JOIN settlement_currency c ON c.id = wipr.settlement_currency_id
LEFT JOIN (
    SELECT
        work_in_progress_registration_id,
        SUM(transfer_amount) AS total_transfer_amount
    FROM work_in_progress_transfer 
    GROUP BY work_in_progress_registration_id
) wipt ON wipt.work_in_progress_registration_id = wipr.id
LEFT JOIN settlement s ON s.id = wipr.settlement_transaction_id
WHERE s.payment_date <= '2023-12-31'
ORDER BY s.payment_date;