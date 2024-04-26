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
