package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LeaseLiabilityScheduleItem} entity.
 */
public class LeaseLiabilityScheduleItemDTO implements Serializable {

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

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> universallyUniqueMappings = new HashSet<>();

    private LeasePeriodDTO leasePeriod;

    private LeaseAmortizationScheduleDTO leaseAmortizationSchedule;

    private IFRS16LeaseContractDTO leaseContract;

    private LeaseLiabilityDTO leaseLiability;

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

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getUniversallyUniqueMappings() {
        return universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMappingDTO> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    public LeasePeriodDTO getLeasePeriod() {
        return leasePeriod;
    }

    public void setLeasePeriod(LeasePeriodDTO leasePeriod) {
        this.leasePeriod = leasePeriod;
    }

    public LeaseAmortizationScheduleDTO getLeaseAmortizationSchedule() {
        return leaseAmortizationSchedule;
    }

    public void setLeaseAmortizationSchedule(LeaseAmortizationScheduleDTO leaseAmortizationSchedule) {
        this.leaseAmortizationSchedule = leaseAmortizationSchedule;
    }

    public IFRS16LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    public LeaseLiabilityDTO getLeaseLiability() {
        return leaseLiability;
    }

    public void setLeaseLiability(LeaseLiabilityDTO leaseLiability) {
        this.leaseLiability = leaseLiability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityScheduleItemDTO)) {
            return false;
        }

        LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO = (LeaseLiabilityScheduleItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaseLiabilityScheduleItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityScheduleItemDTO{" +
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
            ", placeholders=" + getPlaceholders() +
            ", universallyUniqueMappings=" + getUniversallyUniqueMappings() +
            ", leasePeriod=" + getLeasePeriod() +
            ", leaseAmortizationSchedule=" + getLeaseAmortizationSchedule() +
            ", leaseContract=" + getLeaseContract() +
            ", leaseLiability=" + getLeaseLiability() +
            "}";
    }
}
