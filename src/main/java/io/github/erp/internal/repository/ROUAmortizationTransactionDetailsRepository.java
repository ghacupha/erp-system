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

import io.github.erp.domain.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ROUAmortizationTransactionDetailsRepository
    extends JpaRepository<TransactionDetails, Long>, JpaSpecificationExecutor<TransactionDetails> {

    // @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        nativeQuery = true,
        value = "" +
            "INSERT INTO transaction_details (id, entry_id, transaction_date, description, amount, debit_account_id, credit_account_id) " +
            "SELECT " +
            "    nextval('sequence_generator') AS id," +
            "    nextval('transaction_entry_id_sequence') AS entry_id," +
            "    lp.end_date AS transaction_date," +
            "    re.description AS description," +
            "    re.depreciation_amount AS amount," +
            "    ar.debit_id AS debit_account_id," +
            "    ar.credit_id AS credit_account_id " +
            "FROM " +
            "    rou_depreciation_entry re " +
            "JOIN " +
            "    ifrs16lease_contract lc ON re.lease_contract_id = lc.id " +
            "JOIN " +
            "    taamortization_rule ar ON ar.lease_contract_id = lc.id " +
            "LEFT JOIN " +
            "    lease_period lp ON re.lease_period_id = lp.id"
    )
    void insertTransactionDetails();
}
