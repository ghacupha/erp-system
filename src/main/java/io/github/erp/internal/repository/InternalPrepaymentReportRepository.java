package io.github.erp.internal.repository;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.domain.PrepaymentReport;
import io.github.erp.domain.PrepaymentReportTuple;
import io.github.erp.repository.PrepaymentReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data SQL repository for the PrepaymentReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalPrepaymentReportRepository extends
    PrepaymentReportRepository,
    JpaRepository<PrepaymentReport, Long>,
    JpaSpecificationExecutor<PrepaymentReport> {

    @Query(value = " SELECT " +
        "    p.id, " +
        "    p.catalogue_number as CatalogueNumber, " +
        "    p.particulars, " +
        "    d.dealer_name as DealerName, " +
        "    s.payment_number as PaymentNumber, " +
        "    s.payment_date as PaymentDate, " +
        "    c.iso_4217_currency_code as CurrencyCode, " +
        "    COALESCE(p.prepayment_amount, 0) as PrepaymentAmount, " +
        "    COALESCE(SUM(pa.prepayment_amount), 0) as AmortisedAmount, " +
        "    COALESCE(p.prepayment_amount, 0) - COALESCE(SUM(pa.prepayment_amount), 0) as OutstandingAmount " +
        " FROM prepayment_account p " +
        " LEFT JOIN dealer d ON d.id = p.dealer_id " +
        " LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id " +
        " LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id " +
        " LEFT JOIN " +
        "  prepayment_amortization pa ON p.id = pa.prepayment_account_id AND pa.fiscal_month_id IN ( " +
        "    SELECT fm.id " +
        "    FROM fiscal_month fm " +
        "    WHERE fm.end_date <= :reportDate " +
        " )" +
        " WHERE s.payment_date <= :reportDate OR p.id NOT IN ( " +
        "    SELECT prep.id" +
        "    FROM prepayment_account prep" +
        "    LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id" +
        "    WHERE s.payment_date > :reportDate " +
        " )" +
        " GROUP BY" +
        "    p.id," +
        "    p.catalogue_number," +
        "    p.particulars," +
        "    d.dealer_name," +
        "    s.payment_number," +
        "    s.payment_date," +
        "    c.iso_4217_currency_code," +
        "    p.prepayment_amount", nativeQuery = true)
    Page<PrepaymentReportTuple> findAllByReportDate(@Param("reportDate") LocalDate reportDate, Pageable page);


    @Query(value = "SELECT" +
        "    p.id," +
        "    p.catalogue_number AS CatalogueNumber, " +
        "    p.particulars, " +
        "    d.dealer_name AS DealerName, " +
        "    s.payment_number AS PaymentNumber, " +
        "    s.payment_date AS PaymentDate, " +
        "    c.iso_4217_currency_code AS CurrencyCode, " +
        "    COALESCE(p.prepayment_amount, 0) AS PrepaymentAmount, " +
        "    COALESCE(pa.AmortisedAmount, 0) AS AmortisedAmount, " +
        "    COALESCE(p.prepayment_amount, 0) - COALESCE(pa.AmortisedAmount, 0) AS OutstandingAmount " +
        "FROM prepayment_account p " +
        "LEFT JOIN dealer d ON d.id = p.dealer_id " +
        "LEFT JOIN settlement_currency c ON c.id = p.settlement_currency_id " +
        "LEFT JOIN settlement s ON s.id = p.prepayment_transaction_id " +
        "LEFT JOIN (" +
        "    SELECT pa.prepayment_account_id," +
        "           SUM(pa.prepayment_amount) AS AmortisedAmount " +
        "    FROM prepayment_amortization pa " +
        "    WHERE pa.fiscal_month_id IN (" +
        "        SELECT fm.id " +
        "        FROM fiscal_month fm " +
        "        WHERE fm.end_date <= :reportDate " +
        "    )" +
        "    GROUP BY pa.prepayment_account_id " +
        ") pa ON p.id = pa.prepayment_account_id " +
        "WHERE " +
        "    (s.payment_date <= :reportDate OR s.payment_date IS NULL) " +
        "    AND (pa.prepayment_account_id IS NOT NULL OR s.payment_date IS NULL) " +
        "    AND p.id = :id", nativeQuery = true)
    Optional<PrepaymentReportTuple> findOneByReportDate(@Param("reportDate") LocalDate reportDate, @Param("id") long id);
}
