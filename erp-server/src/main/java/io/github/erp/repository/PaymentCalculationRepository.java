package io.github.erp.repository;

import io.github.erp.domain.PaymentCalculation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentCalculation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentCalculationRepository
    extends JpaRepository<PaymentCalculation, Long>, JpaSpecificationExecutor<PaymentCalculation> {}
