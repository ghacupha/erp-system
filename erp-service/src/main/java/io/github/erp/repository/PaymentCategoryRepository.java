package io.github.erp.repository;

import io.github.erp.domain.PaymentCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PaymentCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentCategoryRepository extends JpaRepository<PaymentCategory, Long>, JpaSpecificationExecutor<PaymentCategory> {
}
