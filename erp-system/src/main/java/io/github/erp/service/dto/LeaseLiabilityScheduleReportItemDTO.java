package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
 * A DTO for the {@link io.github.erp.domain.LeaseLiabilityScheduleReportItem} entity.
 */
public class LeaseLiabilityScheduleReportItemDTO implements Serializable {

    private Long id;

    private Integer sequenceNumber;

    private BigDecimal openingBalance;

    private BigDecimal cashPayment;

    private BigDecimal principalPayment;

    private BigDecimal interestPayment;

    private BigDecimal outstandingBalance;

    private BigDecimal interestPayableOpening;

    private BigDecimal interestAccrued;

    private BigDecimal interestPayableClosing;

    private Long amortizationScheduleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(BigDecimal cashPayment) {
        this.cashPayment = cashPayment;
    }

    public BigDecimal getPrincipalPayment() {
        return principalPayment;
    }

    public void setPrincipalPayment(BigDecimal principalPayment) {
        this.principalPayment = principalPayment;
    }

    public BigDecimal getInterestPayment() {
        return interestPayment;
    }

    public void setInterestPayment(BigDecimal interestPayment) {
        this.interestPayment = interestPayment;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public BigDecimal getInterestPayableOpening() {
        return interestPayableOpening;
    }

    public void setInterestPayableOpening(BigDecimal interestPayableOpening) {
        this.interestPayableOpening = interestPayableOpening;
    }

    public BigDecimal getInterestAccrued() {
        return interestAccrued;
    }

    public void setInterestAccrued(BigDecimal interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public BigDecimal getInterestPayableClosing() {
        return interestPayableClosing;
    }

    public void setInterestPayableClosing(BigDecimal interestPayableClosing) {
        this.interestPayableClosing = interestPayableClosing;
    }

    public Long getAmortizationScheduleId() {
        return amortizationScheduleId;
    }

    public void setAmortizationScheduleId(Long amortizationScheduleId) {
        this.amortizationScheduleId = amortizationScheduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityScheduleReportItemDTO)) {
            return false;
        }

        LeaseLiabilityScheduleReportItemDTO leaseLiabilityScheduleReportItemDTO = (LeaseLiabilityScheduleReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaseLiabilityScheduleReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityScheduleReportItemDTO{" +
            "id=" + getId() +
            ", sequenceNumber=" + getSequenceNumber() +
            ", openingBalance=" + getOpeningBalance() +
            ", cashPayment=" + getCashPayment() +
            ", principalPayment=" + getPrincipalPayment() +
            ", interestPayment=" + getInterestPayment() +
            ", outstandingBalance=" + getOutstandingBalance() +
            ", interestPayableOpening=" + getInterestPayableOpening() +
            ", interestAccrued=" + getInterestAccrued() +
            ", interestPayableClosing=" + getInterestPayableClosing() +
            ", amortizationScheduleId=" + getAmortizationScheduleId() +
            "}";
    }
}
