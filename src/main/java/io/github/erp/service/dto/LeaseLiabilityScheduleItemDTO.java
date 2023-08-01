package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    private Boolean periodIncluded;

    private LocalDate periodStartDate;

    private LocalDate periodEndDate;

    private BigDecimal openingBalance;

    private BigDecimal cashPayment;

    private BigDecimal principalPayment;

    private BigDecimal interestPayment;

    private BigDecimal outstandingBalance;

    private BigDecimal interestPayableOpening;

    private BigDecimal interestExpenseAccrued;

    private BigDecimal interestPayableBalance;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private LeaseContractDTO leaseContract;

    private LeaseModelMetadataDTO leaseModelMetadata;

    private Set<UniversallyUniqueMappingDTO> universallyUniqueMappings = new HashSet<>();

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

    public Boolean getPeriodIncluded() {
        return periodIncluded;
    }

    public void setPeriodIncluded(Boolean periodIncluded) {
        this.periodIncluded = periodIncluded;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
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

    public BigDecimal getInterestExpenseAccrued() {
        return interestExpenseAccrued;
    }

    public void setInterestExpenseAccrued(BigDecimal interestExpenseAccrued) {
        this.interestExpenseAccrued = interestExpenseAccrued;
    }

    public BigDecimal getInterestPayableBalance() {
        return interestPayableBalance;
    }

    public void setInterestPayableBalance(BigDecimal interestPayableBalance) {
        this.interestPayableBalance = interestPayableBalance;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    public LeaseModelMetadataDTO getLeaseModelMetadata() {
        return leaseModelMetadata;
    }

    public void setLeaseModelMetadata(LeaseModelMetadataDTO leaseModelMetadata) {
        this.leaseModelMetadata = leaseModelMetadata;
    }

    public Set<UniversallyUniqueMappingDTO> getUniversallyUniqueMappings() {
        return universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMappingDTO> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
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
            ", periodIncluded='" + getPeriodIncluded() + "'" +
            ", periodStartDate='" + getPeriodStartDate() + "'" +
            ", periodEndDate='" + getPeriodEndDate() + "'" +
            ", openingBalance=" + getOpeningBalance() +
            ", cashPayment=" + getCashPayment() +
            ", principalPayment=" + getPrincipalPayment() +
            ", interestPayment=" + getInterestPayment() +
            ", outstandingBalance=" + getOutstandingBalance() +
            ", interestPayableOpening=" + getInterestPayableOpening() +
            ", interestExpenseAccrued=" + getInterestExpenseAccrued() +
            ", interestPayableBalance=" + getInterestPayableBalance() +
            ", placeholders=" + getPlaceholders() +
            ", leaseContract=" + getLeaseContract() +
            ", leaseModelMetadata=" + getLeaseModelMetadata() +
            ", universallyUniqueMappings=" + getUniversallyUniqueMappings() +
            "}";
    }
}
