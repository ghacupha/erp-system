package io.github.erp.repository;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.domain.LeaseModelMetadata;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LeaseModelMetadata entity.
 */
@Repository
public interface LeaseModelMetadataRepository
    extends JpaRepository<LeaseModelMetadata, Long>, JpaSpecificationExecutor<LeaseModelMetadata> {
    @Query(
        value = "select distinct leaseModelMetadata from LeaseModelMetadata leaseModelMetadata left join fetch leaseModelMetadata.placeholders left join fetch leaseModelMetadata.leaseMappings",
        countQuery = "select count(distinct leaseModelMetadata) from LeaseModelMetadata leaseModelMetadata"
    )
    Page<LeaseModelMetadata> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct leaseModelMetadata from LeaseModelMetadata leaseModelMetadata left join fetch leaseModelMetadata.placeholders left join fetch leaseModelMetadata.leaseMappings"
    )
    List<LeaseModelMetadata> findAllWithEagerRelationships();

    @Query(
        "select leaseModelMetadata from LeaseModelMetadata leaseModelMetadata left join fetch leaseModelMetadata.placeholders left join fetch leaseModelMetadata.leaseMappings where leaseModelMetadata.id =:id"
    )
    Optional<LeaseModelMetadata> findOneWithEagerRelationships(@Param("id") Long id);
}
