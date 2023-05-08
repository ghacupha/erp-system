package io.github.erp.repository;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.0-SNAPSHOT
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

import io.github.erp.domain.InstitutionCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InstitutionCode entity.
 */
@Repository
public interface InstitutionCodeRepository extends JpaRepository<InstitutionCode, Long>, JpaSpecificationExecutor<InstitutionCode> {
    @Query(
        value = "select distinct institutionCode from InstitutionCode institutionCode left join fetch institutionCode.placeholders",
        countQuery = "select count(distinct institutionCode) from InstitutionCode institutionCode"
    )
    Page<InstitutionCode> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct institutionCode from InstitutionCode institutionCode left join fetch institutionCode.placeholders")
    List<InstitutionCode> findAllWithEagerRelationships();

    @Query(
        "select institutionCode from InstitutionCode institutionCode left join fetch institutionCode.placeholders where institutionCode.id =:id"
    )
    Optional<InstitutionCode> findOneWithEagerRelationships(@Param("id") Long id);
}
