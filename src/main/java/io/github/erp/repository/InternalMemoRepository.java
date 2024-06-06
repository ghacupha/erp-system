package io.github.erp.repository;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.InternalMemo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InternalMemo entity.
 */
@Repository
public interface InternalMemoRepository extends JpaRepository<InternalMemo, Long>, JpaSpecificationExecutor<InternalMemo> {
    @Query(
        value = "select distinct internalMemo from InternalMemo internalMemo left join fetch internalMemo.preparedBies left join fetch internalMemo.reviewedBies left join fetch internalMemo.approvedBies left join fetch internalMemo.memoDocuments left join fetch internalMemo.placeholders",
        countQuery = "select count(distinct internalMemo) from InternalMemo internalMemo"
    )
    Page<InternalMemo> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct internalMemo from InternalMemo internalMemo left join fetch internalMemo.preparedBies left join fetch internalMemo.reviewedBies left join fetch internalMemo.approvedBies left join fetch internalMemo.memoDocuments left join fetch internalMemo.placeholders"
    )
    List<InternalMemo> findAllWithEagerRelationships();

    @Query(
        "select internalMemo from InternalMemo internalMemo left join fetch internalMemo.preparedBies left join fetch internalMemo.reviewedBies left join fetch internalMemo.approvedBies left join fetch internalMemo.memoDocuments left join fetch internalMemo.placeholders where internalMemo.id =:id"
    )
    Optional<InternalMemo> findOneWithEagerRelationships(@Param("id") Long id);
}
