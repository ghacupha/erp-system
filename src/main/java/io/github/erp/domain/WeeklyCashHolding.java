package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WeeklyCashHolding.
 */
@Entity
@Table(name = "weekly_cash_holding")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "weeklycashholding")
public class WeeklyCashHolding implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reporting_date", nullable = false)
    private LocalDate reportingDate;

    @NotNull
    @Column(name = "fit_units", nullable = false)
    private Integer fitUnits;

    @NotNull
    @Column(name = "unfit_units", nullable = false)
    private Integer unfitUnits;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private BankBranchCode branchId;

    @ManyToOne(optional = false)
    @NotNull
    private CountySubCountyCode subCountyCode;

    @ManyToOne(optional = false)
    @NotNull
    private KenyanCurrencyDenomination denomination;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WeeklyCashHolding id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public WeeklyCashHolding reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Integer getFitUnits() {
        return this.fitUnits;
    }

    public WeeklyCashHolding fitUnits(Integer fitUnits) {
        this.setFitUnits(fitUnits);
        return this;
    }

    public void setFitUnits(Integer fitUnits) {
        this.fitUnits = fitUnits;
    }

    public Integer getUnfitUnits() {
        return this.unfitUnits;
    }

    public WeeklyCashHolding unfitUnits(Integer unfitUnits) {
        this.setUnfitUnits(unfitUnits);
        return this;
    }

    public void setUnfitUnits(Integer unfitUnits) {
        this.unfitUnits = unfitUnits;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public WeeklyCashHolding bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public BankBranchCode getBranchId() {
        return this.branchId;
    }

    public void setBranchId(BankBranchCode bankBranchCode) {
        this.branchId = bankBranchCode;
    }

    public WeeklyCashHolding branchId(BankBranchCode bankBranchCode) {
        this.setBranchId(bankBranchCode);
        return this;
    }

    public CountySubCountyCode getSubCountyCode() {
        return this.subCountyCode;
    }

    public void setSubCountyCode(CountySubCountyCode countySubCountyCode) {
        this.subCountyCode = countySubCountyCode;
    }

    public WeeklyCashHolding subCountyCode(CountySubCountyCode countySubCountyCode) {
        this.setSubCountyCode(countySubCountyCode);
        return this;
    }

    public KenyanCurrencyDenomination getDenomination() {
        return this.denomination;
    }

    public void setDenomination(KenyanCurrencyDenomination kenyanCurrencyDenomination) {
        this.denomination = kenyanCurrencyDenomination;
    }

    public WeeklyCashHolding denomination(KenyanCurrencyDenomination kenyanCurrencyDenomination) {
        this.setDenomination(kenyanCurrencyDenomination);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeeklyCashHolding)) {
            return false;
        }
        return id != null && id.equals(((WeeklyCashHolding) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyCashHolding{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", fitUnits=" + getFitUnits() +
            ", unfitUnits=" + getUnfitUnits() +
            "}";
    }
}
