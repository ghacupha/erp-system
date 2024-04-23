package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.domain.AssetGeneralAdjustment;
import io.github.erp.domain.AssetRevaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the AssetGeneralAdjustment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalAssetGeneralAdjustmentRepository
    extends JpaRepository<AssetGeneralAdjustment, Long>, JpaSpecificationExecutor<AssetGeneralAdjustment> {

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "   ar.id, " +
            "   description, " +
            "   devaluation_amount, " +
            "   adjustment_date, " +
            "   time_of_creation, " +
            "   adjustment_reference_id, " +
            "   effective_period_id, " +
            "   asset_registration_id, " +
            "   ar.created_by_id, " +
            "   ar.last_modified_by_id, " +
            "   last_accessed_by_id, " +
            "   placeholder_id " +
            "FROM public.asset_general_adjustment  ar" +
            " LEFT JOIN depreciation_period dp ON ar.effective_period_id = dp.id " +
            " WHERE asset_registration_id = :revaluedAssetId " +
            " AND dp.start_date <= CAST ( :depreciationPeriodStartDate AS DATE)",
        countQuery = "" +
            "SELECT " +
            "   ar.id, " +
            "   description, " +
            "   devaluation_amount, " +
            "   adjustment_date, " +
            "   time_of_creation, " +
            "   adjustment_reference_id, " +
            "   effective_period_id, " +
            "   asset_registration_id, " +
            "   ar.created_by_id, " +
            "   ar.last_modified_by_id, " +
            "   last_accessed_by_id, " +
            "   placeholder_id " +
            "FROM public.asset_general_adjustment  ar" +
            " LEFT JOIN depreciation_period dp ON ar.effective_period_id = dp.id " +
            " WHERE asset_registration_id = :revaluedAssetId " +
            " AND dp.start_date <= CAST ( :depreciationPeriodStartDate AS DATE)"
    )
    Optional<List<AssetGeneralAdjustment>> findAssetGeneralAdjustment(@Param("revaluedAssetId")Long revaluedAssetId, @Param("depreciationPeriodStartDate") LocalDate depreciationPeriodStartDate);
}
