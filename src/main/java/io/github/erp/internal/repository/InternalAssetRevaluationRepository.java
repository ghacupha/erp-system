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
            "   ar.id, " +
            "   description, " +
            "   devaluation_amount, " +
            "   revaluation_date, " +
            "   revaluation_reference_id, " +
            "   time_of_creation, " +
            "   revaluer_id, " +
            "   ar.created_by_id, " +
            "   ar.last_modified_by_id, " +
            "   last_accessed_by_id, " +
            "   effective_period_id, " +
            "   revalued_asset_id " +
            " FROM public.asset_revaluation ar " +
            " LEFT JOIN depreciation_period dp ON ar.effective_period_id = dp.id " +
            " WHERE revalued_asset_id = :revaluedAssetId " +
            " AND dp.start_date <= CAST ( :depreciationPeriodStartDate AS DATE)",
        countQuery = "" +
            "SELECT " +
            "   ar.id, " +
            "   description, " +
            "   devaluation_amount, " +
            "   revaluation_date, " +
            "   revaluation_reference_id, " +
            "   time_of_creation, " +
            "   revaluer_id, " +
            "   ar.created_by_id, " +
            "   ar.last_modified_by_id, " +
            "   last_accessed_by_id, " +
            "   effective_period_id, " +
            "   revalued_asset_id " +
            " FROM public.asset_revaluation ar " +
            " LEFT JOIN depreciation_period dp ON ar.effective_period_id = dp.id " +
            " WHERE revalued_asset_id = :revaluedAssetId " +
            " AND dp.start_date <= CAST ( :depreciationPeriodStartDate AS DATE)"
    )
    Optional<List<AssetRevaluation>> findAssetRevaluation(@Param("revaluedAssetId")Long revaluedAssetId, @Param("depreciationPeriodStartDate") LocalDate depreciationPeriodStartDate);
}
