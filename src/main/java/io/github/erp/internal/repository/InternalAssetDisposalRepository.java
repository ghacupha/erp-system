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
import io.github.erp.domain.AssetDisposalInternal;
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
 * Spring Data SQL repository for the AssetDisposal entity.
 */
@Repository
public interface InternalAssetDisposalRepository extends JpaRepository<AssetDisposal, Long>, JpaSpecificationExecutor<AssetDisposal> {
    @Query(
        value = "select distinct assetDisposal from AssetDisposal assetDisposal left join fetch assetDisposal.placeholders",
        countQuery = "select count(distinct assetDisposal) from AssetDisposal assetDisposal"
    )
    Page<AssetDisposal> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct assetDisposal from AssetDisposal assetDisposal left join fetch assetDisposal.placeholders")
    List<AssetDisposal> findAllWithEagerRelationships();

    @Query("select assetDisposal from AssetDisposal assetDisposal left join fetch assetDisposal.placeholders where assetDisposal.id =:id")
    Optional<AssetDisposal> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "   ad.id," +
            "   asset_disposal_reference," +
            "   description, asset_cost," +
            "   historical_cost," +
            "   accrued_depreciation," +
            "   net_book_value," +
            "   decommissioning_date," +
            "   disposal_date," +
            "   dormant," +
            "   ad.created_by_id," +
            "   ad.modified_by_id," +
            "   ad.last_accessed_by_id," +
            "   effective_period_id," +
            "   asset_disposed_id " +
            "FROM public.asset_disposal ad " +
            "LEFT JOIN depreciation_period dp ON ad.effective_period_id = dp.id " +
            "WHERE asset_disposed_id = :disposedAssetId " +
            "AND dp.start_date <= CAST ( :depreciationPeriodStartDate AS DATE)",
        countQuery = "" +
            "SELECT " +
            "   ad.id," +
            "   asset_disposal_reference," +
            "   description, asset_cost," +
            "   historical_cost," +
            "   accrued_depreciation," +
            "   net_book_value," +
            "   decommissioning_date," +
            "   disposal_date," +
            "   dormant," +
            "   ad.created_by_id," +
            "   ad.modified_by_id," +
            "   ad.last_accessed_by_id," +
            "   effective_period_id," +
            "   asset_disposed_id " +
            "FROM public.asset_disposal ad " +
            "LEFT JOIN depreciation_period dp ON ad.effective_period_id = dp.id " +
            "WHERE asset_disposed_id = :disposedAssetId " +
            "AND dp.start_date <= CAST ( :depreciationPeriodStartDate AS DATE)"
    )
    Optional<List<AssetDisposal>> findAssetDisposal(@Param("disposedAssetId")Long disposedAssetId, @Param("depreciationPeriodStartDate")LocalDate depreciationPeriodStartDate);
}
