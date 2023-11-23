package io.github.erp.internal.repository;

import io.github.erp.domain.FiscalMonth;
import io.github.erp.repository.FiscalMonthRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface InternalFiscalMonthRepository extends
    FiscalMonthRepository,
    JpaRepository<FiscalMonth, Long>,
    JpaSpecificationExecutor<FiscalMonth> {

    Optional<FiscalMonth> findFiscalMonthByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}
