package io.github.erp.internal.repository;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
