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
        value = "select distinct paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels",
        countQuery = "select count(distinct paymentInvoice) from PaymentInvoice paymentInvoice"
    )
    Page<PaymentInvoice> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels"
    )
    List<PaymentInvoice> findAllWithEagerRelationships();

    @Query(
        "select paymentInvoice from PaymentInvoice paymentInvoice left join fetch paymentInvoice.purchaseOrders left join fetch paymentInvoice.placeholders left join fetch paymentInvoice.paymentLabels where paymentInvoice.id =:id"
    )
    Optional<PaymentInvoice> findOneWithEagerRelationships(@Param("id") Long id);
}
