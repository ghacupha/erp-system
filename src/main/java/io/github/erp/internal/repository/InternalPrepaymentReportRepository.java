package io.github.erp.internal.repository;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
        " WHERE p.recognition_date <= :reportDate OR p.id NOT IN ( " +
        "    SELECT prep.id" +
        "    FROM prepayment_account prep" +
        "    LEFT JOIN settlement s ON s.id = prep.prepayment_transaction_id" +
        "    WHERE prep.recognition_date > :reportDate " +
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
        "    (p.recognition_date <= :reportDate OR p.recognition_date IS NULL) " +
        "    AND (pa.prepayment_account_id IS NOT NULL OR p.recognition_date IS NULL) " +
        "    AND p.id = :id", nativeQuery = true)
    Optional<PrepaymentReportTuple> findOneByReportDate(@Param("reportDate") LocalDate reportDate, @Param("id") long id);
}
