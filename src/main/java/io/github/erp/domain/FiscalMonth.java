package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FiscalMonth.
 */
@Entity
@Table(name = "fiscal_month")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fiscalmonth")
public class FiscalMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "month_number", nullable = false)
    private Integer monthNumber;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "fiscal_month_code", nullable = false, unique = true)
    private String fiscalMonthCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "universallyUniqueMappings", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private FiscalYear fiscalYear;

    @ManyToMany
    @JoinTable(
        name = "rel_fiscal_month__placeholder",
        joinColumns = @JoinColumn(name = "fiscal_month_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_fiscal_month__universally_unique_mapping",
        joinColumns = @JoinColumn(name = "fiscal_month_id"),
        inverseJoinColumns = @JoinColumn(name = "universally_unique_mapping_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> universallyUniqueMappings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings" }, allowSetters = true)
    private FiscalQuarter fiscalQuarter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FiscalMonth id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMonthNumber() {
        return this.monthNumber;
    }

    public FiscalMonth monthNumber(Integer monthNumber) {
        this.setMonthNumber(monthNumber);
        return this;
    }

    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public FiscalMonth startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public FiscalMonth endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getFiscalMonthCode() {
        return this.fiscalMonthCode;
    }

    public FiscalMonth fiscalMonthCode(String fiscalMonthCode) {
        this.setFiscalMonthCode(fiscalMonthCode);
        return this;
    }

    public void setFiscalMonthCode(String fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public FiscalYear getFiscalYear() {
        return this.fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public FiscalMonth fiscalYear(FiscalYear fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public FiscalMonth placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public FiscalMonth addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public FiscalMonth removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getUniversallyUniqueMappings() {
        return this.universallyUniqueMappings;
    }

    public void setUniversallyUniqueMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.universallyUniqueMappings = universallyUniqueMappings;
    }

    public FiscalMonth universallyUniqueMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setUniversallyUniqueMappings(universallyUniqueMappings);
        return this;
    }

    public FiscalMonth addUniversallyUniqueMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.universallyUniqueMappings.add(universallyUniqueMapping);
        return this;
    }

    public FiscalMonth removeUniversallyUniqueMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.universallyUniqueMappings.remove(universallyUniqueMapping);
        return this;
    }

    public FiscalQuarter getFiscalQuarter() {
        return this.fiscalQuarter;
    }

    public void setFiscalQuarter(FiscalQuarter fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public FiscalMonth fiscalQuarter(FiscalQuarter fiscalQuarter) {
        this.setFiscalQuarter(fiscalQuarter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiscalMonth)) {
            return false;
        }
        return id != null && id.equals(((FiscalMonth) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiscalMonth{" +
            "id=" + getId() +
            ", monthNumber=" + getMonthNumber() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", fiscalMonthCode='" + getFiscalMonthCode() + "'" +
            "}";
    }
}
