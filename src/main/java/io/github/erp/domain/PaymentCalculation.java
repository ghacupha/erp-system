package io.github.erp.domain;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentCalculation.
 */
@Entity
@Table(name = "payment_calculation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paymentcalculation")
public class PaymentCalculation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_expense", precision = 21, scale = 2)
    private BigDecimal paymentExpense;

    @Column(name = "withholding_vat", precision = 21, scale = 2)
    private BigDecimal withholdingVAT;

    @Column(name = "withholding_tax", precision = 21, scale = 2)
    private BigDecimal withholdingTax;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_calculation__payment_label",
        joinColumns = @JoinColumn(name = "payment_calculation_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "paymentCalculations", "placeholders" }, allowSetters = true)
    private PaymentCategory paymentCategory;

    @ManyToMany
    @JoinTable(
        name = "rel_payment_calculation__placeholder",
        joinColumns = @JoinColumn(name = "payment_calculation_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentCalculation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPaymentExpense() {
        return this.paymentExpense;
    }

    public PaymentCalculation paymentExpense(BigDecimal paymentExpense) {
        this.setPaymentExpense(paymentExpense);
        return this;
    }

    public void setPaymentExpense(BigDecimal paymentExpense) {
        this.paymentExpense = paymentExpense;
    }

    public BigDecimal getWithholdingVAT() {
        return this.withholdingVAT;
    }

    public PaymentCalculation withholdingVAT(BigDecimal withholdingVAT) {
        this.setWithholdingVAT(withholdingVAT);
        return this;
    }

    public void setWithholdingVAT(BigDecimal withholdingVAT) {
        this.withholdingVAT = withholdingVAT;
    }

    public BigDecimal getWithholdingTax() {
        return this.withholdingTax;
    }

    public PaymentCalculation withholdingTax(BigDecimal withholdingTax) {
        this.setWithholdingTax(withholdingTax);
        return this;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public PaymentCalculation paymentAmount(BigDecimal paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public PaymentCalculation fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public PaymentCalculation compilationToken(String compilationToken) {
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

    public PaymentCalculation paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public PaymentCalculation addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        return this;
    }

    public PaymentCalculation removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        return this;
    }

    public PaymentCategory getPaymentCategory() {
        return this.paymentCategory;
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public PaymentCalculation paymentCategory(PaymentCategory paymentCategory) {
        this.setPaymentCategory(paymentCategory);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PaymentCalculation placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PaymentCalculation addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PaymentCalculation removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentCalculation)) {
            return false;
        }
        return id != null && id.equals(((PaymentCalculation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCalculation{" +
            "id=" + getId() +
            ", paymentExpense=" + getPaymentExpense() +
            ", withholdingVAT=" + getWithholdingVAT() +
            ", withholdingTax=" + getWithholdingTax() +
            ", paymentAmount=" + getPaymentAmount() +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
