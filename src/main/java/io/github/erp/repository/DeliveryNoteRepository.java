package io.github.erp.repository;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.domain.DeliveryNote;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeliveryNote entity.
 */
@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long>, JpaSpecificationExecutor<DeliveryNote> {
    @Query(
        value = "select distinct deliveryNote from DeliveryNote deliveryNote left join fetch deliveryNote.placeholders left join fetch deliveryNote.deliveryStamps left join fetch deliveryNote.signatories left join fetch deliveryNote.otherPurchaseOrders left join fetch deliveryNote.businessDocuments",
        countQuery = "select count(distinct deliveryNote) from DeliveryNote deliveryNote"
    )
    Page<DeliveryNote> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct deliveryNote from DeliveryNote deliveryNote left join fetch deliveryNote.placeholders left join fetch deliveryNote.deliveryStamps left join fetch deliveryNote.signatories left join fetch deliveryNote.otherPurchaseOrders left join fetch deliveryNote.businessDocuments"
    )
    List<DeliveryNote> findAllWithEagerRelationships();

    @Query(
        "select deliveryNote from DeliveryNote deliveryNote left join fetch deliveryNote.placeholders left join fetch deliveryNote.deliveryStamps left join fetch deliveryNote.signatories left join fetch deliveryNote.otherPurchaseOrders left join fetch deliveryNote.businessDocuments where deliveryNote.id =:id"
    )
    Optional<DeliveryNote> findOneWithEagerRelationships(@Param("id") Long id);
}
