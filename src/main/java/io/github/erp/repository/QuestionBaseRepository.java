package io.github.erp.repository;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

import io.github.erp.domain.QuestionBase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionBase entity.
 */
@Repository
public interface QuestionBaseRepository extends JpaRepository<QuestionBase, Long>, JpaSpecificationExecutor<QuestionBase> {
    @Query(
        value = "select distinct questionBase from QuestionBase questionBase left join fetch questionBase.parameters left join fetch questionBase.placeholderItems",
        countQuery = "select count(distinct questionBase) from QuestionBase questionBase"
    )
    Page<QuestionBase> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct questionBase from QuestionBase questionBase left join fetch questionBase.parameters left join fetch questionBase.placeholderItems"
    )
    List<QuestionBase> findAllWithEagerRelationships();

    @Query(
        "select questionBase from QuestionBase questionBase left join fetch questionBase.parameters left join fetch questionBase.placeholderItems where questionBase.id =:id"
    )
    Optional<QuestionBase> findOneWithEagerRelationships(@Param("id") Long id);
}
