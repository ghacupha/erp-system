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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A LeaseAmortizationCalculation.
 */
@Entity
@Table(name = "lease_amortization_calculation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaseamortizationcalculation-" + "#{ T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM')) }")
public class LeaseAmortizationCalculation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "interest_rate", precision = 21, scale = 15, nullable = false)
    @Field(type = FieldType.Double)
    private BigDecimal interestRate;

    @Column(name = "periodicity")
    @Field(type = FieldType.Text)
    private String periodicity;

    @Column(name = "lease_amount", precision = 21, scale = 2)
    @Field(type = FieldType.Double)
    private BigDecimal leaseAmount;

    @Column(name = "number_of_periods")
    @Field(type = FieldType.Integer)
    private Integer numberOfPeriods;

    @JsonIgnoreProperties(value = { "leaseAmortizationCalculation", "leasePayments", "leaseContract" }, allowSetters = true)
    @OneToOne(mappedBy = "leaseAmortizationCalculation")
    private LeaseLiability leaseLiability;

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

    public LeaseAmortizationCalculation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriodicity() {
        return this.periodicity;
    }

    public LeaseAmortizationCalculation periodicity(String periodicity) {
        this.setPeriodicity(periodicity);
        return this;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public BigDecimal getLeaseAmount() {
        return this.leaseAmount;
    }

    public LeaseAmortizationCalculation leaseAmount(BigDecimal leaseAmount) {
        this.setLeaseAmount(leaseAmount);
        return this;
    }

    public void setLeaseAmount(BigDecimal leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public Integer getNumberOfPeriods() {
        return this.numberOfPeriods;
    }

    public LeaseAmortizationCalculation numberOfPeriods(Integer numberOfPeriods) {
        this.setNumberOfPeriods(numberOfPeriods);
        return this;
    }

    public void setNumberOfPeriods(Integer numberOfPeriods) {
        this.numberOfPeriods = numberOfPeriods;
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public LeaseAmortizationCalculation interestRate(BigDecimal interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LeaseLiability getLeaseLiability() {
        return this.leaseLiability;
    }

    public void setLeaseLiability(LeaseLiability leaseLiability) {
        if (this.leaseLiability != null) {
            this.leaseLiability.setLeaseAmortizationCalculation(null);
        }
        if (leaseLiability != null) {
            leaseLiability.setLeaseAmortizationCalculation(this);
        }
        this.leaseLiability = leaseLiability;
    }

    public LeaseAmortizationCalculation leaseLiability(LeaseLiability leaseLiability) {
        this.setLeaseLiability(leaseLiability);
        return this;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return this.leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
    }

    public LeaseAmortizationCalculation leaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.setLeaseContract(iFRS16LeaseContract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseAmortizationCalculation)) {
            return false;
        }
        return id != null && id.equals(((LeaseAmortizationCalculation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseAmortizationCalculation{" +
            "id=" + getId() +
            ", periodicity='" + getPeriodicity() + "'" +
            ", leaseAmount=" + getLeaseAmount() +
            ", numberOfPeriods=" + getNumberOfPeriods() +
            ", interestRate=" + getInterestRate() +
            "}";
    }
}
