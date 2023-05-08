package io.github.erp.repository;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.1
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
import io.github.erp.domain.AgencyNotice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AgencyNotice entity.
 */
@Repository
public interface AgencyNoticeRepository extends JpaRepository<AgencyNotice, Long>, JpaSpecificationExecutor<AgencyNotice> {
    @Query(
        value = "select distinct agencyNotice from AgencyNotice agencyNotice left join fetch agencyNotice.correspondents left join fetch agencyNotice.placeholders left join fetch agencyNotice.businessDocuments",
        countQuery = "select count(distinct agencyNotice) from AgencyNotice agencyNotice"
    )
    Page<AgencyNotice> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct agencyNotice from AgencyNotice agencyNotice left join fetch agencyNotice.correspondents left join fetch agencyNotice.placeholders left join fetch agencyNotice.businessDocuments"
    )
    List<AgencyNotice> findAllWithEagerRelationships();

    @Query(
        "select agencyNotice from AgencyNotice agencyNotice left join fetch agencyNotice.correspondents left join fetch agencyNotice.placeholders left join fetch agencyNotice.businessDocuments where agencyNotice.id =:id"
    )
    Optional<AgencyNotice> findOneWithEagerRelationships(@Param("id") Long id);
}
