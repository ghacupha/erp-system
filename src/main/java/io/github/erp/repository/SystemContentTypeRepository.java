package io.github.erp.repository;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.6-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.SystemContentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemContentType entity.
 */
@Repository
public interface SystemContentTypeRepository extends JpaRepository<SystemContentType, Long>, JpaSpecificationExecutor<SystemContentType> {
    @Query(
        value = "select distinct systemContentType from SystemContentType systemContentType left join fetch systemContentType.placeholders left join fetch systemContentType.sysMaps",
        countQuery = "select count(distinct systemContentType) from SystemContentType systemContentType"
    )
    Page<SystemContentType> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct systemContentType from SystemContentType systemContentType left join fetch systemContentType.placeholders left join fetch systemContentType.sysMaps"
    )
    List<SystemContentType> findAllWithEagerRelationships();

    @Query(
        "select systemContentType from SystemContentType systemContentType left join fetch systemContentType.placeholders left join fetch systemContentType.sysMaps where systemContentType.id =:id"
    )
    Optional<SystemContentType> findOneWithEagerRelationships(@Param("id") Long id);
}
