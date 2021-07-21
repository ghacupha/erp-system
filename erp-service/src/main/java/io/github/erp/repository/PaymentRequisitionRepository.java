package io.github.erp.repository;

import io.github.erp.domain.PaymentRequisition;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PaymentRequisition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRequisitionRepository extends JpaRepository<PaymentRequisition, Long>, JpaSpecificationExecutor<PaymentRequisition> {
}
