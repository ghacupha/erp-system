package io.github.erp.erp.repository.prepayments;

import io.github.erp.erp.reports.prepayments.UnallocatedPrepaymentAmortizationReportItem;
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
public class UnallocatedPrepaymentAmortizationReportRepository {

    private static final String BASE_QUERY =
        "select " +
        "  pa.id as amortization_id, " +
        "  pa.description, " +
        "  pa.prepayment_period, " +
        "  pa.prepayment_amount, " +
        "  currency.iso_4217_currency_code as currency_code, " +
        "  debit.account_number as debit_account_number, " +
        "  debit.account_name as debit_account_name, " +
        "  credit.account_number as credit_account_number, " +
        "  credit.account_name as credit_account_name, " +
        "  fm.fiscal_month_code, " +
        "  ap.period_code as amortization_period_code, " +
        "  pcr.id as compilation_request_id " +
        "from prepayment_amortization pa " +
        "left join settlement_currency currency on currency.id = pa.settlement_currency_id " +
        "left join transaction_account debit on debit.id = pa.debit_account_id " +
        "left join transaction_account credit on credit.id = pa.credit_account_id " +
        "left join fiscal_month fm on fm.id = pa.fiscal_month_id " +
        "left join amortization_period ap on ap.id = pa.amortization_period_id " +
        "left join prepayment_compilation_request pcr on pcr.id = pa.prepayment_compilation_request_id " +
        "where pa.prepayment_account_id is null " +
        "  and coalesce(pa.inactive, false) = false ";

    @PersistenceContext
    private EntityManager entityManager;

    public Page<UnallocatedPrepaymentAmortizationReportItem> findUnallocated(Pageable pageable) {
        Query query = entityManager.createNativeQuery(BASE_QUERY + "order by pa.prepayment_period desc, pa.id desc");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        List<UnallocatedPrepaymentAmortizationReportItem> content = rows.stream().map(this::toReportItem).toList();

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + BASE_QUERY + ") unallocated_amortizations");
        Number total = (Number) countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total.longValue());
    }

    private UnallocatedPrepaymentAmortizationReportItem toReportItem(Object[] row) {
        return new UnallocatedPrepaymentAmortizationReportItem(
            toLong(row[0]),
            toNullableString(row[1]),
            toLocalDate(row[2]),
            toBigDecimal(row[3]),
            toNullableString(row[4]),
            toNullableString(row[5]),
            toNullableString(row[6]),
            toNullableString(row[7]),
            toNullableString(row[8]),
            toNullableString(row[9]),
            toNullableString(row[10]),
            toLong(row[11])
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
