package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
 * A PrepaymentAmortization.
 */
@Entity
@Table(name = "prepayment_amortization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prepaymentamortization")
public class PrepaymentAmortization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "prepayment_period")
    private LocalDate prepaymentPeriod;

    @Column(name = "prepayment_amount", precision = 21, scale = 2)
    private BigDecimal prepaymentAmount;

    @Column(name = "inactive")
    private Boolean inactive;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "settlementCurrency",
            "prepaymentTransaction",
            "serviceOutlet",
            "dealer",
            "debitAccount",
            "transferAccount",
            "placeholders",
            "generalParameters",
            "prepaymentParameters",
            "businessDocuments",
        },
        allowSetters = true
    )
    private PrepaymentAccount prepaymentAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount debitAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount creditAccount;

    @ManyToMany
    @JoinTable(
        name = "rel_prepayment_amortization__placeholder",
        joinColumns = @JoinColumn(name = "prepayment_amortization_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth fiscalMonth;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private PrepaymentCompilationRequest prepaymentCompilationRequest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrepaymentAmortization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public PrepaymentAmortization description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPrepaymentPeriod() {
        return this.prepaymentPeriod;
    }

    public PrepaymentAmortization prepaymentPeriod(LocalDate prepaymentPeriod) {
        this.setPrepaymentPeriod(prepaymentPeriod);
        return this;
    }

    public void setPrepaymentPeriod(LocalDate prepaymentPeriod) {
        this.prepaymentPeriod = prepaymentPeriod;
    }

    public BigDecimal getPrepaymentAmount() {
        return this.prepaymentAmount;
    }

    public PrepaymentAmortization prepaymentAmount(BigDecimal prepaymentAmount) {
        this.setPrepaymentAmount(prepaymentAmount);
        return this;
    }

    public void setPrepaymentAmount(BigDecimal prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public Boolean getInactive() {
        return this.inactive;
    }

    public PrepaymentAmortization inactive(Boolean inactive) {
        this.setInactive(inactive);
        return this;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public PrepaymentAccount getPrepaymentAccount() {
        return this.prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccount prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public PrepaymentAmortization prepaymentAccount(PrepaymentAccount prepaymentAccount) {
        this.setPrepaymentAccount(prepaymentAccount);
        return this;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public PrepaymentAmortization settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public TransactionAccount getDebitAccount() {
        return this.debitAccount;
    }

    public void setDebitAccount(TransactionAccount transactionAccount) {
        this.debitAccount = transactionAccount;
    }

    public PrepaymentAmortization debitAccount(TransactionAccount transactionAccount) {
        this.setDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getCreditAccount() {
        return this.creditAccount;
    }

    public void setCreditAccount(TransactionAccount transactionAccount) {
        this.creditAccount = transactionAccount;
    }

    public PrepaymentAmortization creditAccount(TransactionAccount transactionAccount) {
        this.setCreditAccount(transactionAccount);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PrepaymentAmortization placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PrepaymentAmortization addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PrepaymentAmortization removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public FiscalMonth getFiscalMonth() {
        return this.fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonth fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public PrepaymentAmortization fiscalMonth(FiscalMonth fiscalMonth) {
        this.setFiscalMonth(fiscalMonth);
        return this;
    }

    public PrepaymentCompilationRequest getPrepaymentCompilationRequest() {
        return this.prepaymentCompilationRequest;
    }

    public void setPrepaymentCompilationRequest(PrepaymentCompilationRequest prepaymentCompilationRequest) {
        this.prepaymentCompilationRequest = prepaymentCompilationRequest;
    }

    public PrepaymentAmortization prepaymentCompilationRequest(PrepaymentCompilationRequest prepaymentCompilationRequest) {
        this.setPrepaymentCompilationRequest(prepaymentCompilationRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentAmortization)) {
            return false;
        }
        return id != null && id.equals(((PrepaymentAmortization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAmortization{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", prepaymentPeriod='" + getPrepaymentPeriod() + "'" +
            ", prepaymentAmount=" + getPrepaymentAmount() +
            ", inactive='" + getInactive() + "'" +
            "}";
    }
}
