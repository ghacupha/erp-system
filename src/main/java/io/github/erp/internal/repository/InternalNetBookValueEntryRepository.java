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

    @Query(nativeQuery = true,
        value = "" +
            "SELECT " +
            "  nbv.id AS id, " +
            "  nbv.asset_number, " +
            "  nbv.asset_tag, " +
            "  nbv.asset_description, " +
            "  nbv.nbv_identifier, " +
            "  nbv.compilation_job_identifier, " +
            "  nbv.compilation_batch_identifier, " +
            "  nbv.elapsed_months, " +
            "  nbv.prior_months, " +
            "  nbv.useful_life_years, " +
            "  nbv.net_book_value_amount, " +
            "  nbv.previous_net_book_value_amount, " +
            "  nbv.historical_cost, " +
            "  svt.outlet_code AS service_outlet_code, " +
            "  pd.period_code AS depreciation_period_code, " +
            "  mon.fiscal_month_code AS fiscal_month_code, " +
            "  depreciation_method_name, " +
            "  cat.asset_category_name " +
            " FROM net_book_value_entry nbv " +
            "  LEFT JOIN service_outlet svt on nbv.service_outlet_id = svt.id " +
            "  LEFT JOIN depreciation_period pd on  nbv.depreciation_period_id = pd.id" +
            "  LEFT JOIN fiscal_month mon on pd.fiscal_month_id = mon.id " +
            "  LEFT JOIN asset_category cat on nbv.asset_category_id = cat.id " +
            "  LEFT JOIN depreciation_method dep on cat.depreciation_method_id = dep.id " +
            "WHERE pd.id = :depreciationPeriodId",
        countQuery = "select count( * ) " +
            " from net_book_value_entry nbv " +
            "    LEFT JOIN depreciation_period pd on  nbv.depreciation_period_id = pd.id " +
            "  WHERE pd.id = :depreciationPeriodId"
    )
    Page<NetBookValueEntryInternal> getNBVEntryByDepreciationPeriod(@Param("depreciationPeriodId") Long depreciationPeriodId, Pageable pageable);
}
