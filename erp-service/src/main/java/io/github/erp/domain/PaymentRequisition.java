package io.github.erp.domain;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A PaymentRequisition.
 */
@Entity
@Table(name = "payment_requisition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymentrequisition")
public class PaymentRequisition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "invoiced_amount", precision = 21, scale = 2)
    private BigDecimal invoicedAmount;

    @Column(name = "disbursement_cost", precision = 21, scale = 2)
    private BigDecimal disbursementCost;

    @Column(name = "vatable_amount", precision = 21, scale = 2)
    private BigDecimal vatableAmount;

    @OneToMany(mappedBy = "paymentRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Payment> requisitions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealerName() {
        return dealerName;
    }

    public PaymentRequisition dealerName(String dealerName) {
        this.dealerName = dealerName;
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public PaymentRequisition invoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
        return this;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getDisbursementCost() {
        return disbursementCost;
    }

    public PaymentRequisition disbursementCost(BigDecimal disbursementCost) {
        this.disbursementCost = disbursementCost;
        return this;
    }

    public void setDisbursementCost(BigDecimal disbursementCost) {
        this.disbursementCost = disbursementCost;
    }

    public BigDecimal getVatableAmount() {
        return vatableAmount;
    }

    public PaymentRequisition vatableAmount(BigDecimal vatableAmount) {
        this.vatableAmount = vatableAmount;
        return this;
    }

    public void setVatableAmount(BigDecimal vatableAmount) {
        this.vatableAmount = vatableAmount;
    }

    public Set<Payment> getRequisitions() {
        return requisitions;
    }

    public PaymentRequisition requisitions(Set<Payment> payments) {
        this.requisitions = payments;
        return this;
    }

    public PaymentRequisition addRequisition(Payment payment) {
        this.requisitions.add(payment);
        payment.setPaymentRequisition(this);
        return this;
    }

    public PaymentRequisition removeRequisition(Payment payment) {
        this.requisitions.remove(payment);
        payment.setPaymentRequisition(null);
        return this;
    }

    public void setRequisitions(Set<Payment> payments) {
        this.requisitions = payments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentRequisition)) {
            return false;
        }
        return id != null && id.equals(((PaymentRequisition) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentRequisition{" +
            "id=" + getId() +
            ", dealerName='" + getDealerName() + "'" +
            ", invoicedAmount=" + getInvoicedAmount() +
            ", disbursementCost=" + getDisbursementCost() +
            ", vatableAmount=" + getVatableAmount() +
            "}";
    }
}
