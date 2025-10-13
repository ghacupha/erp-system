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
import io.github.erp.internal.service.ledgers.TransactionAccountAdjacent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the TransactionDetails entity.
 */
@Repository
public interface InternalTransactionDetailsRepository
    extends JpaRepository<TransactionDetails, Long>, JpaSpecificationExecutor<TransactionDetails>, TransactionAccountAdjacent {
    @Query(
        value = "select distinct transactionDetails from TransactionDetails transactionDetails left join fetch transactionDetails.placeholders",
        countQuery = "select count(distinct transactionDetails) from TransactionDetails transactionDetails"
    )
    Page<TransactionDetails> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct transactionDetails from TransactionDetails transactionDetails left join fetch transactionDetails.placeholders")
    List<TransactionDetails> findAllWithEagerRelationships();

    @Query(
        "select transactionDetails from TransactionDetails transactionDetails left join fetch transactionDetails.placeholders where transactionDetails.id =:id"
    )
    Optional<TransactionDetails> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "  CAST(reg.id  AS BIGINT)  " +
            "  FROM public.transaction_account reg  " +
            " LEFT JOIN transaction_details aga on reg.id = aga.debit_account_id  " +
            " WHERE aga.debit_account_id IS NOT NULL " +
            "UNION " +
            " SELECT  " +
            "  CAST(reg.id  AS BIGINT)  " +
            "  FROM public.transaction_account reg  " +
            " LEFT JOIN transaction_details aga on reg.id = aga.credit_account_id  " +
            " WHERE aga.credit_account_id IS NOT NULL",

        countQuery = "" +
            "SELECT  " +
            "  CAST(reg.id  AS BIGINT)  " +
            "  FROM public.transaction_account reg  " +
            " LEFT JOIN transaction_details aga on reg.id = aga.debit_account_id  " +
            " WHERE aga.debit_account_id IS NOT NULL " +
            "UNION " +
            " SELECT  " +
            "  CAST(reg.id  AS BIGINT)  " +
            "  FROM public.transaction_account reg  " +
            " LEFT JOIN transaction_details aga on reg.id = aga.credit_account_id  " +
            " WHERE aga.credit_account_id IS NOT NULL"

    )
    @Override
    List<Long> findAdjacentIds();
}
