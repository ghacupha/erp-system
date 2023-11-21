package io.github.erp.repository;

import io.github.erp.domain.PrepaymentCompilationRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrepaymentCompilationRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrepaymentCompilationRequestRepository
    extends JpaRepository<PrepaymentCompilationRequest, Long>, JpaSpecificationExecutor<PrepaymentCompilationRequest> {}
