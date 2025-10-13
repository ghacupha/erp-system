package io.github.erp.domain;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaseLiabilityScheduleItem.
 */
@Entity
@Table(name = "lease_liability_schedule_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaseliabilityscheduleitem")
public class LeaseLiabilityScheduleItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Column(name = "opening_balance", precision = 21, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "cash_payment", precision = 21, scale = 2)
    private BigDecimal cashPayment;

    @Column(name = "principal_payment", precision = 21, scale = 2)
    private BigDecimal principalPayment;

    @Column(name = "interest_payment", precision = 21, scale = 2)
    private BigDecimal interestPayment;

    @Column(name = "outstanding_balance", precision = 21, scale = 2)
    private BigDecimal outstandingBalance;

    @Column(name = "interest_payable_opening", precision = 21, scale = 2)
    private BigDecimal interestPayableOpening;

    @Column(name = "interest_accrued", precision = 21, scale = 2)
    private BigDecimal interestAccrued;

    @Column(name = "interest_payable_closing", precision = 21, scale = 2)
    private BigDecimal interestPayableClosing;

    @ManyToMany
    @JoinTable(
        name = "rel_lease_liability_schedule_item__placeholder",
        joinColumns = @JoinColumn(name = "lease_liability_schedule_item_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_lease_liability_schedule_item__universally_unique_mapping",
        joinColumns = @JoinColumn(name = "lease_liability_schedule_item_id"),
        inverseJoinColumns = @JoinColumn(name = "universally_unique_mapping_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> universallyUniqueMappings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "leaseLiability", "leaseLiabilityScheduleItems", "leaseContract" }, allowSetters = true)
    private LeaseAmortizationSchedule leaseAmortizationSchedule;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "superintendentServiceOutlet",
            "mainDealer",
            "firstReportingPeriod",
            "lastReportingPeriod",
            "leaseContractDocument",
            "leaseContractCalculations",
            "leasePayments",
        },
        allowSetters = true
    )
    private IFRS16LeaseContract leaseContract;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "leaseAmortizationCalculation", "leaseContract" }, allowSetters = true)
    private LeaseLiability leaseLiability;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalMonth" }, allowSetters = true)
    private LeaseRepaymentPeriod leasePeriod;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseLiabilityScheduleItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public LeaseLiabilityScheduleItem sequenceNumber(Integer sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimal getOpeningBalance() {
        return this.openingBalance;
    }

    public LeaseLiabilityScheduleItem openingBalance(BigDecimal openingBalance) {
        this.setOpeningBalance(openingBalance);
        return this;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getCashPayment() {
        return this.cashPayment;
    }

    public LeaseLiabilityScheduleItem cashPayment(BigDecimal cashPayment) {
        this.setCashPayment(cashPayment);
        return this;
    }

    public void setCashPayment(BigDecimal cashPayment) {
        this.cashPayment = cashPayment;
    }

    public BigDecimal getPrincipalPayment() {
        return this.principalPayment;
    }

    public LeaseLiabilityScheduleItem principalPayment(BigDecimal principalPayment) {
        this.setPrincipalPayment(principalPayment);
        return this;
    }

    public void setPrincipalPayment(BigDecimal principalPayment) {
        this.principalPayment = principalPayment;
    }

    public BigDecimal getInterestPayment() {
        return this.interestPayment;
    }

    public LeaseLiabilityScheduleItem interestPayment(BigDecimal interestPayment) {
        this.setInterestPayment(interestPayment);
        return this;
    }

    public void setInterestPayment(BigDecimal interestPayment) {
        this.interestPayment = interestPayment;
    }

    public BigDecimal getOutstandingBalance() {
        return this.outstandingBalance;
    }

    public LeaseLiabilityScheduleItem outstandingBalance(BigDecimal outstandingBalance) {
        this.setOutstandingBalance(outstandingBalance);
        return this;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public BigDecimal getInterestPayableOpening() {
        return this.interestPayableOpening;
    }

    public LeaseLiabilityScheduleItem interestPayableOpening(BigDecimal interestPayableOpening) {
        this.setInterestPayableOpening(interestPayableOpening);
        return this;
    }

    public void setInterestPayableOpening(BigDecimal interestPayableOpening) {
        this.interestPayableOpening = interestPayableOpening;
    }

    public BigDecimal getInterestAccrued() {
        return this.interestAccrued;
    }

    public LeaseLiabilityScheduleItem interestAccrued(BigDecimal interestAccrued) {
        this.setInterestAccrued(interestAccrued);
        return this;
    }

    public void setInterestAccrued(BigDecimal interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public BigDecimal getInterestPayableClosing() {
        return this.interestPayableClosing;
    }

    public LeaseLiabilityScheduleItem interestPayableClosing(BigDecimal interestPayableClosing) {
        this.setInterestPayableClosing(interestPayableClosing);
        return this;
    }

    public void setInterestPayableClosing(BigDecimal interestPayableClosing) {
        this.interestPayableClosing = interestPayableClosing;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public LeaseLiabilityScheduleItem placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public LeaseLiabilityScheduleItem addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public LeaseLiabilityScheduleItem removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getUniversallyUniqueMappings() {
        return this.universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    public LeaseLiabilityScheduleItem universallyUniqueMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setUniversallyUniqueMappings(universallyUniqueMappings);
        return this;
    }

    public LeaseLiabilityScheduleItem addUniversallyUniqueMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.universallyUniqueMappings.add(universallyUniqueMapping);
        return this;
    }

    public LeaseLiabilityScheduleItem removeUniversallyUniqueMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.universallyUniqueMappings.remove(universallyUniqueMapping);
        return this;
    }

    public LeaseAmortizationSchedule getLeaseAmortizationSchedule() {
        return this.leaseAmortizationSchedule;
    }

    public void setLeaseAmortizationSchedule(LeaseAmortizationSchedule leaseAmortizationSchedule) {
        this.leaseAmortizationSchedule = leaseAmortizationSchedule;
    }

    public LeaseLiabilityScheduleItem leaseAmortizationSchedule(LeaseAmortizationSchedule leaseAmortizationSchedule) {
        this.setLeaseAmortizationSchedule(leaseAmortizationSchedule);
        return this;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return this.leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
    }

    public LeaseLiabilityScheduleItem leaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.setLeaseContract(iFRS16LeaseContract);
        return this;
    }

    public LeaseLiability getLeaseLiability() {
        return this.leaseLiability;
    }

    public void setLeaseLiability(LeaseLiability leaseLiability) {
        this.leaseLiability = leaseLiability;
    }

    public LeaseLiabilityScheduleItem leaseLiability(LeaseLiability leaseLiability) {
        this.setLeaseLiability(leaseLiability);
        return this;
    }

    public LeaseRepaymentPeriod getLeasePeriod() {
        return this.leasePeriod;
    }

    public void setLeasePeriod(LeaseRepaymentPeriod leaseRepaymentPeriod) {
        this.leasePeriod = leaseRepaymentPeriod;
    }

    public LeaseLiabilityScheduleItem leasePeriod(LeaseRepaymentPeriod leaseRepaymentPeriod) {
        this.setLeasePeriod(leaseRepaymentPeriod);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityScheduleItem)) {
            return false;
        }
        return id != null && id.equals(((LeaseLiabilityScheduleItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityScheduleItem{" +
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
            "}";
    }
}
