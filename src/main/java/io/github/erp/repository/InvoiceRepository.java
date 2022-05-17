package io.github.erp.repository;

import io.github.erp.domain.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    @Query(
        value = "select distinct invoice from Invoice invoice left join fetch invoice.paymentLabels left join fetch invoice.placeholders",
        countQuery = "select count(distinct invoice) from Invoice invoice"
    )
    Page<Invoice> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct invoice from Invoice invoice left join fetch invoice.paymentLabels left join fetch invoice.placeholders")
    List<Invoice> findAllWithEagerRelationships();

    @Query(
        "select invoice from Invoice invoice left join fetch invoice.paymentLabels left join fetch invoice.placeholders where invoice.id =:id"
    )
    Optional<Invoice> findOneWithEagerRelationships(@Param("id") Long id);
}
