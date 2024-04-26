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
import io.github.erp.domain.AssetWriteOff;
import io.github.erp.domain.AssetWriteOffInternal;
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
 * Spring Data SQL repository for the AssetWriteOff entity.
 */
@Repository
public interface InternalAssetWriteOffRepository extends JpaRepository<AssetWriteOff, Long>, JpaSpecificationExecutor<AssetWriteOff> {
    @Query(
        value = "select distinct assetWriteOff from AssetWriteOff assetWriteOff left join fetch assetWriteOff.placeholders",
        countQuery = "select count(distinct assetWriteOff) from AssetWriteOff assetWriteOff"
    )
    Page<AssetWriteOff> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct assetWriteOff from AssetWriteOff assetWriteOff left join fetch assetWriteOff.placeholders")
    List<AssetWriteOff> findAllWithEagerRelationships();

    @Query("select assetWriteOff from AssetWriteOff assetWriteOff left join fetch assetWriteOff.placeholders where assetWriteOff.id =:id")
    Optional<AssetWriteOff> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(nativeQuery = true,
    value = "" +
        "SELECT " +
        " aw.id," +
        " description," +
        " aw.write_off_amount AS writeOffAmount," +
        " aw.write_off_date AS writeOffDate," +
        " dp.start_date AS effectivePeriodStartDate," +
        " dp.end_date AS effectivePeriodEndDate," +
        " ar.asset_number " +
        " FROM asset_write_off aw " +
        " LEFT JOIN depreciation_period dp ON aw.effective_period_id = dp.id " +
        " LEFT JOIN asset_registration ar ON AW.asset_written_off_id = ar.id " +
        " WHERE " +
        "  aw.asset_written_off_id= :assetWrittenOffId " +
        "  AND dp.start_date <= CAST ( :depreciationPeriodStartDate AS DATE)")
    Optional<List<AssetWriteOffInternal>> findAssetWriteOff(@Param("assetWrittenOffId") Long assetWrittenOffId, @Param("depreciationPeriodStartDate")LocalDate depreciationPeriodStartDate);
}
