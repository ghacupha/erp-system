package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.WIPListItem;
import io.github.erp.domain.WIPListItemREPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the WIPListItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalWIPListItemRepository extends JpaRepository<WIPListItem, Long>, JpaSpecificationExecutor<WIPListItem> {

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "    r.id AS id, " +
            "    sequence_number AS sequenceNumber, " +
            "    particulars AS particulars, " +
            "    instalment_date AS instalmentDate, " +
            "    instalment_amount AS instalmentAmount, " +
            "    crn.iso_4217_currency_code AS settlementCurrency, " +
            "    sol.outlet_code AS outletCode, " +
            "    sett.payment_number AS settlementTransaction, " +
            "    sett.payment_date AS settlementTransactionDate," +
            "    d.dealer_name AS dealerName, " +
            "    w.project_title AS workProject " +
            "FROM public.work_in_progress_registration r " +
            "    LEFT JOIN settlement_currency crn on crn.id = r.settlement_currency_id " +
            "    LEFT JOIN service_outlet sol ON sol.id = r.outlet_code_id " +
            "    LEFT JOIN settlement sett ON sett.id = r.settlement_transaction_id " +
            "    LEFT JOIN dealer d ON d.id = r.dealer_id " +
            "    LEFT JOIN work_project_register w ON w.id = r.work_project_register_id ",

        countQuery = "" +
            "SELECT " +
            "    r.id, " +
            "    sequence_number, " +
            "    particulars, " +
            "    instalment_date, " +
            "    instalment_amount, " +
            "    crn.iso_4217_currency_code, " +
            "    sol.outlet_code, " +
            "    sett.payment_number AS settlementTransaction, " +
            "    sett.payment_date AS settlementTransactionDate," +
            "    d.dealer_name AS dealerName, " +
            "    w.project_title AS workProject " +
            "FROM public.work_in_progress_registration r " +
            "    LEFT JOIN settlement_currency crn on crn.id = r.settlement_currency_id " +
            "    LEFT JOIN service_outlet sol ON sol.id = r.outlet_code_id " +
            "    LEFT JOIN settlement sett ON sett.id = r.settlement_transaction_id " +
            "    LEFT JOIN dealer d ON d.id = r.dealer_id " +
            "    LEFT JOIN work_project_register w ON w.id = r.work_project_register_id "
    )
    Page<WIPListItemREPO> findAllSpecifiedReportItems(Specification<WIPListItem> specification, Pageable pageable);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT " +
            "    r.id AS id, " +
            "    sequence_number AS sequenceNumber, " +
            "    particulars AS particulars, " +
            "    instalment_date AS instalmentDate, " +
            "    instalment_amount AS instalmentAmount, " +
            "    crn.iso_4217_currency_code AS settlementCurrency, " +
            "    sol.outlet_code AS outletCode, " +
            "    sett.payment_number AS settlementTransaction, " +
            "    sett.payment_date AS settlementTransactionDate," +
            "    d.dealer_name AS dealerName, " +
            "    w.project_title AS workProject " +
            "FROM public.work_in_progress_registration r " +
            "    LEFT JOIN settlement_currency crn on crn.id = r.settlement_currency_id " +
            "    LEFT JOIN service_outlet sol ON sol.id = r.outlet_code_id " +
            "    LEFT JOIN settlement sett ON sett.id = r.settlement_transaction_id " +
            "    LEFT JOIN dealer d ON d.id = r.dealer_id " +
            "    LEFT JOIN work_project_register w ON w.id = r.work_project_register_id ",

        countQuery = "" +
            "SELECT " +
            "    r.id, " +
            "    sequence_number, " +
            "    particulars, " +
            "    instalment_date, " +
            "    instalment_amount, " +
            "    crn.iso_4217_currency_code, " +
            "    sol.outlet_code, " +
            "    sett.payment_number AS settlementTransaction, " +
            "    sett.payment_date AS settlementTransactionDate," +
            "    d.dealer_name AS dealerName, " +
            "    w.project_title AS workProject " +
            "FROM public.work_in_progress_registration r " +
            "    LEFT JOIN settlement_currency crn on crn.id = r.settlement_currency_id " +
            "    LEFT JOIN service_outlet sol ON sol.id = r.outlet_code_id " +
            "    LEFT JOIN settlement sett ON sett.id = r.settlement_transaction_id " +
            "    LEFT JOIN dealer d ON d.id = r.dealer_id " +
            "    LEFT JOIN work_project_register w ON w.id = r.work_project_register_id "
    )
    Page<WIPListItemREPO> findAllReportItems(Pageable pageable);
}
