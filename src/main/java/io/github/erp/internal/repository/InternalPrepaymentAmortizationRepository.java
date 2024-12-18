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
import io.github.erp.domain.PrepaymentAmortization;
import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.internal.service.ledgers.TransactionAccountAdjacent;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface InternalPrepaymentAmortizationRepository extends
    PrepaymentAmortizationRepository,
    JpaRepository<PrepaymentAmortization, Long>,
    JpaSpecificationExecutor<PrepaymentAmortization>,
    TransactionAccountAdjacent {

    List<PrepaymentAmortization> findAllByPrepaymentCompilationRequest(@NotNull PrepaymentCompilationRequest prepaymentCompilationRequest);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT  " +
            "  CAST(reg.id  AS BIGINT)  " +
            "  FROM public.transaction_account reg  " +
            " LEFT JOIN prepayment_amortization aga on reg.id = aga.debit_account_id  " +
            " WHERE aga.debit_account_id IS NOT NULL " +
            "UNION " +
            " SELECT  " +
            "  CAST(reg.id  AS BIGINT)  " +
            "  FROM public.transaction_account reg  " +
            " LEFT JOIN prepayment_amortization aga on reg.id = aga.credit_account_id  " +
            " WHERE aga.credit_account_id IS NOT NULL",

        countQuery = "" +
            "SELECT  " +
            "  CAST(reg.id  AS BIGINT)  " +
            "  FROM public.transaction_account reg  " +
            " LEFT JOIN prepayment_amortization aga on reg.id = aga.debit_account_id  " +
            " WHERE aga.debit_account_id IS NOT NULL " +
            "UNION " +
            " SELECT  " +
            "  CAST(reg.id  AS BIGINT)  " +
            "  FROM public.transaction_account reg  " +
            " LEFT JOIN prepayment_amortization aga on reg.id = aga.credit_account_id  " +
            " WHERE aga.credit_account_id IS NOT NULL"

    )
    List<Long> findAdjacentIds();

}
