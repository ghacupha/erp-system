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

-- Lease Liability Maturity Summary
-- --------------------------------
-- Buckets the outstanding lease liability principal and interest payable into short-, medium- and long-term maturity bands
-- relative to the selected lease repayment period. This mirrors the native query defined in
-- InternalLeaseLiabilityScheduleReportItemRepository#getLeaseLiabilityMaturitySummary.

WITH target_period AS (
    SELECT lp.end_date AS target_end_date
    FROM lease_repayment_period lp
    WHERE lp.id = :leasePeriodId
), maturity_data AS (
    SELECT CASE
               WHEN maturity_days <= 365 THEN '≤365 days'
               WHEN maturity_days BETWEEN 366 AND 1824 THEN '366–1824 days'
               ELSE '≥1825 days'
               END AS maturity_label,
           COALESCE(llsi.outstanding_balance, 0) AS lease_principal,
           COALESCE(llsi.interest_payable_closing, 0) AS interest_payable
    FROM lease_liability_schedule_item llsi
             JOIN lease_liability ll ON ll.id = llsi.lease_liability_id
             CROSS JOIN target_period tp
             CROSS JOIN LATERAL (
        SELECT GREATEST(COALESCE(EXTRACT(DAY FROM (ll.end_date - tp.target_end_date) * INTERVAL '1 day'), 0), 0)::bigint AS maturity_days
        ) maturity
    WHERE llsi.lease_period_id = :leasePeriodId
)
SELECT maturity_label,
       SUM(lease_principal) AS lease_principal,
       SUM(interest_payable) AS interest_payable,
       SUM(lease_principal + interest_payable) AS total_amount
FROM maturity_data
GROUP BY maturity_label
HAVING SUM(lease_principal) <> 0 OR SUM(interest_payable) <> 0
ORDER BY CASE maturity_label
             WHEN '≤365 days' THEN 1
             WHEN '366–1824 days' THEN 2
             ELSE 3
             END;
