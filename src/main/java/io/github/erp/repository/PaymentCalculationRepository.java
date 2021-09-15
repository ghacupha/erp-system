package io.github.erp.repository;

import io.github.erp.domain.PaymentCalculation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentCalculation entity.
 */
@Repository
public interface PaymentCalculationRepository
    extends JpaRepository<PaymentCalculation, Long>, JpaSpecificationExecutor<PaymentCalculation> {
    @Query(
        value = "select distinct paymentCalculation from PaymentCalculation paymentCalculation left join fetch paymentCalculation.placeholders",
        countQuery = "select count(distinct paymentCalculation) from PaymentCalculation paymentCalculation"
    )
    Page<PaymentCalculation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct paymentCalculation from PaymentCalculation paymentCalculation left join fetch paymentCalculation.placeholders")
    List<PaymentCalculation> findAllWithEagerRelationships();

    @Query(
        "select paymentCalculation from PaymentCalculation paymentCalculation left join fetch paymentCalculation.placeholders where paymentCalculation.id =:id"
    )
    Optional<PaymentCalculation> findOneWithEagerRelationships(@Param("id") Long id);
}
