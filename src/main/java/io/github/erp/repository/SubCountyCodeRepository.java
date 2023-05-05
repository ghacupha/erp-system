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
import io.github.erp.domain.SubCountyCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubCountyCode entity.
 */
@Repository
public interface SubCountyCodeRepository extends JpaRepository<SubCountyCode, Long>, JpaSpecificationExecutor<SubCountyCode> {
    @Query(
        value = "select distinct subCountyCode from SubCountyCode subCountyCode left join fetch subCountyCode.placeholders",
        countQuery = "select count(distinct subCountyCode) from SubCountyCode subCountyCode"
    )
    Page<SubCountyCode> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct subCountyCode from SubCountyCode subCountyCode left join fetch subCountyCode.placeholders")
    List<SubCountyCode> findAllWithEagerRelationships();

    @Query("select subCountyCode from SubCountyCode subCountyCode left join fetch subCountyCode.placeholders where subCountyCode.id =:id")
    Optional<SubCountyCode> findOneWithEagerRelationships(@Param("id") Long id);
}
