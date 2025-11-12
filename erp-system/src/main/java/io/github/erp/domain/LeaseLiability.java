package io.github.erp.domain;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A LeaseLiability.
 */
@Entity
@Table(name = "lease_liability")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaseliability-" + "#{ T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM')) }")
public class LeaseLiability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "lease_id", nullable = false, unique = true)
    @Field(type = FieldType.Text)
    private String leaseId;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "liability_amount", precision = 21, scale = 2, nullable = false)
    @Field(type = FieldType.Double)
    private BigDecimal liabilityAmount;

    @NotNull
    @Column(name = "start_date", nullable = false)
    @Field(type = FieldType.Date)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    @Field(type = FieldType.Date)
    private LocalDate endDate;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "interest_rate", precision = 21, scale = 15, nullable = false)
    @Field(type = FieldType.Double)
    private BigDecimal interestRate;

    @JsonIgnoreProperties(value = { "leaseLiability", "leaseContract" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private LeaseAmortizationCalculation leaseAmortizationCalculation;

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

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public LeaseLiability interestRate(BigDecimal interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", interestRate=" + getInterestRate() +
            "}";
    }
}
