package io.github.erp.service.dto;

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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentAccountReport} entity.
 */
public class PrepaymentAccountReportDTO implements Serializable {

    private Long id;

    private String accountName;

    private String accountNumber;

    private BigDecimal prepaymentAmount;

    private BigDecimal amortisedAmount;

    private BigDecimal outstandingAmount;

    private Integer numberOfPrepaymentAccounts;

    private Integer numberOfAmortisedItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public void setPrepaymentAmount(BigDecimal prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public BigDecimal getAmortisedAmount() {
        return amortisedAmount;
    }

    public void setAmortisedAmount(BigDecimal amortisedAmount) {
        this.amortisedAmount = amortisedAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public Integer getNumberOfPrepaymentAccounts() {
        return numberOfPrepaymentAccounts;
    }

    public void setNumberOfPrepaymentAccounts(Integer numberOfPrepaymentAccounts) {
        this.numberOfPrepaymentAccounts = numberOfPrepaymentAccounts;
    }

    public Integer getNumberOfAmortisedItems() {
        return numberOfAmortisedItems;
    }

    public void setNumberOfAmortisedItems(Integer numberOfAmortisedItems) {
        this.numberOfAmortisedItems = numberOfAmortisedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentAccountReportDTO)) {
            return false;
        }

        PrepaymentAccountReportDTO prepaymentAccountReportDTO = (PrepaymentAccountReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prepaymentAccountReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAccountReportDTO{" +
            "id=" + getId() +
            ", accountName='" + getAccountName() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", prepaymentAmount=" + getPrepaymentAmount() +
            ", amortisedAmount=" + getAmortisedAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            ", numberOfPrepaymentAccounts=" + getNumberOfPrepaymentAccounts() +
            ", numberOfAmortisedItems=" + getNumberOfAmortisedItems() +
            "}";
    }
}
