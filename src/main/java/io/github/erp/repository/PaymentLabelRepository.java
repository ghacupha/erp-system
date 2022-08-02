package io.github.erp.repository;

import io.github.erp.domain.PaymentLabel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentLabel entity.
 */
@Repository
public interface PaymentLabelRepository extends JpaRepository<PaymentLabel, Long>, JpaSpecificationExecutor<PaymentLabel> {
    @Query(
        value = "select distinct paymentLabel from PaymentLabel paymentLabel left join fetch paymentLabel.placeholders",
        countQuery = "select count(distinct paymentLabel) from PaymentLabel paymentLabel"
    )
    Page<PaymentLabel> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct paymentLabel from PaymentLabel paymentLabel left join fetch paymentLabel.placeholders")
    List<PaymentLabel> findAllWithEagerRelationships();

    @Query("select paymentLabel from PaymentLabel paymentLabel left join fetch paymentLabel.placeholders where paymentLabel.id =:id")
    Optional<PaymentLabel> findOneWithEagerRelationships(@Param("id") Long id);
}
