package io.github.erp.repository;

/*-
 * Erp System - Mark II No 23 (Baruch Series)
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

import io.github.erp.domain.ProcessStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProcessStatus entity.
 */
@Repository
public interface ProcessStatusRepository extends JpaRepository<ProcessStatus, Long>, JpaSpecificationExecutor<ProcessStatus> {
    @Query(
        value = "select distinct processStatus from ProcessStatus processStatus left join fetch processStatus.placeholders left join fetch processStatus.parameters",
        countQuery = "select count(distinct processStatus) from ProcessStatus processStatus"
    )
    Page<ProcessStatus> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct processStatus from ProcessStatus processStatus left join fetch processStatus.placeholders left join fetch processStatus.parameters"
    )
    List<ProcessStatus> findAllWithEagerRelationships();

    @Query(
        "select processStatus from ProcessStatus processStatus left join fetch processStatus.placeholders left join fetch processStatus.parameters where processStatus.id =:id"
    )
    Optional<ProcessStatus> findOneWithEagerRelationships(@Param("id") Long id);
}
