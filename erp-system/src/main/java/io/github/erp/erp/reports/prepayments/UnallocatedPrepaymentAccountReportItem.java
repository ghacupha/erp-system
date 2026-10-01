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

public class UnallocatedPrepaymentAccountReportItem {

    private Long prepaymentAccountId;
    private String catalogueNumber;
    private String particulars;
    private LocalDate recognitionDate;
    private String dealerName;
    private String debitAccountNumber;
    private String debitAccountName;
    private String transferAccountNumber;
    private String transferAccountName;
    private String currencyCode;
    private BigDecimal prepaymentAmount;
    private BigDecimal amortisedAmount;
    private BigDecimal outstandingAmount;
    private Long amortizationEntryCount;
    private LocalDate lastAmortizationDate;

    public UnallocatedPrepaymentAccountReportItem(
        Long prepaymentAccountId,
        String catalogueNumber,
        String particulars,
        LocalDate recognitionDate,
        String dealerName,
        String debitAccountNumber,
        String debitAccountName,
        String transferAccountNumber,
        String transferAccountName,
        String currencyCode,
        BigDecimal prepaymentAmount,
        BigDecimal amortisedAmount,
        BigDecimal outstandingAmount,
        Long amortizationEntryCount,
        LocalDate lastAmortizationDate
    ) {
        this.prepaymentAccountId = prepaymentAccountId;
        this.catalogueNumber = catalogueNumber;
        this.particulars = particulars;
        this.recognitionDate = recognitionDate;
        this.dealerName = dealerName;
        this.debitAccountNumber = debitAccountNumber;
        this.debitAccountName = debitAccountName;
        this.transferAccountNumber = transferAccountNumber;
        this.transferAccountName = transferAccountName;
        this.currencyCode = currencyCode;
        this.prepaymentAmount = prepaymentAmount;
        this.amortisedAmount = amortisedAmount;
        this.outstandingAmount = outstandingAmount;
        this.amortizationEntryCount = amortizationEntryCount;
        this.lastAmortizationDate = lastAmortizationDate;
    }

    public Long getPrepaymentAccountId() {
        return prepaymentAccountId;
    }

    public String getCatalogueNumber() {
        return catalogueNumber;
    }

    public String getParticulars() {
        return particulars;
    }

    public LocalDate getRecognitionDate() {
        return recognitionDate;
    }

    public String getDealerName() {
        return dealerName;
    }

    public String getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public String getDebitAccountName() {
        return debitAccountName;
    }

    public String getTransferAccountNumber() {
        return transferAccountNumber;
    }

    public String getTransferAccountName() {
        return transferAccountName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public BigDecimal getAmortisedAmount() {
        return amortisedAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public Long getAmortizationEntryCount() {
        return amortizationEntryCount;
    }

    public LocalDate getLastAmortizationDate() {
        return lastAmortizationDate;
    }
}
