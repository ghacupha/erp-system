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
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A PresentValueEnumeration row produced from lease payments.
 */
@Entity
@Table(name = "present_value_enumeration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "presentvalueenumeration")
public class PresentValueEnumeration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @Column(name = "sequence_number")
    @Field(type = FieldType.Integer)
    private Integer sequenceNumber;

    @Column(name = "payment_date")
    @Field(type = FieldType.Date)
    private LocalDate paymentDate;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    @Field(type = FieldType.Double)
    private BigDecimal paymentAmount;

    @Column(name = "discount_rate", precision = 21, scale = 15)
    @Field(type = FieldType.Double)
    private BigDecimal discountRate;

    @Column(name = "present_value", precision = 21, scale = 2)
    @Field(type = FieldType.Double)
    private BigDecimal presentValue;

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
    @JsonIgnoreProperties(value = { "leaseContract", "leasePaymentUpload", "presentValueEnumerations" }, allowSetters = true)
    private LiabilityEnumeration liabilityEnumeration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PresentValueEnumeration id(Long id) {
        this.setId(id);
        return this;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public PresentValueEnumeration sequenceNumber(Integer sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PresentValueEnumeration paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PresentValueEnumeration paymentAmount(BigDecimal paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public PresentValueEnumeration discountRate(BigDecimal discountRate) {
        this.setDiscountRate(discountRate);
        return this;
    }

    public BigDecimal getPresentValue() {
        return presentValue;
    }

    public void setPresentValue(BigDecimal presentValue) {
        this.presentValue = presentValue;
    }

    public PresentValueEnumeration presentValue(BigDecimal presentValue) {
        this.setPresentValue(presentValue);
        return this;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract leaseContract) {
        this.leaseContract = leaseContract;
    }

    public PresentValueEnumeration leaseContract(IFRS16LeaseContract leaseContract) {
        this.setLeaseContract(leaseContract);
        return this;
    }

    public LiabilityEnumeration getLiabilityEnumeration() {
        return liabilityEnumeration;
    }

    public void setLiabilityEnumeration(LiabilityEnumeration liabilityEnumeration) {
        this.liabilityEnumeration = liabilityEnumeration;
    }

    public PresentValueEnumeration liabilityEnumeration(LiabilityEnumeration liabilityEnumeration) {
        this.setLiabilityEnumeration(liabilityEnumeration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PresentValueEnumeration)) {
            return false;
        }
        return id != null && id.equals(((PresentValueEnumeration) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PresentValueEnumeration{" +
            "id=" + getId() +
            ", sequenceNumber=" + getSequenceNumber() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", discountRate=" + getDiscountRate() +
            ", presentValue=" + getPresentValue() +
            "}";
    }
}
