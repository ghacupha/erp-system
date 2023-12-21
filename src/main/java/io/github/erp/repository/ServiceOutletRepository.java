package io.github.erp.repository;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.ServiceOutlet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ServiceOutlet entity.
 */
@Repository
public interface ServiceOutletRepository extends JpaRepository<ServiceOutlet, Long>, JpaSpecificationExecutor<ServiceOutlet> {
    @Query(
        value = "select distinct serviceOutlet from ServiceOutlet serviceOutlet left join fetch serviceOutlet.placeholders",
        countQuery = "select count(distinct serviceOutlet) from ServiceOutlet serviceOutlet"
    )
    Page<ServiceOutlet> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct serviceOutlet from ServiceOutlet serviceOutlet left join fetch serviceOutlet.placeholders")
    List<ServiceOutlet> findAllWithEagerRelationships();

    @Query("select serviceOutlet from ServiceOutlet serviceOutlet left join fetch serviceOutlet.placeholders where serviceOutlet.id =:id")
    Optional<ServiceOutlet> findOneWithEagerRelationships(@Param("id") Long id);
}
