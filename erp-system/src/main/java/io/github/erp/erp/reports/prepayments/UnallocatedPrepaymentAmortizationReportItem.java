package io.github.erp.erp.reports.prepayments;

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
