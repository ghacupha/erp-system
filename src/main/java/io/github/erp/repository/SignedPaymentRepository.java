package io.github.erp.repository;

import io.github.erp.domain.SignedPayment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SignedPayment entity.
 */
@Repository
public interface SignedPaymentRepository extends JpaRepository<SignedPayment, Long>, JpaSpecificationExecutor<SignedPayment> {
    @Query(
        value = "select distinct signedPayment from SignedPayment signedPayment left join fetch signedPayment.paymentLabels left join fetch signedPayment.placeholders",
        countQuery = "select count(distinct signedPayment) from SignedPayment signedPayment"
    )
    Page<SignedPayment> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct signedPayment from SignedPayment signedPayment left join fetch signedPayment.paymentLabels left join fetch signedPayment.placeholders"
    )
    List<SignedPayment> findAllWithEagerRelationships();

    @Query(
        "select signedPayment from SignedPayment signedPayment left join fetch signedPayment.paymentLabels left join fetch signedPayment.placeholders where signedPayment.id =:id"
    )
    Optional<SignedPayment> findOneWithEagerRelationships(@Param("id") Long id);
}
