package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.RouAssetListReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.RouAssetListReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-asset-list-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouAssetListReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter modelTitle;

    private BigDecimalFilter modelVersion;

    private StringFilter description;

    private IntegerFilter leaseTermPeriods;

    private UUIDFilter rouModelReference;

    private LocalDateFilter commencementDate;

    private LocalDateFilter expirationDate;

    private StringFilter leaseContractTitle;

    private StringFilter assetAccountNumber;

    private StringFilter depreciationAccountNumber;

    private StringFilter accruedDepreciationAccountNumber;

    private StringFilter assetCategoryName;

    private BigDecimalFilter leaseAmount;

    private StringFilter leaseContractSerialNumber;

    private Boolean distinct;

    public RouAssetListReportItemCriteria() {}

    public RouAssetListReportItemCriteria(RouAssetListReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.modelTitle = other.modelTitle == null ? null : other.modelTitle.copy();
        this.modelVersion = other.modelVersion == null ? null : other.modelVersion.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.leaseTermPeriods = other.leaseTermPeriods == null ? null : other.leaseTermPeriods.copy();
        this.rouModelReference = other.rouModelReference == null ? null : other.rouModelReference.copy();
        this.commencementDate = other.commencementDate == null ? null : other.commencementDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.leaseContractTitle = other.leaseContractTitle == null ? null : other.leaseContractTitle.copy();
        this.assetAccountNumber = other.assetAccountNumber == null ? null : other.assetAccountNumber.copy();
        this.depreciationAccountNumber = other.depreciationAccountNumber == null ? null : other.depreciationAccountNumber.copy();
        this.accruedDepreciationAccountNumber =
            other.accruedDepreciationAccountNumber == null ? null : other.accruedDepreciationAccountNumber.copy();
        this.assetCategoryName = other.assetCategoryName == null ? null : other.assetCategoryName.copy();
        this.leaseAmount = other.leaseAmount == null ? null : other.leaseAmount.copy();
        this.leaseContractSerialNumber = other.leaseContractSerialNumber == null ? null : other.leaseContractSerialNumber.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouAssetListReportItemCriteria copy() {
        return new RouAssetListReportItemCriteria(this);
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

    public IntegerFilter getLeaseTermPeriods() {
        return leaseTermPeriods;
    }

    public IntegerFilter leaseTermPeriods() {
        if (leaseTermPeriods == null) {
            leaseTermPeriods = new IntegerFilter();
        }
        return leaseTermPeriods;
    }

    public void setLeaseTermPeriods(IntegerFilter leaseTermPeriods) {
        this.leaseTermPeriods = leaseTermPeriods;
    }

    public UUIDFilter getRouModelReference() {
        return rouModelReference;
    }

    public UUIDFilter rouModelReference() {
        if (rouModelReference == null) {
            rouModelReference = new UUIDFilter();
        }
        return rouModelReference;
    }

    public void setRouModelReference(UUIDFilter rouModelReference) {
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

    public StringFilter getLeaseContractTitle() {
        return leaseContractTitle;
    }

    public StringFilter leaseContractTitle() {
        if (leaseContractTitle == null) {
            leaseContractTitle = new StringFilter();
        }
        return leaseContractTitle;
    }

    public void setLeaseContractTitle(StringFilter leaseContractTitle) {
        this.leaseContractTitle = leaseContractTitle;
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

    public StringFilter getAccruedDepreciationAccountNumber() {
        return accruedDepreciationAccountNumber;
    }

    public StringFilter accruedDepreciationAccountNumber() {
        if (accruedDepreciationAccountNumber == null) {
            accruedDepreciationAccountNumber = new StringFilter();
        }
        return accruedDepreciationAccountNumber;
    }

    public void setAccruedDepreciationAccountNumber(StringFilter accruedDepreciationAccountNumber) {
        this.accruedDepreciationAccountNumber = accruedDepreciationAccountNumber;
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

    public StringFilter getLeaseContractSerialNumber() {
        return leaseContractSerialNumber;
    }

    public StringFilter leaseContractSerialNumber() {
        if (leaseContractSerialNumber == null) {
            leaseContractSerialNumber = new StringFilter();
        }
        return leaseContractSerialNumber;
    }

    public void setLeaseContractSerialNumber(StringFilter leaseContractSerialNumber) {
        this.leaseContractSerialNumber = leaseContractSerialNumber;
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
        final RouAssetListReportItemCriteria that = (RouAssetListReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(modelTitle, that.modelTitle) &&
            Objects.equals(modelVersion, that.modelVersion) &&
            Objects.equals(description, that.description) &&
            Objects.equals(leaseTermPeriods, that.leaseTermPeriods) &&
            Objects.equals(rouModelReference, that.rouModelReference) &&
            Objects.equals(commencementDate, that.commencementDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(leaseContractTitle, that.leaseContractTitle) &&
            Objects.equals(assetAccountNumber, that.assetAccountNumber) &&
            Objects.equals(depreciationAccountNumber, that.depreciationAccountNumber) &&
            Objects.equals(accruedDepreciationAccountNumber, that.accruedDepreciationAccountNumber) &&
            Objects.equals(assetCategoryName, that.assetCategoryName) &&
            Objects.equals(leaseAmount, that.leaseAmount) &&
            Objects.equals(leaseContractSerialNumber, that.leaseContractSerialNumber) &&
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
            leaseTermPeriods,
            rouModelReference,
            commencementDate,
            expirationDate,
            leaseContractTitle,
            assetAccountNumber,
            depreciationAccountNumber,
            accruedDepreciationAccountNumber,
            assetCategoryName,
            leaseAmount,
            leaseContractSerialNumber,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouAssetListReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (modelTitle != null ? "modelTitle=" + modelTitle + ", " : "") +
            (modelVersion != null ? "modelVersion=" + modelVersion + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (leaseTermPeriods != null ? "leaseTermPeriods=" + leaseTermPeriods + ", " : "") +
            (rouModelReference != null ? "rouModelReference=" + rouModelReference + ", " : "") +
            (commencementDate != null ? "commencementDate=" + commencementDate + ", " : "") +
            (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
            (leaseContractTitle != null ? "leaseContractTitle=" + leaseContractTitle + ", " : "") +
            (assetAccountNumber != null ? "assetAccountNumber=" + assetAccountNumber + ", " : "") +
            (depreciationAccountNumber != null ? "depreciationAccountNumber=" + depreciationAccountNumber + ", " : "") +
            (accruedDepreciationAccountNumber != null ? "accruedDepreciationAccountNumber=" + accruedDepreciationAccountNumber + ", " : "") +
            (assetCategoryName != null ? "assetCategoryName=" + assetCategoryName + ", " : "") +
            (leaseAmount != null ? "leaseAmount=" + leaseAmount + ", " : "") +
            (leaseContractSerialNumber != null ? "leaseContractSerialNumber=" + leaseContractSerialNumber + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
