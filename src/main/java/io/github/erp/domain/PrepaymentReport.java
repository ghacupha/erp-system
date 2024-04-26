package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PrepaymentReport.
 */
@Entity
@Table(name = "prepayment_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prepaymentreport")
public class PrepaymentReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "catalogue_number")
    private String catalogueNumber;

    @Column(name = "particulars")
    private String particulars;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "prepayment_amount", precision = 21, scale = 2)
    private BigDecimal prepaymentAmount;

    @Column(name = "amortised_amount", precision = 21, scale = 2)
    private BigDecimal amortisedAmount;

    @Column(name = "outstanding_amount", precision = 21, scale = 2)
    private BigDecimal outstandingAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrepaymentReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogueNumber() {
        return this.catalogueNumber;
    }

    public PrepaymentReport catalogueNumber(String catalogueNumber) {
        this.setCatalogueNumber(catalogueNumber);
        return this;
    }

    public void setCatalogueNumber(String catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public String getParticulars() {
        return this.particulars;
    }

    public PrepaymentReport particulars(String particulars) {
        this.setParticulars(particulars);
        return this;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public PrepaymentReport dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPaymentNumber() {
        return this.paymentNumber;
    }

    public PrepaymentReport paymentNumber(String paymentNumber) {
        this.setPaymentNumber(paymentNumber);
        return this;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public PrepaymentReport paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public PrepaymentReport currencyCode(String currencyCode) {
        this.setCurrencyCode(currencyCode);
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPrepaymentAmount() {
        return this.prepaymentAmount;
    }

    public PrepaymentReport prepaymentAmount(BigDecimal prepaymentAmount) {
        this.setPrepaymentAmount(prepaymentAmount);
        return this;
    }

    public void setPrepaymentAmount(BigDecimal prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public BigDecimal getAmortisedAmount() {
        return this.amortisedAmount;
    }

    public PrepaymentReport amortisedAmount(BigDecimal amortisedAmount) {
        this.setAmortisedAmount(amortisedAmount);
        return this;
    }

    public void setAmortisedAmount(BigDecimal amortisedAmount) {
        this.amortisedAmount = amortisedAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return this.outstandingAmount;
    }

    public PrepaymentReport outstandingAmount(BigDecimal outstandingAmount) {
        this.setOutstandingAmount(outstandingAmount);
        return this;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentReport)) {
            return false;
        }
        return id != null && id.equals(((PrepaymentReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentReport{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", prepaymentAmount=" + getPrepaymentAmount() +
            ", amortisedAmount=" + getAmortisedAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            "}";
    }
}
