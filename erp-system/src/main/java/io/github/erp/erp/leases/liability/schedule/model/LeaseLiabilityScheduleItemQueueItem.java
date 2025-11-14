package io.github.erp.erp.leases.liability.schedule.model;

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
import java.util.UUID;

/**
 * Queue payload representing a single lease liability schedule row.
 */
public class LeaseLiabilityScheduleItemQueueItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID uuid = UUID.randomUUID();
    private Integer sequenceNumber;
    private BigDecimal openingBalance;
    private BigDecimal cashPayment;
    private BigDecimal principalPayment;
    private BigDecimal interestPayment;
    private BigDecimal outstandingBalance;
    private BigDecimal interestPayableOpening;
    private BigDecimal interestAccrued;
    private BigDecimal interestPayableClosing;
    private Boolean active = Boolean.TRUE;
    private Long leasePeriodId;
    private Long leaseLiabilityId;
    private Long leaseAmortizationScheduleId;
    private Long leaseLiabilityCompilationId;
    private Long uploadId;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getLeasePeriodId() {
        return leasePeriodId;
    }

    public void setLeasePeriodId(Long leasePeriodId) {
        this.leasePeriodId = leasePeriodId;
    }

    public Long getLeaseLiabilityId() {
        return leaseLiabilityId;
    }

    public void setLeaseLiabilityId(Long leaseLiabilityId) {
        this.leaseLiabilityId = leaseLiabilityId;
    }

    public Long getLeaseAmortizationScheduleId() {
        return leaseAmortizationScheduleId;
    }

    public void setLeaseAmortizationScheduleId(Long leaseAmortizationScheduleId) {
        this.leaseAmortizationScheduleId = leaseAmortizationScheduleId;
    }

    public Long getLeaseLiabilityCompilationId() {
        return leaseLiabilityCompilationId;
    }

    public void setLeaseLiabilityCompilationId(Long leaseLiabilityCompilationId) {
        this.leaseLiabilityCompilationId = leaseLiabilityCompilationId;
    }

    public Long getUploadId() {
        return uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }
}

