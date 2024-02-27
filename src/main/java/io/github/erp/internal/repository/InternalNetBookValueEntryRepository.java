package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.domain.NetBookValueEntryInternal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the NetBookValueEntry entity.
 */
@Repository
public interface InternalNetBookValueEntryRepository extends JpaRepository<NetBookValueEntry, Long>, JpaSpecificationExecutor<NetBookValueEntry> {
    @Query(
        value = "select distinct netBookValueEntry from NetBookValueEntry netBookValueEntry LEFT JOIN fetch netBookValueEntry.placeholders",
        countQuery = "select count(distinct netBookValueEntry) from NetBookValueEntry netBookValueEntry"
    )
    Page<NetBookValueEntry> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct netBookValueEntry from NetBookValueEntry netBookValueEntry LEFT JOIN fetch netBookValueEntry.placeholders")
    List<NetBookValueEntry> findAllWithEagerRelationships();

    @Query(
        "select netBookValueEntry from NetBookValueEntry netBookValueEntry LEFT JOIN fetch netBookValueEntry.placeholders where netBookValueEntry.id =:id"
    )
    Optional<NetBookValueEntry> findOneWithEagerRelationships(@Param("id") Long id);

//    @Query(value = "" +
//            "SELECT " +
//            "  nbv.id AS id, " +
//            "  nbv.asset_number AS assetNumber, " +
//            "  nbv.asset_tag AS assetTag, " +
//            "  nbv.asset_description AS assetDescription, " +
//            "  nbv.nbv_identifier AS nbvIdentifier, " +
//            "  nbv.compilation_job_identifier AS compilationJobIdentifier, " +
//            "  nbv.compilation_batch_identifier AS compilationBatchIdentifier, " +
//            "  nbv.elapsed_months AS elapsedMonths, " +
//            "  nbv.prior_months AS priorMonths, " +
//            "  nbv.useful_life_years AS usefulLifeYears, " +
//            "  nbv.net_book_value_amount AS netBookValueAmount, " +
//            "  nbv.previous_net_book_value_amount AS previousNetBookValueAmount, " +
//            "  nbv.historical_cost AS historicalCost, " +
//            "  ar.capitalization_date AS capitalizationDate," +
//            "  svt.outlet_code AS serviceOutlet, " +
//            "  pd.period_code AS depreciationPeriod, " +
//            "  mon.fiscal_month_code AS fiscalMonth, " +
//            "  depreciation_method_name AS depreciationMethod, " +
//            "  cat.asset_category_name AS assetCategory " +
//            "FROM net_book_value_entry nbv " +
//            "  LEFT JOIN asset_registration ar on nbv.asset_registration_id = ar.id " +
//            "  LEFT JOIN service_outlet svt on nbv.service_outlet_id = svt.id " +
//            "  LEFT JOIN depreciation_period pd on  nbv.depreciation_period_id = pd.id" +
//            "  LEFT JOIN fiscal_month mon on pd.fiscal_month_id = mon.id " +
//            "  LEFT JOIN asset_category cat on nbv.asset_category_id = cat.id " +
//            "  LEFT JOIN depreciation_method dep on cat.depreciation_method_id = dep.id " +
//            "WHERE " +
//            "pd.id = :depreciationPeriodId", nativeQuery = true)
//    Page<NetBookValueEntryInternal> getNBVEntryByDepreciationPeriod(@Param("depreciationPeriodId") Long depreciationPeriodId, Pageable pageable);

//    @Query(value = "" +
//        " SELECT " +
//        " nbv.id AS id," +
//        "    nbv.asset_number AS assetNumber, " +
//        "    nbv.asset_tag AS assetTag," +
//        "    nbv.asset_description AS assetDescription, " +
//        "    nbv.nbv_identifier::varchar AS nbvIdentifier," +
//        "    nbv.compilation_job_identifier::varchar AS compilationJobIdentifier, " +
//        "    nbv.compilation_batch_identifier::varchar AS compilationBatchIdentifier," +
//        "    nbv.elapsed_months AS elapsedMonths, " +
//        "    nbv.prior_months AS priorMonths, " +
//        "    nbv.useful_life_years AS usefulLifeYears," +
//        "    nbv.net_book_value_amount AS netBookValueAmount, " +
//        "    nbv.previous_net_book_value_amount AS previousNetBookValueAmount," +
//        "    nbv.historical_cost AS historicalCost," +
//        "    ar.capitalization_date AS capitalizationDate," +
//        "    svt.outlet_code AS serviceOutlet, " +
//        "    pd.period_code AS depreciationPeriod, " +
//        "    mon.fiscal_month_code AS fiscalMonth," +
//        "    depreciation_method_name AS depreciationMethod, " +
//        "    cat.asset_category_name AS assetCategory " +
//        " FROM net_book_value_entry nbv " +
//        "    LEFT JOIN asset_registration ar on nbv.asset_registration_id = ar.id " +
//        "    LEFT JOIN service_outlet svt on nbv.service_outlet_id = svt.id " +
//        "    LEFT JOIN depreciation_period pd on  nbv.depreciation_period_id = pd.id " +
//        "    LEFT JOIN fiscal_month mon on pd.fiscal_month_id = mon.id " +
//        "    LEFT JOIN asset_category cat on nbv.asset_category_id = cat.id " +
//        "    LEFT JOIN depreciation_method dep on cat.depreciation_method_id = dep.id " +
//        "  WHERE  pd.id = '95851'", nativeQuery = true)
//    Page<NetBookValueEntryInternal> getNBVEntryByDepreciationPeriod(@Param("depreciationPeriodId") Long depreciationPeriodId, Pageable pageable);

//    @Query(value = "" +
//        "SELECT " +
//        "    nbv.id AS id," +
//        "    nbv.asset_number AS assetNumber, " +
//        "    nbv.asset_tag AS assetTag," +
//        "    nbv.asset_description AS assetDescription, " +
//        "    nbv.nbv_identifier::varchar AS nbvIdentifier," +  // Casting UUID to varchar
//        "    nbv.compilation_job_identifier::varchar AS compilationJobIdentifier, " +  // Casting UUID to varchar
//        "    nbv.compilation_batch_identifier::varchar AS compilationBatchIdentifier," +  // Casting UUID to varchar
//        "    nbv.elapsed_months AS elapsedMonths, " +
//        "    nbv.prior_months AS priorMonths, " +
//        "    nbv.useful_life_years AS usefulLifeYears," +
//        "    nbv.net_book_value_amount AS netBookValueAmount, " +
//        "    nbv.previous_net_book_value_amount AS previousNetBookValueAmount," +
//        "    nbv.historical_cost AS historicalCost," +
//        "    ar.capitalization_date AS capitalizationDate," +
//        "    svt.outlet_code AS serviceOutlet, " +
//        "    pd.period_code AS depreciationPeriod, " +
//        "    mon.fiscal_month_code AS fiscalMonth," +
//        "    depreciation_method_name AS depreciationMethod, " +
//        "    cat.asset_category_name AS assetCategory " +
//        "FROM net_book_value_entry nbv " +
//        "    LEFT JOIN asset_registration ar ON nbv.asset_registration_id = ar.id " +
//        "    LEFT JOIN service_outlet svt ON nbv.service_outlet_id = svt.id " +
//        "    LEFT JOIN depreciation_period pd ON nbv.depreciation_period_id = pd.id " +
//        "    LEFT JOIN fiscal_month mon ON pd.fiscal_month_id = mon.id " +
//        "    LEFT JOIN asset_category cat ON nbv.asset_category_id = cat.id " +
//        "    LEFT JOIN depreciation_method dep ON cat.depreciation_method_id = dep.id " +
//        "WHERE pd.id = :depreciationPeriodId", nativeQuery = true)
//    Page<NetBookValueEntryInternal> getNBVEntryByDepreciationPeriod(@Param("depreciationPeriodId") Long depreciationPeriodId, Pageable pageable);

    @Query(value = "" +
        "SELECT " +
        "    nbv.id AS id," +
        "    nbv.asset_number AS assetNumber, " +
        "    nbv.asset_tag AS assetTag," +
        "    nbv.asset_description AS assetDescription, " +
        "    CAST(nbv.nbv_identifier AS varchar) AS nbvIdentifier," +
        "    CAST(nbv.compilation_job_identifier AS varchar) AS compilationJobIdentifier, " +
        "    CAST(nbv.compilation_batch_identifier AS varchar) AS compilationBatchIdentifier," +
        "    nbv.elapsed_months AS elapsedMonths, " +
        "    nbv.prior_months AS priorMonths, " +
        "    nbv.useful_life_years AS usefulLifeYears," +
        "    nbv.net_book_value_amount AS netBookValueAmount, " +
        "    nbv.previous_net_book_value_amount AS previousNetBookValueAmount," +
        "    nbv.historical_cost AS historicalCost," +
        "    ar.capitalization_date AS capitalizationDate," +
        "    svt.outlet_code AS serviceOutlet, " +
        "    pd.period_code AS depreciationPeriod, " +
        "    mon.fiscal_month_code AS fiscalMonth," +
        "    depreciation_method_name AS depreciationMethod, " +
        "    cat.asset_category_name AS assetCategory " +
        "FROM net_book_value_entry nbv " +
        "    LEFT JOIN asset_registration ar ON nbv.asset_registration_id = ar.id " +
        "    LEFT JOIN service_outlet svt ON nbv.service_outlet_id = svt.id " +
        "    LEFT JOIN depreciation_period pd ON nbv.depreciation_period_id = pd.id " +
        "    LEFT JOIN fiscal_month mon ON pd.fiscal_month_id = mon.id " +
        "    LEFT JOIN asset_category cat ON nbv.asset_category_id = cat.id " +
        "    LEFT JOIN depreciation_method dep ON cat.depreciation_method_id = dep.id " +
        "WHERE pd.id = :depreciationPeriodId", nativeQuery = true)
    Page<NetBookValueEntryInternal> getNBVEntryByDepreciationPeriod(@Param("depreciationPeriodId") Long depreciationPeriodId, Pageable pageable);

}
