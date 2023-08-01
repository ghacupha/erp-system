package io.github.erp.repository;

import io.github.erp.domain.DepreciationPeriod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DepreciationPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepreciationPeriodRepository
    extends JpaRepository<DepreciationPeriod, Long>, JpaSpecificationExecutor<DepreciationPeriod> {}
