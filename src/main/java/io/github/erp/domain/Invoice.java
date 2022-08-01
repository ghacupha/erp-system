package io.github.erp.domain;

/*-
 * Erp System - Mark II No 22 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.CurrencyTypes;
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
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "invoice_amount", precision = 21, scale = 2)
    private BigDecimal invoiceAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyTypes currency;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_invoice__payment_label",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_invoice__placeholder",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public Invoice invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public Invoice invoiceDate(LocalDate invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceAmount() {
        return this.invoiceAmount;
    }

    public Invoice invoiceAmount(BigDecimal invoiceAmount) {
        this.setInvoiceAmount(invoiceAmount);
        return this;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public CurrencyTypes getCurrency() {
        return this.currency;
    }

    public Invoice currency(CurrencyTypes currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(CurrencyTypes currency) {
        this.currency = currency;
    }

    public String getPaymentReference() {
        return this.paymentReference;
    }

    public Invoice paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public Invoice dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public Invoice fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public Invoice compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Invoice paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public Invoice addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        return this;
    }

    public Invoice removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Invoice placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public Invoice addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public Invoice removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceAmount=" + getInvoiceAmount() +
            ", currency='" + getCurrency() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
