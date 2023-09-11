package io.github.erp.repository;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.CustomerIDDocumentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CustomerIDDocumentType entity.
 */
@Repository
public interface CustomerIDDocumentTypeRepository
    extends JpaRepository<CustomerIDDocumentType, Long>, JpaSpecificationExecutor<CustomerIDDocumentType> {
    @Query(
        value = "select distinct customerIDDocumentType from CustomerIDDocumentType customerIDDocumentType left join fetch customerIDDocumentType.placeholders",
        countQuery = "select count(distinct customerIDDocumentType) from CustomerIDDocumentType customerIDDocumentType"
    )
    Page<CustomerIDDocumentType> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct customerIDDocumentType from CustomerIDDocumentType customerIDDocumentType left join fetch customerIDDocumentType.placeholders"
    )
    List<CustomerIDDocumentType> findAllWithEagerRelationships();

    @Query(
        "select customerIDDocumentType from CustomerIDDocumentType customerIDDocumentType left join fetch customerIDDocumentType.placeholders where customerIDDocumentType.id =:id"
    )
    Optional<CustomerIDDocumentType> findOneWithEagerRelationships(@Param("id") Long id);
}
