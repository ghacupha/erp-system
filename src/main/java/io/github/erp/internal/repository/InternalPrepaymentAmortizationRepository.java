package io.github.erp.internal.repository;

import io.github.erp.domain.PrepaymentAmortization;
import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface InternalPrepaymentAmortizationRepository extends
    PrepaymentAmortizationRepository,
    JpaRepository<PrepaymentAmortization, Long>,
    JpaSpecificationExecutor<PrepaymentAmortization> {

    List<PrepaymentAmortization> findAllByPrepaymentCompilationRequest(@NotNull PrepaymentCompilationRequest prepaymentCompilationRequest);
}
