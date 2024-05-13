package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
 * Criteria class for the {@link io.github.erp.domain.RouDepreciationEntryReportItem} entity. This class is used
 * in {@link io.github.erp.web.rest.RouDepreciationEntryReportItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-depreciation-entry-report-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouDepreciationEntryReportItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter leaseContractNumber;

    private StringFilter fiscalPeriodCode;

    private LocalDateFilter fiscalPeriodEndDate;

    private StringFilter assetCategoryName;

    private StringFilter debitAccountNumber;

    private StringFilter creditAccountNumber;

    private StringFilter description;

    private StringFilter shortTitle;

    private StringFilter rouAssetIdentifier;

    private IntegerFilter sequenceNumber;

    private BigDecimalFilter depreciationAmount;

    private BigDecimalFilter outstandingAmount;

    private Boolean distinct;

    public RouDepreciationEntryReportItemCriteria() {}

    public RouDepreciationEntryReportItemCriteria(RouDepreciationEntryReportItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.leaseContractNumber = other.leaseContractNumber == null ? null : other.leaseContractNumber.copy();
        this.fiscalPeriodCode = other.fiscalPeriodCode == null ? null : other.fiscalPeriodCode.copy();
        this.fiscalPeriodEndDate = other.fiscalPeriodEndDate == null ? null : other.fiscalPeriodEndDate.copy();
        this.assetCategoryName = other.assetCategoryName == null ? null : other.assetCategoryName.copy();
        this.debitAccountNumber = other.debitAccountNumber == null ? null : other.debitAccountNumber.copy();
        this.creditAccountNumber = other.creditAccountNumber == null ? null : other.creditAccountNumber.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.shortTitle = other.shortTitle == null ? null : other.shortTitle.copy();
        this.rouAssetIdentifier = other.rouAssetIdentifier == null ? null : other.rouAssetIdentifier.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.depreciationAmount = other.depreciationAmount == null ? null : other.depreciationAmount.copy();
        this.outstandingAmount = other.outstandingAmount == null ? null : other.outstandingAmount.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouDepreciationEntryReportItemCriteria copy() {
        return new RouDepreciationEntryReportItemCriteria(this);
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

    public StringFilter getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public StringFilter leaseContractNumber() {
        if (leaseContractNumber == null) {
            leaseContractNumber = new StringFilter();
        }
        return leaseContractNumber;
    }

    public void setLeaseContractNumber(StringFilter leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public StringFilter getFiscalPeriodCode() {
        return fiscalPeriodCode;
    }

    public StringFilter fiscalPeriodCode() {
        if (fiscalPeriodCode == null) {
            fiscalPeriodCode = new StringFilter();
        }
        return fiscalPeriodCode;
    }

    public void setFiscalPeriodCode(StringFilter fiscalPeriodCode) {
        this.fiscalPeriodCode = fiscalPeriodCode;
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

    public StringFilter getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public StringFilter debitAccountNumber() {
        if (debitAccountNumber == null) {
            debitAccountNumber = new StringFilter();
        }
        return debitAccountNumber;
    }

    public void setDebitAccountNumber(StringFilter debitAccountNumber) {
        this.debitAccountNumber = debitAccountNumber;
    }

    public StringFilter getCreditAccountNumber() {
        return creditAccountNumber;
    }

    public StringFilter creditAccountNumber() {
        if (creditAccountNumber == null) {
            creditAccountNumber = new StringFilter();
        }
        return creditAccountNumber;
    }

    public void setCreditAccountNumber(StringFilter creditAccountNumber) {
        this.creditAccountNumber = creditAccountNumber;
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

    public StringFilter getShortTitle() {
        return shortTitle;
    }

    public StringFilter shortTitle() {
        if (shortTitle == null) {
            shortTitle = new StringFilter();
        }
        return shortTitle;
    }

    public void setShortTitle(StringFilter shortTitle) {
        this.shortTitle = shortTitle;
    }

    public StringFilter getRouAssetIdentifier() {
        return rouAssetIdentifier;
    }

    public StringFilter rouAssetIdentifier() {
        if (rouAssetIdentifier == null) {
            rouAssetIdentifier = new StringFilter();
        }
        return rouAssetIdentifier;
    }

    public void setRouAssetIdentifier(StringFilter rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public IntegerFilter getSequenceNumber() {
        return sequenceNumber;
    }

    public IntegerFilter sequenceNumber() {
        if (sequenceNumber == null) {
            sequenceNumber = new IntegerFilter();
        }
        return sequenceNumber;
    }

    public void setSequenceNumber(IntegerFilter sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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

    public BigDecimalFilter getOutstandingAmount() {
        return outstandingAmount;
    }

    public BigDecimalFilter outstandingAmount() {
        if (outstandingAmount == null) {
            outstandingAmount = new BigDecimalFilter();
        }
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimalFilter outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
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
        final RouDepreciationEntryReportItemCriteria that = (RouDepreciationEntryReportItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(leaseContractNumber, that.leaseContractNumber) &&
            Objects.equals(fiscalPeriodCode, that.fiscalPeriodCode) &&
            Objects.equals(fiscalPeriodEndDate, that.fiscalPeriodEndDate) &&
            Objects.equals(assetCategoryName, that.assetCategoryName) &&
            Objects.equals(debitAccountNumber, that.debitAccountNumber) &&
            Objects.equals(creditAccountNumber, that.creditAccountNumber) &&
            Objects.equals(description, that.description) &&
            Objects.equals(shortTitle, that.shortTitle) &&
            Objects.equals(rouAssetIdentifier, that.rouAssetIdentifier) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(depreciationAmount, that.depreciationAmount) &&
            Objects.equals(outstandingAmount, that.outstandingAmount) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            leaseContractNumber,
            fiscalPeriodCode,
            fiscalPeriodEndDate,
            assetCategoryName,
            debitAccountNumber,
            creditAccountNumber,
            description,
            shortTitle,
            rouAssetIdentifier,
            sequenceNumber,
            depreciationAmount,
            outstandingAmount,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationEntryReportItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (leaseContractNumber != null ? "leaseContractNumber=" + leaseContractNumber + ", " : "") +
            (fiscalPeriodCode != null ? "fiscalPeriodCode=" + fiscalPeriodCode + ", " : "") +
            (fiscalPeriodEndDate != null ? "fiscalPeriodEndDate=" + fiscalPeriodEndDate + ", " : "") +
            (assetCategoryName != null ? "assetCategoryName=" + assetCategoryName + ", " : "") +
            (debitAccountNumber != null ? "debitAccountNumber=" + debitAccountNumber + ", " : "") +
            (creditAccountNumber != null ? "creditAccountNumber=" + creditAccountNumber + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (shortTitle != null ? "shortTitle=" + shortTitle + ", " : "") +
            (rouAssetIdentifier != null ? "rouAssetIdentifier=" + rouAssetIdentifier + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (depreciationAmount != null ? "depreciationAmount=" + depreciationAmount + ", " : "") +
            (outstandingAmount != null ? "outstandingAmount=" + outstandingAmount + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
