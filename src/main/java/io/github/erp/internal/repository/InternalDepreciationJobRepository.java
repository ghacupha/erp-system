package io.github.erp.internal.repository;

import io.github.erp.domain.DepreciationJob;
import io.github.erp.repository.DepreciationJobRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InternalDepreciationJobRepository extends
    DepreciationJobRepository,
    JpaRepository<DepreciationJob, Long>,
    JpaSpecificationExecutor<DepreciationJob> {

    @EntityGraph(
        attributePaths = {
            "createdBy",
            "lastModifiedBy",
            "createdBy.userProperties",
            "createdBy.placeholders",
            "lastModifiedBy.userProperties",
            "lastModifiedBy.placeholders",
        })
    Optional<DepreciationJob> findByIdEquals(Long id);
}
