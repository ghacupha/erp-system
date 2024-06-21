package io.github.erp.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaseLiability.
 */
@Entity
@Table(name = "lease_liability")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaseliability")
public class LeaseLiability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "lease_id", nullable = false, unique = true)
    private String leaseId;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "liability_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal liabilityAmount;

    @NotNull
    @Column(name = "interest_rate", nullable = false)
    private Float interestRate;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @JsonIgnoreProperties(value = { "leaseLiability", "leaseContract" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private LeaseAmortizationCalculation leaseAmortizationCalculation;

    @OneToMany(mappedBy = "leaseLiability")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "leaseLiability" }, allowSetters = true)
    private Set<LeasePayment> leasePayments = new HashSet<>();

    @JsonIgnoreProperties(
        value = {
            "superintendentServiceOutlet",
            "mainDealer",
            "firstReportingPeriod",
            "lastReportingPeriod",
            "leaseContractDocument",
            "leaseContractCalculations",
        },
        allowSetters = true
    )
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private IFRS16LeaseContract leaseContract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseLiability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaseId() {
        return this.leaseId;
    }

    public LeaseLiability leaseId(String leaseId) {
        this.setLeaseId(leaseId);
        return this;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }

    public BigDecimal getLiabilityAmount() {
        return this.liabilityAmount;
    }

    public LeaseLiability liabilityAmount(BigDecimal liabilityAmount) {
        this.setLiabilityAmount(liabilityAmount);
        return this;
    }

    public void setLiabilityAmount(BigDecimal liabilityAmount) {
        this.liabilityAmount = liabilityAmount;
    }

    public Float getInterestRate() {
        return this.interestRate;
    }

    public LeaseLiability interestRate(Float interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public void setInterestRate(Float interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LeaseLiability startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public LeaseLiability endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LeaseAmortizationCalculation getLeaseAmortizationCalculation() {
        return this.leaseAmortizationCalculation;
    }

    public void setLeaseAmortizationCalculation(LeaseAmortizationCalculation leaseAmortizationCalculation) {
        this.leaseAmortizationCalculation = leaseAmortizationCalculation;
    }

    public LeaseLiability leaseAmortizationCalculation(LeaseAmortizationCalculation leaseAmortizationCalculation) {
        this.setLeaseAmortizationCalculation(leaseAmortizationCalculation);
        return this;
    }

    public Set<LeasePayment> getLeasePayments() {
        return this.leasePayments;
    }

    public void setLeasePayments(Set<LeasePayment> leasePayments) {
        if (this.leasePayments != null) {
            this.leasePayments.forEach(i -> i.setLeaseLiability(null));
        }
        if (leasePayments != null) {
            leasePayments.forEach(i -> i.setLeaseLiability(this));
        }
        this.leasePayments = leasePayments;
    }

    public LeaseLiability leasePayments(Set<LeasePayment> leasePayments) {
        this.setLeasePayments(leasePayments);
        return this;
    }

    public LeaseLiability addLeasePayment(LeasePayment leasePayment) {
        this.leasePayments.add(leasePayment);
        leasePayment.setLeaseLiability(this);
        return this;
    }

    public LeaseLiability removeLeasePayment(LeasePayment leasePayment) {
        this.leasePayments.remove(leasePayment);
        leasePayment.setLeaseLiability(null);
        return this;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return this.leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
    }

    public LeaseLiability leaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.setLeaseContract(iFRS16LeaseContract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiability)) {
            return false;
        }
        return id != null && id.equals(((LeaseLiability) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiability{" +
            "id=" + getId() +
            ", leaseId='" + getLeaseId() + "'" +
            ", liabilityAmount=" + getLiabilityAmount() +
            ", interestRate=" + getInterestRate() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
