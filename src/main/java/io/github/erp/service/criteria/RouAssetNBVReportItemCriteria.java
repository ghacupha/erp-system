package io.github.erp.service.criteria;

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
 * Criteria class for the {@link io.github.erp.domain.RouAssetNBVReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.RouAssetNBVReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-asset-nbv-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouAssetNBVReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter modelTitle;

    private BigDecimalFilter modelVersion;

    private StringFilter description;

    private StringFilter rouModelReference;

    private LocalDateFilter commencementDate;

    private LocalDateFilter expirationDate;

    private StringFilter assetCategoryName;

    private StringFilter assetAccountNumber;

    private StringFilter depreciationAccountNumber;

    private LocalDateFilter fiscalPeriodEndDate;

    private BigDecimalFilter leaseAmount;

    private BigDecimalFilter netBookValue;

    private Boolean distinct;

    public RouAssetNBVReportItemCriteria() {}

    public RouAssetNBVReportItemCriteria(RouAssetNBVReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.modelTitle = other.modelTitle == null ? null : other.modelTitle.copy();
        this.modelVersion = other.modelVersion == null ? null : other.modelVersion.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.rouModelReference = other.rouModelReference == null ? null : other.rouModelReference.copy();
        this.commencementDate = other.commencementDate == null ? null : other.commencementDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.assetCategoryName = other.assetCategoryName == null ? null : other.assetCategoryName.copy();
        this.assetAccountNumber = other.assetAccountNumber == null ? null : other.assetAccountNumber.copy();
        this.depreciationAccountNumber = other.depreciationAccountNumber == null ? null : other.depreciationAccountNumber.copy();
        this.fiscalPeriodEndDate = other.fiscalPeriodEndDate == null ? null : other.fiscalPeriodEndDate.copy();
        this.leaseAmount = other.leaseAmount == null ? null : other.leaseAmount.copy();
        this.netBookValue = other.netBookValue == null ? null : other.netBookValue.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouAssetNBVReportItemCriteria copy() {
        return new RouAssetNBVReportItemCriteria(this);
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

    public StringFilter getModelTitle() {
        return modelTitle;
    }

    public StringFilter modelTitle() {
        if (modelTitle == null) {
            modelTitle = new StringFilter();
        }
        return modelTitle;
    }

    public void setModelTitle(StringFilter modelTitle) {
        this.modelTitle = modelTitle;
    }

    public BigDecimalFilter getModelVersion() {
        return modelVersion;
    }

    public BigDecimalFilter modelVersion() {
        if (modelVersion == null) {
            modelVersion = new BigDecimalFilter();
        }
        return modelVersion;
    }

    public void setModelVersion(BigDecimalFilter modelVersion) {
        this.modelVersion = modelVersion;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getRouModelReference() {
        return rouModelReference;
    }

    public StringFilter rouModelReference() {
        if (rouModelReference == null) {
            rouModelReference = new StringFilter();
        }
        return rouModelReference;
    }

    public void setRouModelReference(StringFilter rouModelReference) {
        this.rouModelReference = rouModelReference;
    }

    public LocalDateFilter getCommencementDate() {
        return commencementDate;
    }

    public LocalDateFilter commencementDate() {
        if (commencementDate == null) {
            commencementDate = new LocalDateFilter();
        }
        return commencementDate;
    }

    public void setCommencementDate(LocalDateFilter commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDateFilter getExpirationDate() {
        return expirationDate;
    }

    public LocalDateFilter expirationDate() {
        if (expirationDate == null) {
            expirationDate = new LocalDateFilter();
        }
        return expirationDate;
    }

    public void setExpirationDate(LocalDateFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public StringFilter getAssetCategoryName() {
        return assetCategoryName;
    }

    public StringFilter assetCategoryName() {
        if (assetCategoryName == null) {
            assetCategoryName = new StringFilter();
        }
        return assetCategoryName;
    }

    public void setAssetCategoryName(StringFilter assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public StringFilter getAssetAccountNumber() {
        return assetAccountNumber;
    }

    public StringFilter assetAccountNumber() {
        if (assetAccountNumber == null) {
            assetAccountNumber = new StringFilter();
        }
        return assetAccountNumber;
    }

    public void setAssetAccountNumber(StringFilter assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public StringFilter getDepreciationAccountNumber() {
        return depreciationAccountNumber;
    }

    public StringFilter depreciationAccountNumber() {
        if (depreciationAccountNumber == null) {
            depreciationAccountNumber = new StringFilter();
        }
        return depreciationAccountNumber;
    }

    public void setDepreciationAccountNumber(StringFilter depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public LocalDateFilter getFiscalPeriodEndDate() {
        return fiscalPeriodEndDate;
    }

    public LocalDateFilter fiscalPeriodEndDate() {
        if (fiscalPeriodEndDate == null) {
            fiscalPeriodEndDate = new LocalDateFilter();
        }
        return fiscalPeriodEndDate;
    }

    public void setFiscalPeriodEndDate(LocalDateFilter fiscalPeriodEndDate) {
        this.fiscalPeriodEndDate = fiscalPeriodEndDate;
    }

    public BigDecimalFilter getLeaseAmount() {
        return leaseAmount;
    }

    public BigDecimalFilter leaseAmount() {
        if (leaseAmount == null) {
            leaseAmount = new BigDecimalFilter();
        }
        return leaseAmount;
    }

    public void setLeaseAmount(BigDecimalFilter leaseAmount) {
        this.leaseAmount = leaseAmount;
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
        final RouAssetNBVReportItemCriteria that = (RouAssetNBVReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(modelTitle, that.modelTitle) &&
            Objects.equals(modelVersion, that.modelVersion) &&
            Objects.equals(description, that.description) &&
            Objects.equals(rouModelReference, that.rouModelReference) &&
            Objects.equals(commencementDate, that.commencementDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(assetCategoryName, that.assetCategoryName) &&
            Objects.equals(assetAccountNumber, that.assetAccountNumber) &&
            Objects.equals(depreciationAccountNumber, that.depreciationAccountNumber) &&
            Objects.equals(fiscalPeriodEndDate, that.fiscalPeriodEndDate) &&
            Objects.equals(leaseAmount, that.leaseAmount) &&
            Objects.equals(netBookValue, that.netBookValue) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            modelTitle,
            modelVersion,
            description,
            rouModelReference,
            commencementDate,
            expirationDate,
            assetCategoryName,
            assetAccountNumber,
            depreciationAccountNumber,
            fiscalPeriodEndDate,
            leaseAmount,
            netBookValue,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAssetNBVReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (modelTitle != null ? "modelTitle=" + modelTitle + ", " : "") +
            (modelVersion != null ? "modelVersion=" + modelVersion + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (rouModelReference != null ? "rouModelReference=" + rouModelReference + ", " : "") +
            (commencementDate != null ? "commencementDate=" + commencementDate + ", " : "") +
            (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
            (assetCategoryName != null ? "assetCategoryName=" + assetCategoryName + ", " : "") +
            (assetAccountNumber != null ? "assetAccountNumber=" + assetAccountNumber + ", " : "") +
            (depreciationAccountNumber != null ? "depreciationAccountNumber=" + depreciationAccountNumber + ", " : "") +
            (fiscalPeriodEndDate != null ? "fiscalPeriodEndDate=" + fiscalPeriodEndDate + ", " : "") +
            (leaseAmount != null ? "leaseAmount=" + leaseAmount + ", " : "") +
            (netBookValue != null ? "netBookValue=" + netBookValue + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
