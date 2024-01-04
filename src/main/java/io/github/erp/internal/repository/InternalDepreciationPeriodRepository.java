package io.github.erp.internal.repository;

import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.repository.DepreciationPeriodRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * This is now necessary because we have lazily initialized datasets from the base
 * class which would cause problems in the search repo
 */
public interface InternalDepreciationPeriodRepository extends
    DepreciationPeriodRepository,
    JpaRepository<DepreciationPeriod, Long>,
    JpaSpecificationExecutor<DepreciationPeriod> {

    @EntityGraph(
        attributePaths = {
            "previousPeriod",
            "fiscalMonth",
            "createdBy",
            "lastModifiedBy",
            "fiscalMonth.placeholders",
            "fiscalMonth.universallyUniqueMappings",
            "createdBy.userProperties",
            "createdBy.placeholders",
            "lastModifiedBy.userProperties",
            "lastModifiedBy.placeholders",
        })
    Optional<DepreciationPeriod> findByIdEquals(Long id);
}
