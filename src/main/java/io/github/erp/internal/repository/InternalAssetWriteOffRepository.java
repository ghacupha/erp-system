package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
