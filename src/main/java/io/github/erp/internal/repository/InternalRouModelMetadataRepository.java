package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.RouModelMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data SQL repository for the RouModelMetadata entity.
 */
@Repository
public interface InternalRouModelMetadataRepository extends JpaRepository<RouModelMetadata, Long>, JpaSpecificationExecutor<RouModelMetadata> {
    @Query(
        value = "select distinct rouModelMetadata from RouModelMetadata rouModelMetadata left join fetch rouModelMetadata.documentAttachments",
        countQuery = "select count(distinct rouModelMetadata) from RouModelMetadata rouModelMetadata"
    )
    Page<RouModelMetadata> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct rouModelMetadata from RouModelMetadata rouModelMetadata left join fetch rouModelMetadata.documentAttachments")
    List<RouModelMetadata> findAllWithEagerRelationships();

    @Query(
        "select rouModelMetadata from RouModelMetadata rouModelMetadata left join fetch rouModelMetadata.documentAttachments where rouModelMetadata.id =:id"
    )
    Optional<RouModelMetadata> findOneWithEagerRelationships(@Param("id") Long id);

    /**
     * This query returns a conditional list of items for calculation of depreciation upon
     * first checking if the items have been flagged as fully amortised and also if the items
     * have been decommissioned. We also check if the instance has depreciation entries to the
     * extent that it is already fully depreciated or the amount calculated comes to within a
     * threshold of currency units specified in the param, where currency is represented in BigDecimal type
     * @return List of items qualified for depreciation
     */
    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT m.*  " +
            "  FROM rou_model_metadata m   " +
            "  LEFT JOIN rou_depreciation_entry e ON m.id = e.rou_metadata_id  " +
            "  GROUP BY m.id   " +
            "  HAVING (m.has_been_decommissioned = 'false' OR m.has_been_decommissioned IS NULL)  " +
            "     AND (m.has_been_fully_amortised = 'false' OR m.has_been_fully_amortised IS NULL)   " +
            "     AND (m.lease_amount - COALESCE(SUM(e.depreciation_amount), 0) > 10)",
        countQuery = "" +
            "SELECT m.*  " +
            "  FROM rou_model_metadata m   " +
            "  LEFT JOIN rou_depreciation_entry e ON m.id = e.rou_metadata_id  " +
            "  GROUP BY m.id   " +
            "  HAVING (m.has_been_decommissioned = 'false' OR m.has_been_decommissioned IS NULL)  " +
            "     AND (m.has_been_fully_amortised = 'false' OR m.has_been_fully_amortised IS NULL)   " +
            "     AND (m.lease_amount - COALESCE(SUM(e.depreciation_amount), 0) > :thresholdAmount)"
    )
    Optional<List<RouModelMetadata>> getDepreciationAdjacentMetadataItems(@Param("thresholdAmount")BigDecimal thresholdAmount);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT *  " +
            "FROM public.rou_model_metadata  " +
            "WHERE batch_job_identifier = :batch_job_identifier"
    )
    Optional<List<RouModelMetadata>> getProcessedItems(@Param("batch_job_identifier")UUID batchJobIdentifier);
}
