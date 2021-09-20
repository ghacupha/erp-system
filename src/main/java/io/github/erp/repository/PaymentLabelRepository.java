package io.github.erp.repository;

import io.github.erp.domain.PaymentLabel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentLabel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentLabelRepository extends JpaRepository<PaymentLabel, Long>, JpaSpecificationExecutor<PaymentLabel> {}
