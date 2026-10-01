package io.github.erp.erp.repository.prepayments;

/*-
 * Erp System - Mark X No 12 (Kadar Series) Server ver 1.9.0
 * Copyright © 2021 - 2026 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.erp.reports.prepayments.UnallocatedPrepaymentAccountReportItem;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class UnallocatedPrepaymentAccountReportRepository {

    private static final String BASE_QUERY =
        "select " +
        "  p.id as prepayment_account_id, " +
        "  p.catalogue_number, " +
        "  p.particulars, " +
        "  p.recognition_date, " +
        "  d.dealer_name, " +
        "  debit.account_number as debit_account_number, " +
        "  debit.account_name as debit_account_name, " +
        "  transfer.account_number as transfer_account_number, " +
        "  transfer.account_name as transfer_account_name, " +
        "  currency.iso_4217_currency_code as currency_code, " +
        "  coalesce(p.prepayment_amount, 0) as prepayment_amount, " +
        "  coalesce(sum(pa.prepayment_amount), 0) as amortised_amount, " +
        "  coalesce(p.prepayment_amount, 0) - coalesce(sum(pa.prepayment_amount), 0) as outstanding_amount, " +
        "  count(pa.id) as amortization_entry_count, " +
        "  max(pa.prepayment_period) as last_amortization_date " +
        "from prepayment_account p " +
        "left join prepayment_amortization pa on pa.prepayment_account_id = p.id and coalesce(pa.inactive, false) = false " +
        "left join dealer d on d.id = p.dealer_id " +
        "left join transaction_account debit on debit.id = p.debit_account_id " +
        "left join transaction_account transfer on transfer.id = p.transfer_account_id " +
        "left join settlement_currency currency on currency.id = p.settlement_currency_id " +
        // Exclude accounts that already have a marshalling record awaiting compilation - without
        // this, an account stays in the "awaiting marshalling" list until compile() runs (since
        // that's the only step that creates prepayment_amortization rows), letting a user pick
        // the same account again and create a second marshalling for it before the first is ever
        // compiled. Found live: see DuplicatePrepaymentMarshallingException.
        "where not exists (" +
        "  select 1 from prepayment_marshalling pm " +
        "  where pm.prepayment_account_id = p.id " +
        "  and coalesce(pm.inactive, false) = false " +
        "  and coalesce(pm.processed, false) = false" +
        ") " +
        "group by " +
        "  p.id, p.catalogue_number, p.particulars, p.recognition_date, d.dealer_name, " +
        "  debit.account_number, debit.account_name, transfer.account_number, transfer.account_name, " +
        "  currency.iso_4217_currency_code, p.prepayment_amount " +
        "having coalesce(p.prepayment_amount, 0) - coalesce(sum(pa.prepayment_amount), 0) >= :minimumOutstandingAmount ";

    @PersistenceContext
    private EntityManager entityManager;

    public Page<UnallocatedPrepaymentAccountReportItem> findUnallocated(BigDecimal minimumOutstandingAmount, Pageable pageable) {
        Query query = entityManager.createNativeQuery(BASE_QUERY + "order by outstanding_amount desc, p.catalogue_number asc");
        query.setParameter("minimumOutstandingAmount", minimumOutstandingAmount);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        List<UnallocatedPrepaymentAccountReportItem> content = rows.stream().map(this::toReportItem).toList();

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + BASE_QUERY + ") outstanding_accounts");
        countQuery.setParameter("minimumOutstandingAmount", minimumOutstandingAmount);
        Number total = (Number) countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total.longValue());
    }

    private UnallocatedPrepaymentAccountReportItem toReportItem(Object[] row) {
        return new UnallocatedPrepaymentAccountReportItem(
            toLong(row[0]),
            toNullableString(row[1]),
            toNullableString(row[2]),
            toLocalDate(row[3]),
            toNullableString(row[4]),
            toNullableString(row[5]),
            toNullableString(row[6]),
            toNullableString(row[7]),
            toNullableString(row[8]),
            toNullableString(row[9]),
            toBigDecimal(row[10]),
            toBigDecimal(row[11]),
            toBigDecimal(row[12]),
            toLong(row[13]),
            toLocalDate(row[14])
        );
    }

    private Long toLong(Object value) {
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return value instanceof Number ? BigDecimal.valueOf(((Number) value).doubleValue()) : BigDecimal.ZERO;
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        return value instanceof Date ? ((Date) value).toLocalDate() : null;
    }

    private String toNullableString(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
