package io.github.erp.repository;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

import io.github.erp.domain.ApplicationUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationUser entity.
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long>, JpaSpecificationExecutor<ApplicationUser> {
    @Query(
        value = "select distinct applicationUser from ApplicationUser applicationUser left join fetch applicationUser.userProperties left join fetch applicationUser.placeholders",
        countQuery = "select count(distinct applicationUser) from ApplicationUser applicationUser"
    )
    Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct applicationUser from ApplicationUser applicationUser left join fetch applicationUser.userProperties left join fetch applicationUser.placeholders"
    )
    List<ApplicationUser> findAllWithEagerRelationships();

    @Query(
        "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.userProperties left join fetch applicationUser.placeholders where applicationUser.id =:id"
    )
    Optional<ApplicationUser> findOneWithEagerRelationships(@Param("id") Long id);
}
