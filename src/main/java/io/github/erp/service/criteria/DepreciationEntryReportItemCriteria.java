package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.DepreciationEntryReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationEntryReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-entry-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationEntryReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetRegistrationDetails;

    private StringFilter postedAt;

    private LongFilter assetNumber;

    private StringFilter serviceOutlet;

    private StringFilter assetCategory;

    private StringFilter depreciationMethod;

    private StringFilter depreciationPeriod;

    private StringFilter fiscalMonthCode;

    private BigDecimalFilter assetRegistrationCost;

    private BigDecimalFilter depreciationAmount;

    private LongFilter elapsedMonths;

    private LongFilter priorMonths;

    private BigDecimalFilter usefulLifeYears;

    private BigDecimalFilter previousNBV;

    private BigDecimalFilter netBookValue;

    private LocalDateFilter depreciationPeriodStartDate;

    private LocalDateFilter depreciationPeriodEndDate;

    private Boolean distinct;

    public DepreciationEntryReportItemCriteria() {}

    public DepreciationEntryReportItemCriteria(DepreciationEntryReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetRegistrationDetails = other.assetRegistrationDetails == null ? null : other.assetRegistrationDetails.copy();
        this.postedAt = other.postedAt == null ? null : other.postedAt.copy();
        this.assetNumber = other.assetNumber == null ? null : other.assetNumber.copy();
        this.serviceOutlet = other.serviceOutlet == null ? null : other.serviceOutlet.copy();
        this.assetCategory = other.assetCategory == null ? null : other.assetCategory.copy();
        this.depreciationMethod = other.depreciationMethod == null ? null : other.depreciationMethod.copy();
        this.depreciationPeriod = other.depreciationPeriod == null ? null : other.depreciationPeriod.copy();
        this.fiscalMonthCode = other.fiscalMonthCode == null ? null : other.fiscalMonthCode.copy();
        this.assetRegistrationCost = other.assetRegistrationCost == null ? null : other.assetRegistrationCost.copy();
        this.depreciationAmount = other.depreciationAmount == null ? null : other.depreciationAmount.copy();
        this.elapsedMonths = other.elapsedMonths == null ? null : other.elapsedMonths.copy();
        this.priorMonths = other.priorMonths == null ? null : other.priorMonths.copy();
        this.usefulLifeYears = other.usefulLifeYears == null ? null : other.usefulLifeYears.copy();
        this.previousNBV = other.previousNBV == null ? null : other.previousNBV.copy();
        this.netBookValue = other.netBookValue == null ? null : other.netBookValue.copy();
        this.depreciationPeriodStartDate = other.depreciationPeriodStartDate == null ? null : other.depreciationPeriodStartDate.copy();
        this.depreciationPeriodEndDate = other.depreciationPeriodEndDate == null ? null : other.depreciationPeriodEndDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationEntryReportItemCriteria copy() {
        return new DepreciationEntryReportItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAssetRegistrationDetails() {
        return assetRegistrationDetails;
    }

    public StringFilter assetRegistrationDetails() {
        if (assetRegistrationDetails == null) {
            assetRegistrationDetails = new StringFilter();
        }
        return assetRegistrationDetails;
    }

    public void setAssetRegistrationDetails(StringFilter assetRegistrationDetails) {
        this.assetRegistrationDetails = assetRegistrationDetails;
    }

    public StringFilter getPostedAt() {
        return postedAt;
    }

    public StringFilter postedAt() {
        if (postedAt == null) {
            postedAt = new StringFilter();
        }
        return postedAt;
    }

    public void setPostedAt(StringFilter postedAt) {
        this.postedAt = postedAt;
    }

    public LongFilter getAssetNumber() {
        return assetNumber;
    }

    public LongFilter assetNumber() {
        if (assetNumber == null) {
            assetNumber = new LongFilter();
        }
        return assetNumber;
    }

    public void setAssetNumber(LongFilter assetNumber) {
        this.assetNumber = assetNumber;
    }

    public StringFilter getServiceOutlet() {
        return serviceOutlet;
    }

    public StringFilter serviceOutlet() {
        if (serviceOutlet == null) {
            serviceOutlet = new StringFilter();
        }
        return serviceOutlet;
    }

    public void setServiceOutlet(StringFilter serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public StringFilter getAssetCategory() {
        return assetCategory;
    }

    public StringFilter assetCategory() {
        if (assetCategory == null) {
            assetCategory = new StringFilter();
        }
        return assetCategory;
    }

    public void setAssetCategory(StringFilter assetCategory) {
        this.assetCategory = assetCategory;
    }

    public StringFilter getDepreciationMethod() {
        return depreciationMethod;
    }

    public StringFilter depreciationMethod() {
        if (depreciationMethod == null) {
            depreciationMethod = new StringFilter();
        }
        return depreciationMethod;
    }

    public void setDepreciationMethod(StringFilter depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public StringFilter getDepreciationPeriod() {
        return depreciationPeriod;
    }

    public StringFilter depreciationPeriod() {
        if (depreciationPeriod == null) {
            depreciationPeriod = new StringFilter();
        }
        return depreciationPeriod;
    }

    public void setDepreciationPeriod(StringFilter depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public StringFilter getFiscalMonthCode() {
        return fiscalMonthCode;
    }

    public StringFilter fiscalMonthCode() {
        if (fiscalMonthCode == null) {
            fiscalMonthCode = new StringFilter();
        }
        return fiscalMonthCode;
    }

    public void setFiscalMonthCode(StringFilter fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public BigDecimalFilter getAssetRegistrationCost() {
        return assetRegistrationCost;
    }

    public BigDecimalFilter assetRegistrationCost() {
        if (assetRegistrationCost == null) {
            assetRegistrationCost = new BigDecimalFilter();
        }
        return assetRegistrationCost;
    }

    public void setAssetRegistrationCost(BigDecimalFilter assetRegistrationCost) {
        this.assetRegistrationCost = assetRegistrationCost;
    }

    public BigDecimalFilter getDepreciationAmount() {
        return depreciationAmount;
    }

    public BigDecimalFilter depreciationAmount() {
        if (depreciationAmount == null) {
            depreciationAmount = new BigDecimalFilter();
        }
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimalFilter depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public LongFilter getElapsedMonths() {
        return elapsedMonths;
    }

    public LongFilter elapsedMonths() {
        if (elapsedMonths == null) {
            elapsedMonths = new LongFilter();
        }
        return elapsedMonths;
    }

    public void setElapsedMonths(LongFilter elapsedMonths) {
        this.elapsedMonths = elapsedMonths;
    }

    public LongFilter getPriorMonths() {
        return priorMonths;
    }

    public LongFilter priorMonths() {
        if (priorMonths == null) {
            priorMonths = new LongFilter();
        }
        return priorMonths;
    }

    public void setPriorMonths(LongFilter priorMonths) {
        this.priorMonths = priorMonths;
    }

    public BigDecimalFilter getUsefulLifeYears() {
        return usefulLifeYears;
    }

    public BigDecimalFilter usefulLifeYears() {
        if (usefulLifeYears == null) {
            usefulLifeYears = new BigDecimalFilter();
        }
        return usefulLifeYears;
    }

    public void setUsefulLifeYears(BigDecimalFilter usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public BigDecimalFilter getPreviousNBV() {
        return previousNBV;
    }

    public BigDecimalFilter previousNBV() {
        if (previousNBV == null) {
            previousNBV = new BigDecimalFilter();
        }
        return previousNBV;
    }

    public void setPreviousNBV(BigDecimalFilter previousNBV) {
        this.previousNBV = previousNBV;
    }

    public BigDecimalFilter getNetBookValue() {
        return netBookValue;
    }

    public BigDecimalFilter netBookValue() {
        if (netBookValue == null) {
            netBookValue = new BigDecimalFilter();
        }
        return netBookValue;
    }

    public void setNetBookValue(BigDecimalFilter netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDateFilter getDepreciationPeriodStartDate() {
        return depreciationPeriodStartDate;
    }

    public LocalDateFilter depreciationPeriodStartDate() {
        if (depreciationPeriodStartDate == null) {
            depreciationPeriodStartDate = new LocalDateFilter();
        }
        return depreciationPeriodStartDate;
    }

    public void setDepreciationPeriodStartDate(LocalDateFilter depreciationPeriodStartDate) {
        this.depreciationPeriodStartDate = depreciationPeriodStartDate;
    }

    public LocalDateFilter getDepreciationPeriodEndDate() {
        return depreciationPeriodEndDate;
    }

    public LocalDateFilter depreciationPeriodEndDate() {
        if (depreciationPeriodEndDate == null) {
            depreciationPeriodEndDate = new LocalDateFilter();
        }
        return depreciationPeriodEndDate;
    }

    public void setDepreciationPeriodEndDate(LocalDateFilter depreciationPeriodEndDate) {
        this.depreciationPeriodEndDate = depreciationPeriodEndDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DepreciationEntryReportItemCriteria that = (DepreciationEntryReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetRegistrationDetails, that.assetRegistrationDetails) &&
            Objects.equals(postedAt, that.postedAt) &&
            Objects.equals(assetNumber, that.assetNumber) &&
            Objects.equals(serviceOutlet, that.serviceOutlet) &&
            Objects.equals(assetCategory, that.assetCategory) &&
            Objects.equals(depreciationMethod, that.depreciationMethod) &&
            Objects.equals(depreciationPeriod, that.depreciationPeriod) &&
            Objects.equals(fiscalMonthCode, that.fiscalMonthCode) &&
            Objects.equals(assetRegistrationCost, that.assetRegistrationCost) &&
            Objects.equals(depreciationAmount, that.depreciationAmount) &&
            Objects.equals(elapsedMonths, that.elapsedMonths) &&
            Objects.equals(priorMonths, that.priorMonths) &&
            Objects.equals(usefulLifeYears, that.usefulLifeYears) &&
            Objects.equals(previousNBV, that.previousNBV) &&
            Objects.equals(netBookValue, that.netBookValue) &&
            Objects.equals(depreciationPeriodStartDate, that.depreciationPeriodStartDate) &&
            Objects.equals(depreciationPeriodEndDate, that.depreciationPeriodEndDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetRegistrationDetails,
            postedAt,
            assetNumber,
            serviceOutlet,
            assetCategory,
            depreciationMethod,
            depreciationPeriod,
            fiscalMonthCode,
            assetRegistrationCost,
            depreciationAmount,
            elapsedMonths,
            priorMonths,
            usefulLifeYears,
            previousNBV,
            netBookValue,
            depreciationPeriodStartDate,
            depreciationPeriodEndDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationEntryReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetRegistrationDetails != null ? "assetRegistrationDetails=" + assetRegistrationDetails + ", " : "") +
            (postedAt != null ? "postedAt=" + postedAt + ", " : "") +
            (assetNumber != null ? "assetNumber=" + assetNumber + ", " : "") +
            (serviceOutlet != null ? "serviceOutlet=" + serviceOutlet + ", " : "") +
            (assetCategory != null ? "assetCategory=" + assetCategory + ", " : "") +
            (depreciationMethod != null ? "depreciationMethod=" + depreciationMethod + ", " : "") +
            (depreciationPeriod != null ? "depreciationPeriod=" + depreciationPeriod + ", " : "") +
            (fiscalMonthCode != null ? "fiscalMonthCode=" + fiscalMonthCode + ", " : "") +
            (assetRegistrationCost != null ? "assetRegistrationCost=" + assetRegistrationCost + ", " : "") +
            (depreciationAmount != null ? "depreciationAmount=" + depreciationAmount + ", " : "") +
            (elapsedMonths != null ? "elapsedMonths=" + elapsedMonths + ", " : "") +
            (priorMonths != null ? "priorMonths=" + priorMonths + ", " : "") +
            (usefulLifeYears != null ? "usefulLifeYears=" + usefulLifeYears + ", " : "") +
            (previousNBV != null ? "previousNBV=" + previousNBV + ", " : "") +
            (netBookValue != null ? "netBookValue=" + netBookValue + ", " : "") +
            (depreciationPeriodStartDate != null ? "depreciationPeriodStartDate=" + depreciationPeriodStartDate + ", " : "") +
            (depreciationPeriodEndDate != null ? "depreciationPeriodEndDate=" + depreciationPeriodEndDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
