package io.github.erp.repository;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.4-SNAPSHOT
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
import io.github.erp.domain.WorkProjectRegister;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkProjectRegister entity.
 */
@Repository
public interface WorkProjectRegisterRepository
    extends JpaRepository<WorkProjectRegister, Long>, JpaSpecificationExecutor<WorkProjectRegister> {
    @Query(
        value = "select distinct workProjectRegister from WorkProjectRegister workProjectRegister left join fetch workProjectRegister.dealers left join fetch workProjectRegister.placeholders",
        countQuery = "select count(distinct workProjectRegister) from WorkProjectRegister workProjectRegister"
    )
    Page<WorkProjectRegister> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct workProjectRegister from WorkProjectRegister workProjectRegister left join fetch workProjectRegister.dealers left join fetch workProjectRegister.placeholders"
    )
    List<WorkProjectRegister> findAllWithEagerRelationships();

    @Query(
        "select workProjectRegister from WorkProjectRegister workProjectRegister left join fetch workProjectRegister.dealers left join fetch workProjectRegister.placeholders where workProjectRegister.id =:id"
    )
    Optional<WorkProjectRegister> findOneWithEagerRelationships(@Param("id") Long id);
}
