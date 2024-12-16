package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.TransactionAccountReportItem;
import io.github.erp.domain.TransactionDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data SQL repository for the TransactionAccountReportItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalTransactionAccountReportItemRepository
    extends JpaRepository<TransactionAccountReportItem, Long>, JpaSpecificationExecutor<TransactionAccountReportItem> {

//    @Query(
//        nativeQuery = true,
//        value = "" +
//            "WITH fiscal_year_period AS (" +
//            "    SELECT start_date, end_date" +
//            "    FROM fiscal_year" +
//            "    WHERE :reportDate BETWEEN start_date AND end_date" +
//            ")" +
//            "SELECT " +
//            "    ta.id, " +
//            "    ta.account_name, " +
//            "    ta.account_number, " +
//            "    SUM(" +
//            "        CASE " +
//            "            WHEN td.debit_account_id = ta.id THEN -td.amount " +
//            "            WHEN td.credit_account_id = ta.id THEN td.amount " +
//            "            ELSE 0 " +
//            "        END" +
//            "    ) AS account_balance " +
//            "FROM " +
//            "    transaction_account ta " +
//            "LEFT JOIN " +
//            "    transaction_details td " +
//            "    ON ta.id = td.debit_account_id OR ta.id = td.credit_account_id " +
//            "JOIN " +
//            "    fiscal_year_period fyp " +
//            "    ON (ta.account_type != 'EQUITY' OR (ta.account_type = 'EQUITY' AND td.transaction_date BETWEEN fyp.start_date AND fyp.end_date))" +
//            "WHERE " +
//            "    td.transaction_date <= :reportDate" +
//            "GROUP BY " +
//            "    ta.id, " +
//            "    ta.account_name, " +
//            "    ta.account_number"
//    )
//    Page<TransactionAccountReportItem> calculateReportItems(@Param("reportDate") LocalDate reportDate, Pageable pageable);

//    @Query(
//        nativeQuery = true,
//        value = "" +
//            "SELECT " +
//            "    ta.id, " +
//            "    ta.account_name, " +
//            "    ta.account_number, " +
//            "    SUM(" +
//            "        CASE " +
//            "            WHEN td.debit_account_id = ta.id THEN -td.amount " +
//            "            WHEN td.credit_account_id = ta.id THEN td.amount " +
//            "            ELSE 0 " +
//            "        END" +
//            "    ) AS account_balance " +
//            "FROM " +
//            "    transaction_account ta " +
//            "LEFT JOIN " +
//            "    transaction_details td " +
//            "    ON ta.id = td.debit_account_id OR ta.id = td.credit_account_id " +
//            "JOIN " +
//            "    fiscal_year fy " +
//            "    ON (ta.account_type != 'EQUITY' OR (ta.account_type = 'EQUITY' AND td.transaction_date BETWEEN fy.start_date AND fy.end_date)) " +
//            "WHERE " +
//            "    td.transaction_date <= :reportDate " +
//            "    AND :reportDate BETWEEN fy.start_date AND fy.end_date " +
//            "GROUP BY " +
//            "    ta.id, " +
//            "    ta.account_name, " +
//            "    ta.account_number"
//    )
//    Page<TransactionAccountReportItem> calculateReportItems(@Param("reportDate") LocalDate reportDate, Pageable pageable);

    @Query(
        nativeQuery = true,
        value = "" +
            "WITH fiscal_year_period AS (" +
            "    SELECT start_date, end_date" +
            "    FROM fiscal_year" +
            "    WHERE :reportDate BETWEEN start_date AND end_date" +
            ")" +
            "SELECT " +
            "    ta.id, " +
            "    ta.account_name, " +
            "    ta.account_number, " +
            "    SUM(" +
            "        CASE " +
            "            WHEN ta.id = re.retained_earnings_account_id THEN td.amount " +
            "            WHEN td.debit_account_id = ta.id THEN -td.amount " +
            "            WHEN td.credit_account_id = ta.id THEN td.amount " +
            "            ELSE 0 " +
            "        END" +
            "    ) AS account_balance " +
            "FROM " +
            "    transaction_account ta " +
            "LEFT JOIN " +
            "    transaction_details td " +
            "    ON ta.id = td.debit_account_id OR ta.id = td.credit_account_id " +
            "JOIN " +
            "    reporting_entity re " +
            "    ON re.id = :reportingEntityId " +
            "JOIN " +
            "    fiscal_year_period fyp " +
            "    ON (ta.account_type != 'EQUITY' OR (ta.account_type = 'EQUITY' AND td.transaction_date BETWEEN fyp.start_date AND fyp.end_date)) " +
            "WHERE " +
            "    td.transaction_date <= :reportDate " +
            "    AND (ta.id != re.retained_earnings_account_id OR (ta.id = re.retained_earnings_account_id AND td.transaction_date < fyp.start_date)) " +
            "GROUP BY " +
            "    ta.id, " +
            "    ta.account_name, " +
            "    ta.account_number"
    )
    Page<TransactionAccountReportItem> calculateReportItems(@Param("reportDate") LocalDate reportDate, @Param("reportingEntityId") Long reportingEntityId, Pageable pageable);


}
