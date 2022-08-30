package io.github.erp.repository;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.0.8-SNAPSHOT
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
import io.github.erp.domain.PaymentInvoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentInvoice entity.
 */
@Repository
public interface PaymentInvoiceRepository extends JpaRepository<PaymentInvoice, Long>, JpaSpecificationExecutor<PaymentInvoice> {
    @Query(
        value = "select distinct paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels left join fetch paymentInvoice.deliveryNotes left join fetch paymentInvoice.jobSheets",
        countQuery = "select count(distinct paymentInvoice) from PaymentInvoice paymentInvoice"
    )
    Page<PaymentInvoice> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels left join fetch paymentInvoice.deliveryNotes left join fetch paymentInvoice.jobSheets"
    )
    List<PaymentInvoice> findAllWithEagerRelationships();

    @Query(
        "select paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels left join fetch paymentInvoice.deliveryNotes left join fetch paymentInvoice.jobSheets where paymentInvoice.id =:id"
    )
    Optional<PaymentInvoice> findOneWithEagerRelationships(@Param("id") Long id);
}
