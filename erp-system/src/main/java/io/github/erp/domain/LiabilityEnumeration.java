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
import io.github.erp.domain.enumeration.LiabilityTimeGranularity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * LiabilityEnumeration captures configuration for a batch of present value calculations.
 */
@Entity
@Table(name = "liability_enumeration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "liabilityenumeration")
public class LiabilityEnumeration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "interest_rate", precision = 21, scale = 15, nullable = false)
    @Field(type = FieldType.Double)
    private BigDecimal interestRate;

    @Column(name = "interest_rate_text")
    @Field(type = FieldType.Text)
    private String interestRateText;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "time_granularity", nullable = false)
    @Field(type = FieldType.Keyword)
    private LiabilityTimeGranularity timeGranularity;

    @Column(name = "active")
    @Field(type = FieldType.Boolean)
    private Boolean active = Boolean.TRUE;

    @NotNull
    @Column(name = "request_date_time", nullable = false)
    @Field(type = FieldType.Date)
    private ZonedDateTime requestDateTime;

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
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "ifrs16lease_contract_id", nullable = false)
    private IFRS16LeaseContract leaseContract;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "leaseContract", "csvFileUpload", "leasePayments" }, allowSetters = true)
    private LeasePaymentUpload leasePaymentUpload;

    @OneToMany(mappedBy = "liabilityEnumeration")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "leaseContract", "liabilityEnumeration" }, allowSetters = true)
    private Set<PresentValueEnumeration> presentValueEnumerations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LiabilityEnumeration id(Long id) {
        this.setId(id);
        return this;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LiabilityEnumeration interestRate(BigDecimal interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public String getInterestRateText() {
        return interestRateText;
    }

    public void setInterestRateText(String interestRateText) {
        this.interestRateText = interestRateText;
    }

    public LiabilityEnumeration interestRateText(String interestRateText) {
        this.setInterestRateText(interestRateText);
        return this;
    }

    public LiabilityTimeGranularity getTimeGranularity() {
        return timeGranularity;
    }

    public void setTimeGranularity(LiabilityTimeGranularity timeGranularity) {
        this.timeGranularity = timeGranularity;
    }

    public LiabilityEnumeration timeGranularity(LiabilityTimeGranularity timeGranularity) {
        this.setTimeGranularity(timeGranularity);
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LiabilityEnumeration active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public ZonedDateTime getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(ZonedDateTime requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public LiabilityEnumeration requestDateTime(ZonedDateTime requestDateTime) {
        this.setRequestDateTime(requestDateTime);
        return this;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract leaseContract) {
        this.leaseContract = leaseContract;
    }

    public LiabilityEnumeration leaseContract(IFRS16LeaseContract leaseContract) {
        this.setLeaseContract(leaseContract);
        return this;
    }

    public LeasePaymentUpload getLeasePaymentUpload() {
        return leasePaymentUpload;
    }

    public void setLeasePaymentUpload(LeasePaymentUpload leasePaymentUpload) {
        this.leasePaymentUpload = leasePaymentUpload;
    }

    public LiabilityEnumeration leasePaymentUpload(LeasePaymentUpload leasePaymentUpload) {
        this.setLeasePaymentUpload(leasePaymentUpload);
        return this;
    }

    public Set<PresentValueEnumeration> getPresentValueEnumerations() {
        return presentValueEnumerations;
    }

    public void setPresentValueEnumerations(Set<PresentValueEnumeration> presentValueEnumerations) {
        if (this.presentValueEnumerations != null) {
            this.presentValueEnumerations.forEach(i -> i.setLiabilityEnumeration(null));
        }
        if (presentValueEnumerations != null) {
            presentValueEnumerations.forEach(i -> i.setLiabilityEnumeration(this));
        }
        this.presentValueEnumerations = presentValueEnumerations;
    }

    public LiabilityEnumeration presentValueEnumerations(Set<PresentValueEnumeration> presentValueEnumerations) {
        this.setPresentValueEnumerations(presentValueEnumerations);
        return this;
    }

    public LiabilityEnumeration addPresentValueEnumeration(PresentValueEnumeration presentValueEnumeration) {
        this.presentValueEnumerations.add(presentValueEnumeration);
        presentValueEnumeration.setLiabilityEnumeration(this);
        return this;
    }

    public LiabilityEnumeration removePresentValueEnumeration(PresentValueEnumeration presentValueEnumeration) {
        this.presentValueEnumerations.remove(presentValueEnumeration);
        presentValueEnumeration.setLiabilityEnumeration(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LiabilityEnumeration)) {
            return false;
        }
        return id != null && id.equals(((LiabilityEnumeration) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "LiabilityEnumeration{" +
            "id=" + getId() +
            ", interestRate=" + getInterestRate() +
            ", interestRateText='" + getInterestRateText() + "'" +
            ", timeGranularity=" + getTimeGranularity() +
            ", active=" + getActive() +
            ", requestDateTime=" + getRequestDateTime() +
            "}";
    }
}
