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
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LeaseLiability} entity.
 */
public class LeaseLiabilityDTO implements Serializable {

    private Long id;

    @NotNull
    private String leaseId;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal liabilityAmount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal interestRate;

    @NotNull
    private Boolean hasBeenFullyAmortised = Boolean.FALSE;

    private LeaseAmortizationCalculationDTO leaseAmortizationCalculation;

    private IFRS16LeaseContractDTO leaseContract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }

    public BigDecimal getLiabilityAmount() {
        return liabilityAmount;
    }

    public void setLiabilityAmount(BigDecimal liabilityAmount) {
        this.liabilityAmount = liabilityAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Boolean getHasBeenFullyAmortised() {
        return hasBeenFullyAmortised;
    }

    public void setHasBeenFullyAmortised(Boolean hasBeenFullyAmortised) {
        this.hasBeenFullyAmortised = hasBeenFullyAmortised != null ? hasBeenFullyAmortised : Boolean.FALSE;
    }

    public LeaseAmortizationCalculationDTO getLeaseAmortizationCalculation() {
        return leaseAmortizationCalculation;
    }

    public void setLeaseAmortizationCalculation(LeaseAmortizationCalculationDTO leaseAmortizationCalculation) {
        this.leaseAmortizationCalculation = leaseAmortizationCalculation;
    }

    public IFRS16LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityDTO)) {
            return false;
        }

        LeaseLiabilityDTO leaseLiabilityDTO = (LeaseLiabilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaseLiabilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityDTO{" +
            "id=" + getId() +
            ", leaseId='" + getLeaseId() + "'" +
            ", liabilityAmount=" + getLiabilityAmount() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", interestRate=" + getInterestRate() +
            ", hasBeenFullyAmortised='" + getHasBeenFullyAmortised() + "'" +
            ", leaseAmortizationCalculation=" + getLeaseAmortizationCalculation() +
            ", leaseContract=" + getLeaseContract() +
            "}";
    }
}
