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
import org.springframework.security.config.web.servlet.PortMapperDsl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT * " +
            "FROM public.rou_model_metadata " +
            "WHERE (has_been_decommissioned = 'false' OR has_been_decommissioned IS NULL) " +
            "  AND (has_been_fully_amortised = 'false' OR has_been_fully_amortised IS NULL)",
        countQuery = "" +
            "SELECT * " +
            "FROM public.rou_model_metadata " +
            "WHERE (has_been_decommissioned = 'false' OR has_been_decommissioned IS NULL) " +
            "  AND (has_been_fully_amortised = 'false' OR has_been_fully_amortised IS NULL)"
    )
    Optional<List<RouModelMetadata>> getDepreciationAdjacentMetadataItems();
}
