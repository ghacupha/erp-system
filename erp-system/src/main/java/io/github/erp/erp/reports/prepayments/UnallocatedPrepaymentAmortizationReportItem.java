package io.github.erp.erp.reports.prepayments;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UnallocatedPrepaymentAmortizationReportItem {

    private Long amortizationId;
    private String description;
    private LocalDate prepaymentPeriod;
    private BigDecimal prepaymentAmount;
    private String currencyCode;
    private String debitAccountNumber;
    private String debitAccountName;
    private String creditAccountNumber;
    private String creditAccountName;
    private String fiscalMonthCode;
    private String amortizationPeriodCode;
    private Long compilationRequestId;

    public UnallocatedPrepaymentAmortizationReportItem(
        Long amortizationId,
        String description,
        LocalDate prepaymentPeriod,
        BigDecimal prepaymentAmount,
        String currencyCode,
        String debitAccountNumber,
        String debitAccountName,
        String creditAccountNumber,
        String creditAccountName,
        String fiscalMonthCode,
        String amortizationPeriodCode,
        Long compilationRequestId
    ) {
        this.amortizationId = amortizationId;
        this.description = description;
        this.prepaymentPeriod = prepaymentPeriod;
        this.prepaymentAmount = prepaymentAmount;
        this.currencyCode = currencyCode;
        this.debitAccountNumber = debitAccountNumber;
        this.debitAccountName = debitAccountName;
        this.creditAccountNumber = creditAccountNumber;
        this.creditAccountName = creditAccountName;
        this.fiscalMonthCode = fiscalMonthCode;
        this.amortizationPeriodCode = amortizationPeriodCode;
        this.compilationRequestId = compilationRequestId;
    }

    public Long getAmortizationId() {
        return amortizationId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPrepaymentPeriod() {
        return prepaymentPeriod;
    }

    public BigDecimal getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public String getDebitAccountName() {
        return debitAccountName;
    }

    public String getCreditAccountNumber() {
        return creditAccountNumber;
    }

    public String getCreditAccountName() {
        return creditAccountName;
    }

    public String getFiscalMonthCode() {
        return fiscalMonthCode;
    }

    public String getAmortizationPeriodCode() {
        return amortizationPeriodCode;
    }

    public Long getCompilationRequestId() {
        return compilationRequestId;
    }
}
