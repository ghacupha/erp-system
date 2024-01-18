package io.github.erp.repository;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.domain.CreditNote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CreditNote entity.
 */
@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNote, Long>, JpaSpecificationExecutor<CreditNote> {
    @Query(
        value = "select distinct creditNote from CreditNote creditNote left join fetch creditNote.purchaseOrders left join fetch creditNote.invoices left join fetch creditNote.paymentLabels left join fetch creditNote.placeholders",
        countQuery = "select count(distinct creditNote) from CreditNote creditNote"
    )
    Page<CreditNote> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct creditNote from CreditNote creditNote left join fetch creditNote.purchaseOrders left join fetch creditNote.invoices left join fetch creditNote.paymentLabels left join fetch creditNote.placeholders"
    )
    List<CreditNote> findAllWithEagerRelationships();

    @Query(
        "select creditNote from CreditNote creditNote left join fetch creditNote.purchaseOrders left join fetch creditNote.invoices left join fetch creditNote.paymentLabels left join fetch creditNote.placeholders where creditNote.id =:id"
    )
    Optional<CreditNote> findOneWithEagerRelationships(@Param("id") Long id);
}
