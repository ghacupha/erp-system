package io.github.erp.repository;

import io.github.erp.domain.PaymentRequisition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentRequisition entity.
 */
@Repository
public interface PaymentRequisitionRepository
    extends JpaRepository<PaymentRequisition, Long>, JpaSpecificationExecutor<PaymentRequisition> {
    @Query(
        value = "select distinct paymentRequisition from PaymentRequisition paymentRequisition left join fetch paymentRequisition.paymentLabels left join fetch paymentRequisition.placeholders",
        countQuery = "select count(distinct paymentRequisition) from PaymentRequisition paymentRequisition"
    )
    Page<PaymentRequisition> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct paymentRequisition from PaymentRequisition paymentRequisition left join fetch paymentRequisition.paymentLabels left join fetch paymentRequisition.placeholders"
    )
    List<PaymentRequisition> findAllWithEagerRelationships();

    @Query(
        "select paymentRequisition from PaymentRequisition paymentRequisition left join fetch paymentRequisition.paymentLabels left join fetch paymentRequisition.placeholders where paymentRequisition.id =:id"
    )
    Optional<PaymentRequisition> findOneWithEagerRelationships(@Param("id") Long id);
}
