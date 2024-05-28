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

import io.github.erp.domain.RouDepreciationEntryReportItem;
import io.github.erp.internal.model.RouDepreciationEntryReportItemInternal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the RouDepreciationEntryReportItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalRouDepreciationEntryReportItemRepository
    extends JpaRepository<RouDepreciationEntryReportItem, Long>, JpaSpecificationExecutor<RouDepreciationEntryReportItem> {

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "    rde.id,  " +
            "    CAST( rmm.model_title AS VARCHAR) AS leaseContractNumber,  " +
            "    CAST( lp.period_code AS VARCHAR) AS fiscalPeriodCode, " +
            "    CAST( fm.end_date AS DATE) AS fiscalPeriodEndDate, " +
            "    CAST( ac.asset_category_name AS VARCHAR) AS assetCategoryName, " +
            "    CAST( dta.account_number AS VARCHAR) AS debitAccountNumber, " +
            "    CAST( cta.account_number AS VARCHAR) AS creditAccountNumber , " +
            "    CAST( rde.description AS VARCHAR) AS description, " +
            "    CAST( ifr.short_title AS VARCHAR) AS shortTitle,  " +
            "    CAST(rou_asset_identifier AS VARCHAR) AS rouAssetIdentifier,  " +
            "    CAST(rde.sequence_number AS INTEGER) AS sequenceNumber,  " +
            "    CAST(depreciation_amount AS NUMERIC) AS depreciationAmount,  " +
            "    CAST(outstanding_amount AS NUMERIC) AS outstandingAmount           " +
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
            "    (invalidated=false OR invalidated IS NULL)",
        countQuery = "" +
            "SELECT  " +
            "    rde.id,  " +
            "    CAST( rmm.model_title AS VARCHAR) AS leaseContractNumber,  " +
            "    CAST( lp.period_code AS VARCHAR) AS fiscalPeriodCode, " +
            "    CAST( fm.end_date AS DATE) AS fiscalPeriodEndDate, " +
            "    CAST( ac.asset_category_name AS VARCHAR) AS assetCategoryName, " +
            "    CAST( dta.account_number AS VARCHAR) AS debitAccountNumber, " +
            "    CAST( cta.account_number AS VARCHAR) AS creditAccountNumber , " +
            "    CAST( rde.description AS VARCHAR) AS description, " +
            "    CAST( ifr.short_title AS VARCHAR) AS shortTitle,  " +
            "    CAST(rou_asset_identifier AS VARCHAR) AS rouAssetIdentifier,  " +
            "    CAST(rde.sequence_number AS INTEGER) AS sequenceNumber,  " +
            "    CAST(depreciation_amount AS NUMERIC) AS depreciationAmount,  " +
            "    CAST(outstanding_amount AS NUMERIC) AS outstandingAmount           " +
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
            "    (invalidated=false OR invalidated IS NULL)"
    )
    Page<RouDepreciationEntryReportItemInternal> allDepreciationItemsReport(Pageable pageable);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "    rde.id,  " +
            "    CAST( rmm.model_title AS VARCHAR) AS leaseContractNumber,  " +
            "    CAST( lp.period_code AS VARCHAR) AS fiscalPeriodCode, " +
            "    CAST( fm.end_date AS DATE) AS fiscalPeriodEndDate, " +
            "    CAST( ac.asset_category_name AS VARCHAR) AS assetCategoryName, " +
            "    CAST( dta.account_number AS VARCHAR) AS debitAccountNumber, " +
            "    CAST( cta.account_number AS VARCHAR) AS creditAccountNumber , " +
            "    CAST( rde.description AS VARCHAR) AS description, " +
            "    CAST( ifr.short_title AS VARCHAR) AS shortTitle,  " +
            "    CAST(rou_asset_identifier AS VARCHAR) AS rouAssetIdentifier,  " +
            "    CAST(rde.sequence_number AS INTEGER) AS sequenceNumber,  " +
            "    CAST(depreciation_amount AS NUMERIC) AS depreciationAmount,  " +
            "    CAST(outstanding_amount AS NUMERIC) AS outstandingAmount           " +
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
            "    (invalidated=false OR invalidated IS NULL) AND " +
            "    rde.rou_metadata_id = :metadataId",
        countQuery = "" +
            "SELECT  " +
            "    rde.id,  " +
            "    CAST( rmm.model_title AS VARCHAR) AS leaseContractNumber,  " +
            "    CAST( lp.period_code AS VARCHAR) AS fiscalPeriodCode, " +
            "    CAST( fm.end_date AS DATE) AS fiscalPeriodEndDate, " +
            "    CAST( ac.asset_category_name AS VARCHAR) AS assetCategoryName, " +
            "    CAST( dta.account_number AS VARCHAR) AS debitAccountNumber, " +
            "    CAST( cta.account_number AS VARCHAR) AS creditAccountNumber , " +
            "    CAST( rde.description AS VARCHAR) AS description, " +
            "    CAST( ifr.short_title AS VARCHAR) AS shortTitle,  " +
            "    CAST(rou_asset_identifier AS VARCHAR) AS rouAssetIdentifier,  " +
            "    CAST(rde.sequence_number AS INTEGER) AS sequenceNumber,  " +
            "    CAST(depreciation_amount AS NUMERIC) AS depreciationAmount,  " +
            "    CAST(outstanding_amount AS NUMERIC) AS outstandingAmount           " +
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
            "    (invalidated=false OR invalidated IS NULL) AND " +
            "    rde.rou_metadata_id = :metadataId"
    )
    Page<RouDepreciationEntryReportItemInternal> getAllByMetadataId(Pageable pageable, @Param("metadataId")long metadataId);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "    rde.id,  " +
            "    rmm.model_title AS leaseContractNumber,  " +
            "    lp.period_code AS fiscalPeriodCode, " +
            "    fm.end_date AS fiscalPeriodEndDate, " +
            "    ac.asset_category_name AS assetCategoryName, " +
            "    dta.account_number AS debitAccountNumber, " +
            "    cta.account_number AS creditAccountNumber, " +
            "    rde.description AS description, " +
            "    CONCAT(ifr.short_title,' ',rmm.description) AS shortTitle,  " +
            "    CAST(rou_asset_identifier AS TEXT)  AS rouAssetIdentifier,  " +
            "    rde.sequence_number AS sequenceNumber,  " +
            "    depreciation_amount AS depreciationAmount,  " +
            "    outstanding_amount AS outstandingAmount           " +
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
            "    rde.id,  " +
            "    rmm.model_title AS leaseContractNumber,  " +
            "    lp.period_code AS fiscalPeriodCode, " +
            "    fm.end_date AS fiscalPeriodEndDate, " +
            "    ac.asset_category_name AS assetCategoryName, " +
            "    dta.account_number AS debitAccountNumber, " +
            "    cta.account_number AS creditAccountNumber, " +
            "    rde.description AS description, " +
            "    CONCAT(ifr.short_title,' ',rmm.description) AS shortTitle,  " +
            "    CAST(rou_asset_identifier AS text) AS rouAssetIdentifier,  " +
            "    rde.sequence_number AS sequenceNumber,  " +
            "    depreciation_amount AS depreciationAmount,  " +
            "    outstanding_amount AS outstandingAmount           " +
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
    Page<RouDepreciationEntryReportItemInternal> getAllByLeasePeriodId(Pageable pageable, @Param("leasePeriodId")long leasePeriodId);


    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "    rde.id,  " +
            "    rmm.model_title AS leaseContractNumber,  " +
            "    lp.period_code AS fiscalPeriodCode, " +
            "    fm.end_date AS fiscalPeriodEndDate, " +
            "    ac.asset_category_name AS assetCategoryName, " +
            "    dta.account_number AS debitAccountNumber, " +
            "    cta.account_number AS creditAccountNumber, " +
            "    rde.description AS description, " +
            "    CONCAT(ifr.short_title,' ',rmm.description) AS shortTitle,  " +
            "    rou_asset_identifier AS rouAssetIdentifier,  " +
            "    rde.sequence_number AS sequenceNumber,  " +
            "    depreciation_amount AS depreciationAmount,  " +
            "    outstanding_amount AS outstandingAmount           " +
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
            "    lp.id = :leasePeriodId AND " +
            "    rde.id = :depreciationEntryId"
    )
    Optional<RouDepreciationEntryReportItemInternal> getOneByLeasePeriodId(@Param("depreciationEntryId")long depreciationEntryId, @Param("leasePeriodId")long leasePeriodId);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "    rde.id,  " +
            "    CAST( rmm.model_title AS VARCHAR) AS leaseContractNumber,  " +
            "    CAST( lp.period_code AS VARCHAR) AS fiscalPeriodCode, " +
            "    CAST( fm.end_date AS DATE) AS fiscalPeriodEndDate, " +
            "    CAST( ac.asset_category_name AS VARCHAR) AS assetCategoryName, " +
            "    CAST( dta.account_number AS VARCHAR) AS debitAccountNumber, " +
            "    CAST( cta.account_number AS VARCHAR) AS creditAccountNumber , " +
            "    CAST( rde.description AS VARCHAR) AS description, " +
            "    CAST( ifr.short_title AS VARCHAR) AS shortTitle,  " +
            "    CAST(rou_asset_identifier AS VARCHAR) AS rouAssetIdentifier,  " +
            "    CAST(rde.sequence_number AS INTEGER) AS sequenceNumber,  " +
            "    CAST(depreciation_amount AS NUMERIC) AS depreciationAmount,  " +
            "    CAST(outstanding_amount AS NUMERIC) AS outstandingAmount           " +
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
            "    (invalidated=false OR invalidated IS NULL) AND " +
            "    rde.id = :depreciationEntryId"
    )
    Optional<RouDepreciationEntryReportItemInternal> getOneByDepreciationEntryId(@Param("depreciationEntryId")long depreciationEntryId);
}
