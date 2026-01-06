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

-- Bootstrap lease_template rows from existing IFRS16 lease contracts,
-- lease liabilities, and transaction-account rule tables.
--
-- Mapping assumptions:
-- - template_title = ifrs16lease_contract.booking_id
-- - service_outlet_id/main_dealer_id copied from the lease contract
-- - depreciation/accrued depreciation accounts from taamortization_rule debit/credit
-- - interest paid transfer accounts from tainterest_paid_transfer_rule debit/credit
-- - interest accrued accounts from talease_interest_accrual_rule debit/credit
-- - lease recognition accounts from talease_recognition_rule debit/credit
-- - lease repayment accounts from talease_repayment_rule debit/credit
-- - ROU recognition accounts from tarecognitionrourule debit/credit
-- - asset_account_id defaults to the ROU recognition debit account
-- - asset_category_id is sourced from rou_model_metadata using the lease contract id
--
-- The query only inserts templates for lease contracts that already have
-- a lease liability and do not yet reference a lease template.

WITH source_data AS (
  SELECT
    lc.id AS lease_contract_id,
    lc.booking_id AS template_title,
    lc.superintendent_service_outlet_id AS service_outlet_id,
    lc.main_dealer_id AS main_dealer_id,
    rmm.asset_category_id AS asset_category_id,
    ar.debit_id AS depreciation_account_id,
    ar.credit_id AS accrued_depreciation_account_id,
    ipt.debit_id AS interest_paid_transfer_debit_account_id,
    ipt.credit_id AS interest_paid_transfer_credit_account_id,
    lia.debit_id AS interest_accrued_debit_account_id,
    lia.credit_id AS interest_accrued_credit_account_id,
    lrec.debit_id AS lease_recognition_debit_account_id,
    lrec.credit_id AS lease_recognition_credit_account_id,
    lrep.debit_id AS lease_repayment_debit_account_id,
    lrep.credit_id AS lease_repayment_credit_account_id,
    rour.debit_id AS rou_recognition_debit_account_id,
    rour.credit_id AS rou_recognition_credit_account_id
  FROM ifrs16lease_contract lc
  JOIN lease_liability ll
    ON ll.lease_contract_id = lc.id
  LEFT JOIN taamortization_rule ar
    ON ar.lease_contract_id = lc.id
  LEFT JOIN LATERAL (
    SELECT rmm.asset_category_id
    FROM rou_model_metadata rmm
    WHERE rmm.ifrs16lease_contract_id = lc.id
    ORDER BY rmm.id DESC
    LIMIT 1
  ) rmm ON true
  LEFT JOIN tainterest_paid_transfer_rule ipt
    ON ipt.lease_contract_id = lc.id
  LEFT JOIN talease_interest_accrual_rule lia
    ON lia.lease_contract_id = lc.id
  LEFT JOIN talease_recognition_rule lrec
    ON lrec.lease_contract_id = lc.id
  LEFT JOIN talease_repayment_rule lrep
    ON lrep.lease_contract_id = lc.id
  LEFT JOIN tarecognitionrourule rour
    ON rour.lease_contract_id = lc.id
  WHERE lc.lease_template_id IS NULL
),
inserted AS (
  INSERT INTO lease_template (
    id,
    template_title,
    asset_account_id,
    depreciation_account_id,
    accrued_depreciation_account_id,
    interest_paid_transfer_debit_account_id,
    interest_paid_transfer_credit_account_id,
    interest_accrued_debit_account_id,
    interest_accrued_credit_account_id,
    lease_recognition_debit_account_id,
    lease_recognition_credit_account_id,
    lease_repayment_debit_account_id,
    lease_repayment_credit_account_id,
    rou_recognition_credit_account_id,
    rou_recognition_debit_account_id,
    asset_category_id,
    service_outlet_id,
    main_dealer_id
  )
  SELECT
    nextval('sequence_generator') AS id,
    sd.template_title,
    sd.rou_recognition_debit_account_id AS asset_account_id,
    sd.depreciation_account_id,
    sd.accrued_depreciation_account_id,
    sd.interest_paid_transfer_debit_account_id,
    sd.interest_paid_transfer_credit_account_id,
    sd.interest_accrued_debit_account_id,
    sd.interest_accrued_credit_account_id,
    sd.lease_recognition_debit_account_id,
    sd.lease_recognition_credit_account_id,
    sd.lease_repayment_debit_account_id,
    sd.lease_repayment_credit_account_id,
    sd.rou_recognition_credit_account_id,
    sd.rou_recognition_debit_account_id,
    sd.asset_category_id,
    sd.service_outlet_id,
    sd.main_dealer_id
  FROM source_data sd
  WHERE NOT EXISTS (
    SELECT 1
    FROM lease_template lt
    WHERE lt.template_title = sd.template_title
  )
  RETURNING id, template_title
)
UPDATE ifrs16lease_contract lc
SET lease_template_id = inserted.id
FROM inserted
WHERE lc.booking_id = inserted.template_title
  AND lc.lease_template_id IS NULL;
