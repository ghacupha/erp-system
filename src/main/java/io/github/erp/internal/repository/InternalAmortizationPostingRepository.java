package io.github.erp.internal.repository;

import io.github.erp.domain.AmortizationPostingReport;
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.repository.AmortizationPostingReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface InternalAmortizationPostingRepository
    extends AmortizationPostingReportRepository,
    JpaRepository<AmortizationPostingReport, Long> {

    @Query(value = "SELECT " +
        " p.id  as id, " +
        " pa.catalogue_number as catalogueNumber," +
        " da.account_number as debitAccount," +
        " ca.account_number as creditAccount," +
        " description as description, " +
        " p.prepayment_amount as amortizationAmount  " +
        "FROM public.prepayment_amortization p  " +
        "LEFT JOIN prepayment_account pa on prepayment_account_id = pa.id " +
        "LEFT JOIN transaction_account da on p.debit_account_id = da.id  " +
        "LEFT JOIN transaction_account ca on p.credit_account_id = ca.id " +
        "LEFT JOIN fiscal_month fm on p.fiscal_month_id = fm.id " +
        "WHERE :reportDate BETWEEN fm.start_date AND fm.end_date", nativeQuery = true)
    Page<AmortizationPostingReportInternal> findByReportDate(@Param("reportDate") LocalDate reportDate, Pageable pageable);
}
