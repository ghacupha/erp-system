package io.github.erp.repository;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.BusinessStamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusinessStamp entity.
 */
@Repository
public interface BusinessStampRepository extends JpaRepository<BusinessStamp, Long>, JpaSpecificationExecutor<BusinessStamp> {
    @Query(
        value = "select distinct businessStamp from BusinessStamp businessStamp left join fetch businessStamp.placeholders",
        countQuery = "select count(distinct businessStamp) from BusinessStamp businessStamp"
    )
    Page<BusinessStamp> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct businessStamp from BusinessStamp businessStamp left join fetch businessStamp.placeholders")
    List<BusinessStamp> findAllWithEagerRelationships();

    @Query("select businessStamp from BusinessStamp businessStamp left join fetch businessStamp.placeholders where businessStamp.id =:id")
    Optional<BusinessStamp> findOneWithEagerRelationships(@Param("id") Long id);
}
