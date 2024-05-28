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

import io.github.erp.domain.RouAssetNBVReportItem;
import io.github.erp.internal.model.RouAssetNBVReportItemInternal;
import io.github.erp.internal.model.RouDepreciationEntryReportItemInternal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RouAssetNBVReportItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalRouAssetNBVReportItemRepository
    extends JpaRepository<RouAssetNBVReportItem, Long>, JpaSpecificationExecutor<RouAssetNBVReportItem> {

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "    rmm.id AS id,  " +
            "    rmm.model_title AS modelTitle,  " +
            "    rmm.model_version AS modelVersion,  " +
            "    CONCAT(ifr.short_title,' ',rmm.description) AS description,  " +
            "    CAST(rou_asset_identifier AS TEXT) AS rouModelReference, " +
            "    rmm.commencement_date AS commencementDate, " +
            "    rmm.expiration_date AS expirationDate, " +
            "    ac.asset_category_name AS assetCategoryName, " +
            "    dta.account_number AS depreciationAccountNumber, " +
            "    cta.account_number AS assetAccountNumber, " +
            "    fm.end_date AS fiscalPeriodEndDate, " +
            "    rmm.lease_amount AS leaseAmount, " +
            "    outstanding_amount AS netBookValue             " +
            " FROM rou_depreciation_entry rde  " +
            "    LEFT JOIN lease_period lp ON lease_period_id = lp.id  " +
            "    LEFT JOIN fiscal_month fm ON lp.fiscal_month_id = fm.id  " +
            "    LEFT JOIN asset_category ac ON asset_category_id = ac.id  " +
            "    LEFT JOIN transaction_account dta ON debit_account_id = dta.id  " +
            "    LEFT JOIN transaction_account cta ON credit_account_id = cta.id  " +
            "    LEFT JOIN rou_model_metadata rmm ON rou_metadata_id = rmm.id " +
            "    LEFT JOIN ifrs16lease_contract ifr ON rmm.ifrs16lease_contract_id = ifr.id " +
            " WHERE (is_deleted=false OR is_deleted IS NULL) AND       " +
            "    (activated=true OR activated IS NULL) AND       " +
            "    (invalidated=false OR invalidated IS NULL) AND  " +
            "    lp.id = :leasePeriodId",
        countQuery = "" +
            "SELECT  " +
            "    rmm.id AS id,  " +
            "    rmm.model_title AS modelTitle,  " +
            "    rmm.model_version AS modelVersion,  " +
            "    CONCAT(ifr.short_title,' ',rmm.description) AS description,  " +
            "    CAST(rou_asset_identifier AS TEXT) AS rouModelReference, " +
            "    rmm.commencement_date AS commencementDate, " +
            "    rmm.expiration_date AS expirationDate, " +
            "    ac.asset_category_name AS assetCategoryName, " +
            "    dta.account_number AS depreciationAccountNumber, " +
            "    cta.account_number AS assetAccountNumber, " +
            "    fm.end_date AS fiscalPeriodEndDate, " +
            "    rmm.lease_amount AS leaseAmount, " +
            "    outstanding_amount AS netBookValue             " +
            " FROM rou_depreciation_entry rde  " +
            "    LEFT JOIN lease_period lp ON lease_period_id = lp.id  " +
            "    LEFT JOIN fiscal_month fm ON lp.fiscal_month_id = fm.id  " +
            "    LEFT JOIN asset_category ac ON asset_category_id = ac.id  " +
            "    LEFT JOIN transaction_account dta ON debit_account_id = dta.id  " +
            "    LEFT JOIN transaction_account cta ON credit_account_id = cta.id  " +
            "    LEFT JOIN rou_model_metadata rmm ON rou_metadata_id = rmm.id " +
            "    LEFT JOIN ifrs16lease_contract ifr ON rmm.ifrs16lease_contract_id = ifr.id " +
            " WHERE (is_deleted=false OR is_deleted IS NULL) AND       " +
            "    (activated=true OR activated IS NULL) AND       " +
            "    (invalidated=false OR invalidated IS NULL) AND  " +
            "    lp.id = :leasePeriodId"
    )
    Page<RouAssetNBVReportItemInternal> getAllByLeasePeriodId(Pageable pageable, @Param("leasePeriodId")long leasePeriodId);

}
