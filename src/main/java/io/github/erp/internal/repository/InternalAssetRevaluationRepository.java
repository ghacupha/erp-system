package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.domain.AssetDisposal;
import io.github.erp.domain.AssetRevaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AssetRevaluation entity.
 */
@Repository
public interface InternalAssetRevaluationRepository extends JpaRepository<AssetRevaluation, Long>, JpaSpecificationExecutor<AssetRevaluation> {
    @Query(
        value = "select distinct assetRevaluation from AssetRevaluation assetRevaluation left join fetch assetRevaluation.placeholders",
        countQuery = "select count(distinct assetRevaluation) from AssetRevaluation assetRevaluation"
    )
    Page<AssetRevaluation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct assetRevaluation from AssetRevaluation assetRevaluation left join fetch assetRevaluation.placeholders")
    List<AssetRevaluation> findAllWithEagerRelationships();

    @Query(
        "select assetRevaluation from AssetRevaluation assetRevaluation left join fetch assetRevaluation.placeholders where assetRevaluation.id =:id"
    )
    Optional<AssetRevaluation> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "    id, " +
            "   description, " +
            "   devaluation_amount, " +
            "   revaluation_date, " +
            "   revaluation_reference_id, " +
            "   time_of_creation, " +
            "   revaluer_id, " +
            "   created_by_id, " +
            "   last_modified_by_id, " +
            "   last_accessed_by_id, " +
            "   effective_period_id, " +
            "   revalued_asset_id " +
            " FROM public.asset_revaluation ar" +
            " LEFT JOIN depreciation_period dp ON ar.effective_period_id = dp.id " +
            " WHERE revalued_asset_id = :revaluedAssetId " +
            " AND dp.start_date <= CAST ( :depreciationPeriodStartDate AS DATE)"
    )
    Optional<List<AssetRevaluation>> findAssetRevaluation(@Param("revaluedAssetId")Long disposedAssetId, @Param("depreciationPeriodStartDate") LocalDate depreciationPeriodStartDate);
}
