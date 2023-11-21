package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DepreciationPeriod.
 */
@Entity
@Table(name = "depreciation_period")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depreciationperiod")
public class DepreciationPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_period_status")
    private DepreciationPeriodStatusTypes depreciationPeriodStatus;

    @NotNull
    @Column(name = "period_code", nullable = false, unique = true)
    private String periodCode;

    @Column(name = "process_locked")
    private Boolean processLocked;

    @JsonIgnoreProperties(value = { "previousPeriod", "createdBy", "fiscalYear", "fiscalMonth", "fiscalQuarter" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private DepreciationPeriod previousPeriod;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser createdBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "universallyUniqueMappings", "createdBy", "lastUpdatedBy" }, allowSetters = true)
    private FiscalYear fiscalYear;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth fiscalMonth;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings" }, allowSetters = true)
    private FiscalQuarter fiscalQuarter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepreciationPeriod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public DepreciationPeriod startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public DepreciationPeriod endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DepreciationPeriodStatusTypes getDepreciationPeriodStatus() {
        return this.depreciationPeriodStatus;
    }

    public DepreciationPeriod depreciationPeriodStatus(DepreciationPeriodStatusTypes depreciationPeriodStatus) {
        this.setDepreciationPeriodStatus(depreciationPeriodStatus);
        return this;
    }

    public void setDepreciationPeriodStatus(DepreciationPeriodStatusTypes depreciationPeriodStatus) {
        this.depreciationPeriodStatus = depreciationPeriodStatus;
    }

    public String getPeriodCode() {
        return this.periodCode;
    }

    public DepreciationPeriod periodCode(String periodCode) {
        this.setPeriodCode(periodCode);
        return this;
    }

    public void setPeriodCode(String periodCode) {
        this.periodCode = periodCode;
    }

    public Boolean getProcessLocked() {
        return this.processLocked;
    }

    public DepreciationPeriod processLocked(Boolean processLocked) {
        this.setProcessLocked(processLocked);
        return this;
    }

    public void setProcessLocked(Boolean processLocked) {
        this.processLocked = processLocked;
    }

    public DepreciationPeriod getPreviousPeriod() {
        return this.previousPeriod;
    }

    public void setPreviousPeriod(DepreciationPeriod depreciationPeriod) {
        this.previousPeriod = depreciationPeriod;
    }

    public DepreciationPeriod previousPeriod(DepreciationPeriod depreciationPeriod) {
        this.setPreviousPeriod(depreciationPeriod);
        return this;
    }

    public ApplicationUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ApplicationUser applicationUser) {
        this.createdBy = applicationUser;
    }

    public DepreciationPeriod createdBy(ApplicationUser applicationUser) {
        this.setCreatedBy(applicationUser);
        return this;
    }

    public FiscalYear getFiscalYear() {
        return this.fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public DepreciationPeriod fiscalYear(FiscalYear fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    public FiscalMonth getFiscalMonth() {
        return this.fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonth fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public DepreciationPeriod fiscalMonth(FiscalMonth fiscalMonth) {
        this.setFiscalMonth(fiscalMonth);
        return this;
    }

    public FiscalQuarter getFiscalQuarter() {
        return this.fiscalQuarter;
    }

    public void setFiscalQuarter(FiscalQuarter fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public DepreciationPeriod fiscalQuarter(FiscalQuarter fiscalQuarter) {
        this.setFiscalQuarter(fiscalQuarter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationPeriod)) {
            return false;
        }
        return id != null && id.equals(((DepreciationPeriod) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationPeriod{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", depreciationPeriodStatus='" + getDepreciationPeriodStatus() + "'" +
            ", periodCode='" + getPeriodCode() + "'" +
            ", processLocked='" + getProcessLocked() + "'" +
            "}";
    }
}
