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
