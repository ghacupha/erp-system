package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.erp.domain.RouAccountBalanceReportItem;
import io.github.erp.internal.model.RouAccountBalanceReportItemInternal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RouAccountBalanceReportItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalRouAccountBalanceReportItemRepository
    extends JpaRepository<RouAccountBalanceReportItem, Long>, JpaSpecificationExecutor<RouAccountBalanceReportItem> {

    @Query(
        nativeQuery = true,
        value = "" +
            "WITH current_period AS ( " +
            "    SELECT  " +
            "        lp.id, " +
            "        lp.end_date " +
            "    FROM lease_period lp " +
            "    WHERE lp.id = :leasePeriodId " +
            "), " +
            "accrued_depreciation AS ( " +
            "    SELECT  " +
            "        rde.credit_account_id AS account_id, " +
            "        SUM(rde.depreciation_amount) AS accrued_depreciation_amount " +
            "    FROM rou_depreciation_entry rde " +
            "    LEFT JOIN lease_period lp ON rde.lease_period_id = lp.id " +
            "    WHERE  " +
            "        (rde.is_deleted = false OR rde.is_deleted IS NULL) AND " +
            "        (rde.activated = true OR rde.activated IS NULL) AND " +
            "        (rde.invalidated = false OR rde.invalidated IS NULL) AND " +
            "        lp.end_date <= (SELECT end_date FROM current_period) " +
            "    GROUP BY rde.credit_account_id " +
            "), " +
            "total_lease_amount AS ( " +
            "    SELECT  " +
            "        cta.id AS account_id, " +
            "        SUM(rmm.lease_amount) AS total_lease_amount " +
            "    FROM rou_model_metadata rmm " +
            "    LEFT JOIN transaction_account cta ON rmm.asset_account_id = cta.id " +
            "    WHERE  " +
            "        rmm.commencement_date <= (SELECT end_date FROM current_period) " +
            "    GROUP BY cta.id " +
            ") " +
            "SELECT  " +
            "    cta.id AS id, " +
            "    cta.account_name AS assetAccountName, " +
            "    cta.account_number AS assetAccountNumber, " +
            "    dta.account_number AS depreciationAccountNumber,     " +
            "    tla.total_lease_amount AS leaseAmount, " +
            "    ad.accrued_depreciation_amount AS accruedDepreciationAmount, " +
            "    SUM(rde.depreciation_amount) AS currentPeriodDepreciationAmount,  " +
            "    SUM(rde.outstanding_amount) AS netBookValue     " +
            "FROM rou_depreciation_entry rde  " +
            "LEFT JOIN lease_period lp ON rde.lease_period_id = lp.id  " +
            "LEFT JOIN fiscal_month fm ON lp.fiscal_month_id = fm.id  " +
            "LEFT JOIN asset_category ac ON rde.asset_category_id = ac.id  " +
            "LEFT JOIN transaction_account dta ON rde.debit_account_id = dta.id  " +
            "LEFT JOIN transaction_account cta ON rde.credit_account_id = cta.id  " +
            "LEFT JOIN rou_model_metadata rmm ON rde.rou_metadata_id = rmm.id " +
            "LEFT JOIN ifrs16lease_contract ifr ON rmm.ifrs16lease_contract_id = ifr.id " +
            "LEFT JOIN accrued_depreciation ad ON cta.id = ad.account_id " +
            "LEFT JOIN total_lease_amount tla ON cta.id = tla.account_id " +
            "WHERE  " +
            "    (rde.is_deleted = false OR rde.is_deleted IS NULL) AND " +
            "    (rde.activated = true OR rde.activated IS NULL) AND " +
            "    (rde.invalidated = false OR rde.invalidated IS NULL) AND " +
            "    lp.id = :leasePeriodId " +
            "GROUP BY cta.id, cta.account_name, dta.account_number, ad.accrued_depreciation_amount, tla.total_lease_amount ",
        countQuery = "" +
            "WITH current_period AS ( " +
            "    SELECT  " +
            "        lp.id, " +
            "        lp.end_date " +
            "    FROM lease_period lp " +
            "    WHERE lp.id = :leasePeriodId " +
            "), " +
            "accrued_depreciation AS ( " +
            "    SELECT  " +
            "        rde.credit_account_id AS account_id, " +
            "        SUM(rde.depreciation_amount) AS accrued_depreciation_amount " +
            "    FROM rou_depreciation_entry rde " +
            "    LEFT JOIN lease_period lp ON rde.lease_period_id = lp.id " +
            "    WHERE  " +
            "        (rde.is_deleted = false OR rde.is_deleted IS NULL) AND " +
            "        (rde.activated = true OR rde.activated IS NULL) AND " +
            "        (rde.invalidated = false OR rde.invalidated IS NULL) AND " +
            "        lp.end_date <= (SELECT end_date FROM current_period) " +
            "    GROUP BY rde.credit_account_id " +
            "), " +
            "total_lease_amount AS ( " +
            "    SELECT  " +
            "        cta.id AS account_id, " +
            "        SUM(rmm.lease_amount) AS total_lease_amount " +
            "    FROM rou_model_metadata rmm " +
            "    LEFT JOIN transaction_account cta ON rmm.asset_account_id = cta.id " +
            "    WHERE  " +
            "        rmm.commencement_date <= (SELECT end_date FROM current_period) " +
            "    GROUP BY cta.id " +
            ") " +
            "SELECT  " +
            "    cta.id AS id, " +
            "    cta.account_name AS assetAccountName, " +
            "    cta.account_number AS assetAccountNumber, " +
            "    dta.account_number AS depreciationAccountNumber,     " +
            "    tla.total_lease_amount AS leaseAmount, " +
            "    ad.accrued_depreciation_amount AS accruedDepreciationAmount, " +
            "    SUM(rde.depreciation_amount) AS currentPeriodDepreciationAmount,  " +
            "    SUM(rde.outstanding_amount) AS netBookValue     " +
            "FROM rou_depreciation_entry rde  " +
            "LEFT JOIN lease_period lp ON rde.lease_period_id = lp.id  " +
            "LEFT JOIN fiscal_month fm ON lp.fiscal_month_id = fm.id  " +
            "LEFT JOIN asset_category ac ON rde.asset_category_id = ac.id  " +
            "LEFT JOIN transaction_account dta ON rde.debit_account_id = dta.id  " +
            "LEFT JOIN transaction_account cta ON rde.credit_account_id = cta.id  " +
            "LEFT JOIN rou_model_metadata rmm ON rde.rou_metadata_id = rmm.id " +
            "LEFT JOIN ifrs16lease_contract ifr ON rmm.ifrs16lease_contract_id = ifr.id " +
            "LEFT JOIN accrued_depreciation ad ON cta.id = ad.account_id " +
            "LEFT JOIN total_lease_amount tla ON cta.id = tla.account_id " +
            "WHERE  " +
            "    (rde.is_deleted = false OR rde.is_deleted IS NULL) AND " +
            "    (rde.activated = true OR rde.activated IS NULL) AND " +
            "    (rde.invalidated = false OR rde.invalidated IS NULL) AND " +
            "    lp.id = :leasePeriodId " +
            "GROUP BY cta.id, cta.account_name, dta.account_number, ad.accrued_depreciation_amount, tla.total_lease_amount"
    )
    Page<RouAccountBalanceReportItemInternal> getRouAccountBalanceReportItemByLeasePeriodParameter(Pageable pageable, @Param("leasePeriodId") long leasePeriodId);
}
