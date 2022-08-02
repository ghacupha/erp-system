package io.github.erp.repository;

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
