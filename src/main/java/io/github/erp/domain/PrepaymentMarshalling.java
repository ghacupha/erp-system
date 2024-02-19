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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PrepaymentMarshalling.
 */
@Entity
@Table(name = "prepayment_marshalling")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prepaymentmarshalling")
public class PrepaymentMarshalling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "inactive")
    private Boolean inactive;

    @Column(name = "amortization_periods")
    private Integer amortizationPeriods;

    @Column(name = "processed")
    private Boolean processed;

    @Column(name = "compilation_token")
    private UUID compilationToken;

    @ManyToOne(optional = false)
    @NotNull
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

    @ManyToMany
    @JoinTable(
        name = "rel_prepayment_marshalling__placeholder",
        joinColumns = @JoinColumn(name = "prepayment_marshalling_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth firstFiscalMonth;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth lastFiscalMonth;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrepaymentMarshalling id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInactive() {
        return this.inactive;
    }

    public PrepaymentMarshalling inactive(Boolean inactive) {
        this.setInactive(inactive);
        return this;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Integer getAmortizationPeriods() {
        return this.amortizationPeriods;
    }

    public PrepaymentMarshalling amortizationPeriods(Integer amortizationPeriods) {
        this.setAmortizationPeriods(amortizationPeriods);
        return this;
    }

    public void setAmortizationPeriods(Integer amortizationPeriods) {
        this.amortizationPeriods = amortizationPeriods;
    }

    public Boolean getProcessed() {
        return this.processed;
    }

    public PrepaymentMarshalling processed(Boolean processed) {
        this.setProcessed(processed);
        return this;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public UUID getCompilationToken() {
        return this.compilationToken;
    }

    public PrepaymentMarshalling compilationToken(UUID compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(UUID compilationToken) {
        this.compilationToken = compilationToken;
    }

    public PrepaymentAccount getPrepaymentAccount() {
        return this.prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccount prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public PrepaymentMarshalling prepaymentAccount(PrepaymentAccount prepaymentAccount) {
        this.setPrepaymentAccount(prepaymentAccount);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PrepaymentMarshalling placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PrepaymentMarshalling addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PrepaymentMarshalling removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public FiscalMonth getFirstFiscalMonth() {
        return this.firstFiscalMonth;
    }

    public void setFirstFiscalMonth(FiscalMonth fiscalMonth) {
        this.firstFiscalMonth = fiscalMonth;
    }

    public PrepaymentMarshalling firstFiscalMonth(FiscalMonth fiscalMonth) {
        this.setFirstFiscalMonth(fiscalMonth);
        return this;
    }

    public FiscalMonth getLastFiscalMonth() {
        return this.lastFiscalMonth;
    }

    public void setLastFiscalMonth(FiscalMonth fiscalMonth) {
        this.lastFiscalMonth = fiscalMonth;
    }

    public PrepaymentMarshalling lastFiscalMonth(FiscalMonth fiscalMonth) {
        this.setLastFiscalMonth(fiscalMonth);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentMarshalling)) {
            return false;
        }
        return id != null && id.equals(((PrepaymentMarshalling) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentMarshalling{" +
            "id=" + getId() +
            ", inactive='" + getInactive() + "'" +
            ", amortizationPeriods=" + getAmortizationPeriods() +
            ", processed='" + getProcessed() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
