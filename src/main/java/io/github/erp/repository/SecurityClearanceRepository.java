package io.github.erp.repository;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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

import io.github.erp.domain.SecurityClearance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SecurityClearance entity.
 */
@Repository
public interface SecurityClearanceRepository extends JpaRepository<SecurityClearance, Long>, JpaSpecificationExecutor<SecurityClearance> {
    @Query(
        value = "select distinct securityClearance from SecurityClearance securityClearance left join fetch securityClearance.grantedClearances left join fetch securityClearance.placeholders left join fetch securityClearance.systemParameters",
        countQuery = "select count(distinct securityClearance) from SecurityClearance securityClearance"
    )
    Page<SecurityClearance> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct securityClearance from SecurityClearance securityClearance left join fetch securityClearance.grantedClearances left join fetch securityClearance.placeholders left join fetch securityClearance.systemParameters"
    )
    List<SecurityClearance> findAllWithEagerRelationships();

    @Query(
        "select securityClearance from SecurityClearance securityClearance left join fetch securityClearance.grantedClearances left join fetch securityClearance.placeholders left join fetch securityClearance.systemParameters where securityClearance.id =:id"
    )
    Optional<SecurityClearance> findOneWithEagerRelationships(@Param("id") Long id);
}
