package io.github.erp.erp.repository.prepayments;

import io.github.erp.erp.reports.prepayments.OverAmortisedPrepaymentAccountReportItem;
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
public class OverAmortisedPrepaymentAccountReportRepository {

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
        "  coalesce(sum(pa.prepayment_amount), 0) - coalesce(p.prepayment_amount, 0) as over_amortised_amount, " +
        "  count(pa.id) as amortization_entry_count, " +
        "  max(pa.prepayment_period) as last_amortization_date " +
        "from prepayment_account p " +
        "inner join prepayment_amortization pa on pa.prepayment_account_id = p.id and coalesce(pa.inactive, false) = false " +
        "left join dealer d on d.id = p.dealer_id " +
        "left join transaction_account debit on debit.id = p.debit_account_id " +
        "left join transaction_account transfer on transfer.id = p.transfer_account_id " +
        "left join settlement_currency currency on currency.id = p.settlement_currency_id " +
        "group by " +
        "  p.id, p.catalogue_number, p.particulars, p.recognition_date, d.dealer_name, " +
        "  debit.account_number, debit.account_name, transfer.account_number, transfer.account_name, " +
        "  currency.iso_4217_currency_code, p.prepayment_amount " +
        "having coalesce(sum(pa.prepayment_amount), 0) > coalesce(p.prepayment_amount, 0) ";

    @PersistenceContext
    private EntityManager entityManager;

    public Page<OverAmortisedPrepaymentAccountReportItem> findOverAmortised(Pageable pageable) {
        Query query = entityManager.createNativeQuery(BASE_QUERY + "order by over_amortised_amount desc, p.catalogue_number asc");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        List<OverAmortisedPrepaymentAccountReportItem> content = rows.stream().map(this::toReportItem).toList();

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + BASE_QUERY + ") over_amortised_accounts");
        Number total = (Number) countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total.longValue());
    }

    private OverAmortisedPrepaymentAccountReportItem toReportItem(Object[] row) {
        return new OverAmortisedPrepaymentAccountReportItem(
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
