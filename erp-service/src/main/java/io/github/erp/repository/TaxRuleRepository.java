package io.github.erp.repository;

import io.github.erp.domain.TaxRule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TaxRule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long>, JpaSpecificationExecutor<TaxRule> {
}
