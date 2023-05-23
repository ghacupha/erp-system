package io.github.erp.repository;

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
        value = "select distinct paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels left join fetch paymentInvoice.deliveryNotes left join fetch paymentInvoice.jobSheets left join fetch paymentInvoice.businessDocuments",
        countQuery = "select count(distinct paymentInvoice) from PaymentInvoice paymentInvoice"
    )
    Page<PaymentInvoice> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels left join fetch paymentInvoice.deliveryNotes left join fetch paymentInvoice.jobSheets left join fetch paymentInvoice.businessDocuments"
    )
    List<PaymentInvoice> findAllWithEagerRelationships();

    @Query(
        "select paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels left join fetch paymentInvoice.deliveryNotes left join fetch paymentInvoice.jobSheets left join fetch paymentInvoice.businessDocuments where paymentInvoice.id =:id"
    )
    Optional<PaymentInvoice> findOneWithEagerRelationships(@Param("id") Long id);
}
