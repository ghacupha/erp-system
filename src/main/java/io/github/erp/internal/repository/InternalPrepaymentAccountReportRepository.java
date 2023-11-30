package io.github.erp.internal.repository;

import io.github.erp.domain.PrepaymentAccountReport;
import io.github.erp.domain.PrepaymentAccountReportTuple;
import io.github.erp.repository.PrepaymentAccountReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface InternalPrepaymentAccountReportRepository
    extends PrepaymentAccountReportRepository, JpaRepository<PrepaymentAccountReport, Long>, JpaSpecificationExecutor<PrepaymentAccountReport> {

//    @Query(value = "SELECT " +
//        " ps.acc_id AS id, " +
//        " ps.account_name AS PrepaymentAccount, " +
//        " SUM(ps.prepayment_amount) AS PrepaymentAmount, " +
//        " SUM(ps.amortised_amount) AS AmortisedAmount, " +
//        " SUM(ps.outstanding_amount) AS OutstandingAmount, " +
//        " SUM(ps.prepayment_item_count) AS NumberOfPrepaymentAccounts, " +
//        " SUM(ps.amortised_item_count) AS NumberOfAmortisedItems " +
//        " FROM ( SELECT " +
//        " ta.id AS acc_id, " +
//        "    p.id, " +
//        "    ta.account_name as account_name, " +
//        "    c.iso_4217_currency_code as currency, " +
//        "    COALESCE ( p.prepayment_amount, 0) as prepayment_amount, " +
//        "    COALESCE ( SUM(pa.prepayment_amount), 0) as amortised_amount, " +
//        "    COALESCE ( p.prepayment_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) AS outstanding_amount, " +
//        " COALESCE ( COUNT(DISTINCT p.id), 0) as prepayment_item_count, " +
//        " COALESCE ( COUNT(ta.id), 0) as amortised_item_count  " +
//        " FROM transaction_account ta " +
//        "         JOIN prepayment_account p ON ta.id = p.debit_account_id " +
//        "         LEFT JOIN dealer d ON d.id = p.dealer_id " +
//        "         LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id " +
//        "         LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id " +
//        "         LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN ( " +
//        "    SELECT fm.id " +
//        "    FROM fiscal_month fm " +
//        "    WHERE fm.end_date <= '2023-10-31' ) " +
//        "WHERE s.payment_date <= '2023-10-31' OR p.id NOT IN ( " +
//        "    SELECT prep.id " +
//        "    FROM prepayment_account prep " +
//        "             LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id " +
//        "    WHERE s.payment_date > '2023-10-31' ) " +
//        " GROUP BY " +
//        "    p.id, " +
//        "   ta.id, " +
//        "    ta.account_name, " +
//        "    c.iso_4217_currency_code, " +
//        "    p.prepayment_amount " +
//        " ) ps " +
//        " GROUP BY ps.account_name, ps.acc_id ", nativeQuery = true)
//    Page<PrepaymentAccountReportTuple> findAllByReportDate(@Param("reportDate") LocalDate reportDate, Pageable page);


    @Query(
        nativeQuery = true,
        value = "SELECT " +
        " ps.acc_id AS id, " +
        " ps.account_name as PrepaymentAccount, " +
        " SUM(ps.prepayment_amount) as PrepaymentAmount, " +
        " SUM(ps.amortised_amount) as AmortisedAmount, " +
        " SUM(ps.outstanding_amount) as OutstandingAmount, " +
        " SUM(ps.prepayment_item_count) as NumberOfPrepaymentAccounts, " +
        " SUM(ps.amortised_item_count) as NumberOfAmortisedItems " +
        "FROM (SELECT " +
        "    ta.id as acc_id, " +
        "    p.id,   " +
        "    ta.account_name as account_name, " +
        "    COALESCE(p.prepayment_amount, 0) as prepayment_amount, " +
        "    COALESCE(SUM(pa.prepayment_amount), 0) as amortised_amount, " +
        "    COALESCE(p.prepayment_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) as outstanding_amount, " +
        "    COALESCE(COUNT(DISTINCT p.id), 0) as prepayment_item_count, " +
        "    COALESCE(COUNT(ta.id), 0) as amortised_item_count " +
        "FROM transaction_account ta   " +
        "  JOIN prepayment_account p ON ta.id = p.debit_account_id " +
        "  LEFT JOIN dealer d ON d.id = p.dealer_id " +
        "  LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id " +
        "  LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id " +
        "  LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN ( " +
        "    SELECT fm.id " +
        "    FROM fiscal_month fm   " +
        "    WHERE fm.end_date <= :reportDate   " +
        ")   " +
        "WHERE s.payment_date <= :reportDate OR p.id NOT IN (   " +
        "    SELECT prep.id   " +
        "    FROM prepayment_account prep " +
        "             LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id " +
        "    WHERE s.payment_date > :reportDate   " +
        ")   " +
        " GROUP BY   " +
        "    p.id,   " +
        "    ta.id,   " +
        "    ta.account_name,   " +
        "    c.iso_4217_currency_code,   " +
        "    p.prepayment_amount   " +
        ") ps   " +
        "GROUP BY ps.account_name, ps.acc_id",

        countQuery = "SELECT " +
        " count(ps.acc_id) " +
        " FROM (SELECT " +
        "    ta.id as acc_id, " +
        "    p.id,   " +
        "    ta.account_name as account_name " +
        "FROM transaction_account ta   " +
        "  JOIN prepayment_account p ON ta.id = p.debit_account_id " +
        "  LEFT JOIN dealer d ON d.id = p.dealer_id " +
        "  LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id " +
        "  LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id " +
        "  LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN ( " +
        "    SELECT fm.id " +
        "    FROM fiscal_month fm   " +
        "    WHERE fm.end_date <= :reportDate   " +
        ")   " +
        "WHERE s.payment_date <= :reportDate OR p.id NOT IN (   " +
        "    SELECT prep.id   " +
        "    FROM prepayment_account prep " +
        "             LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id " +
        "    WHERE s.payment_date > :reportDate   " +
        ")   " +
        " GROUP BY   " +
        "    p.id,   " +
        "    ta.id,   " +
        "    ta.account_name,   " +
        "    c.iso_4217_currency_code,   " +
        "    p.prepayment_amount   " +
        ") ps   " +
        "GROUP BY ps.account_name, ps.acc_id"
    )
    Page<PrepaymentAccountReportTuple> findAllByReportDate(@Param("reportDate") LocalDate reportDate, Pageable page);

    @Query(value = "SELECT   " +
        " ps.acc_id AS id,   " +
        " ps.account_name AS PrepaymentAccount,   " +
        " SUM(ps.prepayment_amount) AS PrepaymentAmount,   " +
        " SUM(ps.amortised_amount) AS AmortisedAmount,   " +
        " SUM(ps.outstanding_amount) AS OutstandingAmount,   " +
        " SUM(ps.prepayment_item_count) AS NumberOfPrepaymentAccounts,   " +
        " SUM(ps.amortised_item_count) AS NumberOfAmortisedItems " +
        "FROM (SELECT   " +
        "  ta.id as acc_id,   " +
        "    p.id,   " +
        "    ta.account_name as account_name,   " +
        "    c.iso_4217_currency_code as currency,   " +
        "    COALESCE(p.prepayment_amount, 0) as prepayment_amount,   " +
        "    COALESCE(SUM(pa.prepayment_amount), 0) as amortised_amount,   " +
        "    COALESCE(p.prepayment_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) as outstanding_amount,    " +
        "  COALESCE(COUNT(DISTINCT p.id), 0) as prepayment_item_count,   " +
        "  COALESCE(COUNT(ta.id), 0) as amortised_item_count    " +
        "FROM transaction_account ta   " +
        "         JOIN prepayment_account p ON ta.id = p.debit_account_id   " +
        "         LEFT JOIN dealer d ON d.id = p.dealer_id   " +
        "         LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id   " +
        "         LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id   " +
        "         LEFT JOIN prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN (   " +
        "    SELECT fm.id   " +
        "    FROM fiscal_month fm   " +
        "    WHERE fm.end_date <= :reportDate   " +
        ")   " +
        "WHERE s.payment_date <= :reportDate OR p.id NOT IN (   " +
        "    SELECT prep.id   " +
        "    FROM prepayment_account prep   " +
        "             LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id   " +
        "    WHERE s.payment_date > :reportDate   " +
        ")   " +
        "GROUP BY   " +
        "    p.id,   " +
        "    ta.id,   " +
        "    ta.account_name,   " +
        "    c.iso_4217_currency_code,   " +
        "    p.prepayment_amount   " +
        "    ) ps   " +
        "       " +
        "WHERE ps.acc_id = :id " +
        "GROUP BY ps.account_name, ps.acc_id", nativeQuery = true)
    Optional<PrepaymentAccountReportTuple> findOneByReportDate(@Param("reportDate") LocalDate reportDate, @Param("id") long id);
}

