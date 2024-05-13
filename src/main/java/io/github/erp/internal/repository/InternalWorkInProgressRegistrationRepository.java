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

import io.github.erp.domain.WorkInProgressRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the WorkInProgressRegistration entity.
 */
@Repository
public interface InternalWorkInProgressRegistrationRepository
    extends JpaRepository<WorkInProgressRegistration, Long>, JpaSpecificationExecutor<WorkInProgressRegistration> {
    @Query(
        value = "select distinct workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.businessDocuments left join fetch workInProgressRegistration.assetAccessories left join fetch workInProgressRegistration.assetWarranties",
        countQuery = "select count(distinct workInProgressRegistration) from WorkInProgressRegistration workInProgressRegistration"
    )
    Page<WorkInProgressRegistration> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.businessDocuments left join fetch workInProgressRegistration.assetAccessories left join fetch workInProgressRegistration.assetWarranties"
    )
    List<WorkInProgressRegistration> findAllWithEagerRelationships();

    @Query(
        "select workInProgressRegistration from WorkInProgressRegistration workInProgressRegistration left join fetch workInProgressRegistration.placeholders left join fetch workInProgressRegistration.businessDocuments left join fetch workInProgressRegistration.assetAccessories left join fetch workInProgressRegistration.assetWarranties where workInProgressRegistration.id =:id"
    )
    Optional<WorkInProgressRegistration> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        nativeQuery = true,
        value = "SELECT CAST(sequence_number AS BIGINT) FROM public.work_in_progress_registration",
        countQuery = "SELECT CAST(sequence_number AS BIGINT) FROM public.work_in_progress_registration"
    )
    List<Long> findAllIds();
}
