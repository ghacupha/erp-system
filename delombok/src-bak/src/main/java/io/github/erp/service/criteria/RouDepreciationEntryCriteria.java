package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.RouDepreciationEntry} entity. This class is used
 * in {@link io.github.erp.web.rest.RouDepreciationEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-depreciation-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouDepreciationEntryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private BigDecimalFilter depreciationAmount;

    private BigDecimalFilter outstandingAmount;

    private UUIDFilter rouAssetIdentifier;

    private UUIDFilter rouDepreciationIdentifier;

    private IntegerFilter sequenceNumber;

    private BooleanFilter activated;

    private BooleanFilter isDeleted;

    private UUIDFilter batchJobIdentifier;

    private UUIDFilter depreciationAmountStepIdentifier;

    private UUIDFilter outstandingAmountStepIdentifier;

    private UUIDFilter flagAmortisedStepIdentifier;

    private ZonedDateTimeFilter compilationTime;

    private BooleanFilter invalidated;

    private LongFilter debitAccountId;

    private LongFilter creditAccountId;

    private LongFilter assetCategoryId;

    private LongFilter leaseContractId;

    private LongFilter rouMetadataId;

    private LongFilter leasePeriodId;

    private Boolean distinct;

    public RouDepreciationEntryCriteria() {}

    public RouDepreciationEntryCriteria(RouDepreciationEntryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.depreciationAmount = other.depreciationAmount == null ? null : other.depreciationAmount.copy();
        this.outstandingAmount = other.outstandingAmount == null ? null : other.outstandingAmount.copy();
        this.rouAssetIdentifier = other.rouAssetIdentifier == null ? null : other.rouAssetIdentifier.copy();
        this.rouDepreciationIdentifier = other.rouDepreciationIdentifier == null ? null : other.rouDepreciationIdentifier.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.activated = other.activated == null ? null : other.activated.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.batchJobIdentifier = other.batchJobIdentifier == null ? null : other.batchJobIdentifier.copy();
        this.depreciationAmountStepIdentifier =
            other.depreciationAmountStepIdentifier == null ? null : other.depreciationAmountStepIdentifier.copy();
        this.outstandingAmountStepIdentifier =
            other.outstandingAmountStepIdentifier == null ? null : other.outstandingAmountStepIdentifier.copy();
        this.flagAmortisedStepIdentifier = other.flagAmortisedStepIdentifier == null ? null : other.flagAmortisedStepIdentifier.copy();
        this.compilationTime = other.compilationTime == null ? null : other.compilationTime.copy();
        this.invalidated = other.invalidated == null ? null : other.invalidated.copy();
        this.debitAccountId = other.debitAccountId == null ? null : other.debitAccountId.copy();
        this.creditAccountId = other.creditAccountId == null ? null : other.creditAccountId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.leaseContractId = other.leaseContractId == null ? null : other.leaseContractId.copy();
        this.rouMetadataId = other.rouMetadataId == null ? null : other.rouMetadataId.copy();
        this.leasePeriodId = other.leasePeriodId == null ? null : other.leasePeriodId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouDepreciationEntryCriteria copy() {
        return new RouDepreciationEntryCriteria(this);
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

    public UUIDFilter getRouAssetIdentifier() {
        return rouAssetIdentifier;
    }

    public UUIDFilter rouAssetIdentifier() {
        if (rouAssetIdentifier == null) {
            rouAssetIdentifier = new UUIDFilter();
        }
        return rouAssetIdentifier;
    }

    public void setRouAssetIdentifier(UUIDFilter rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public UUIDFilter getRouDepreciationIdentifier() {
        return rouDepreciationIdentifier;
    }

    public UUIDFilter rouDepreciationIdentifier() {
        if (rouDepreciationIdentifier == null) {
            rouDepreciationIdentifier = new UUIDFilter();
        }
        return rouDepreciationIdentifier;
    }

    public void setRouDepreciationIdentifier(UUIDFilter rouDepreciationIdentifier) {
        this.rouDepreciationIdentifier = rouDepreciationIdentifier;
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

    public BooleanFilter getActivated() {
        return activated;
    }

    public BooleanFilter activated() {
        if (activated == null) {
            activated = new BooleanFilter();
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UUIDFilter getBatchJobIdentifier() {
        return batchJobIdentifier;
    }

    public UUIDFilter batchJobIdentifier() {
        if (batchJobIdentifier == null) {
            batchJobIdentifier = new UUIDFilter();
        }
        return batchJobIdentifier;
    }

    public void setBatchJobIdentifier(UUIDFilter batchJobIdentifier) {
        this.batchJobIdentifier = batchJobIdentifier;
    }

    public UUIDFilter getDepreciationAmountStepIdentifier() {
        return depreciationAmountStepIdentifier;
    }

    public UUIDFilter depreciationAmountStepIdentifier() {
        if (depreciationAmountStepIdentifier == null) {
            depreciationAmountStepIdentifier = new UUIDFilter();
        }
        return depreciationAmountStepIdentifier;
    }

    public void setDepreciationAmountStepIdentifier(UUIDFilter depreciationAmountStepIdentifier) {
        this.depreciationAmountStepIdentifier = depreciationAmountStepIdentifier;
    }

    public UUIDFilter getOutstandingAmountStepIdentifier() {
        return outstandingAmountStepIdentifier;
    }

    public UUIDFilter outstandingAmountStepIdentifier() {
        if (outstandingAmountStepIdentifier == null) {
            outstandingAmountStepIdentifier = new UUIDFilter();
        }
        return outstandingAmountStepIdentifier;
    }

    public void setOutstandingAmountStepIdentifier(UUIDFilter outstandingAmountStepIdentifier) {
        this.outstandingAmountStepIdentifier = outstandingAmountStepIdentifier;
    }

    public UUIDFilter getFlagAmortisedStepIdentifier() {
        return flagAmortisedStepIdentifier;
    }

    public UUIDFilter flagAmortisedStepIdentifier() {
        if (flagAmortisedStepIdentifier == null) {
            flagAmortisedStepIdentifier = new UUIDFilter();
        }
        return flagAmortisedStepIdentifier;
    }

    public void setFlagAmortisedStepIdentifier(UUIDFilter flagAmortisedStepIdentifier) {
        this.flagAmortisedStepIdentifier = flagAmortisedStepIdentifier;
    }

    public ZonedDateTimeFilter getCompilationTime() {
        return compilationTime;
    }

    public ZonedDateTimeFilter compilationTime() {
        if (compilationTime == null) {
            compilationTime = new ZonedDateTimeFilter();
        }
        return compilationTime;
    }

    public void setCompilationTime(ZonedDateTimeFilter compilationTime) {
        this.compilationTime = compilationTime;
    }

    public BooleanFilter getInvalidated() {
        return invalidated;
    }

    public BooleanFilter invalidated() {
        if (invalidated == null) {
            invalidated = new BooleanFilter();
        }
        return invalidated;
    }

    public void setInvalidated(BooleanFilter invalidated) {
        this.invalidated = invalidated;
    }

    public LongFilter getDebitAccountId() {
        return debitAccountId;
    }

    public LongFilter debitAccountId() {
        if (debitAccountId == null) {
            debitAccountId = new LongFilter();
        }
        return debitAccountId;
    }

    public void setDebitAccountId(LongFilter debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public LongFilter getCreditAccountId() {
        return creditAccountId;
    }

    public LongFilter creditAccountId() {
        if (creditAccountId == null) {
            creditAccountId = new LongFilter();
        }
        return creditAccountId;
    }

    public void setCreditAccountId(LongFilter creditAccountId) {
        this.creditAccountId = creditAccountId;
    }

    public LongFilter getAssetCategoryId() {
        return assetCategoryId;
    }

    public LongFilter assetCategoryId() {
        if (assetCategoryId == null) {
            assetCategoryId = new LongFilter();
        }
        return assetCategoryId;
    }

    public void setAssetCategoryId(LongFilter assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public LongFilter getLeaseContractId() {
        return leaseContractId;
    }

    public LongFilter leaseContractId() {
        if (leaseContractId == null) {
            leaseContractId = new LongFilter();
        }
        return leaseContractId;
    }

    public void setLeaseContractId(LongFilter leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public LongFilter getRouMetadataId() {
        return rouMetadataId;
    }

    public LongFilter rouMetadataId() {
        if (rouMetadataId == null) {
            rouMetadataId = new LongFilter();
        }
        return rouMetadataId;
    }

    public void setRouMetadataId(LongFilter rouMetadataId) {
        this.rouMetadataId = rouMetadataId;
    }

    public LongFilter getLeasePeriodId() {
        return leasePeriodId;
    }

    public LongFilter leasePeriodId() {
        if (leasePeriodId == null) {
            leasePeriodId = new LongFilter();
        }
        return leasePeriodId;
    }

    public void setLeasePeriodId(LongFilter leasePeriodId) {
        this.leasePeriodId = leasePeriodId;
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
        final RouDepreciationEntryCriteria that = (RouDepreciationEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(depreciationAmount, that.depreciationAmount) &&
            Objects.equals(outstandingAmount, that.outstandingAmount) &&
            Objects.equals(rouAssetIdentifier, that.rouAssetIdentifier) &&
            Objects.equals(rouDepreciationIdentifier, that.rouDepreciationIdentifier) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(batchJobIdentifier, that.batchJobIdentifier) &&
            Objects.equals(depreciationAmountStepIdentifier, that.depreciationAmountStepIdentifier) &&
            Objects.equals(outstandingAmountStepIdentifier, that.outstandingAmountStepIdentifier) &&
            Objects.equals(flagAmortisedStepIdentifier, that.flagAmortisedStepIdentifier) &&
            Objects.equals(compilationTime, that.compilationTime) &&
            Objects.equals(invalidated, that.invalidated) &&
            Objects.equals(debitAccountId, that.debitAccountId) &&
            Objects.equals(creditAccountId, that.creditAccountId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(leaseContractId, that.leaseContractId) &&
            Objects.equals(rouMetadataId, that.rouMetadataId) &&
            Objects.equals(leasePeriodId, that.leasePeriodId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            depreciationAmount,
            outstandingAmount,
            rouAssetIdentifier,
            rouDepreciationIdentifier,
            sequenceNumber,
            activated,
            isDeleted,
            batchJobIdentifier,
            depreciationAmountStepIdentifier,
            outstandingAmountStepIdentifier,
            flagAmortisedStepIdentifier,
            compilationTime,
            invalidated,
            debitAccountId,
            creditAccountId,
            assetCategoryId,
            leaseContractId,
            rouMetadataId,
            leasePeriodId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationEntryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (depreciationAmount != null ? "depreciationAmount=" + depreciationAmount + ", " : "") +
            (outstandingAmount != null ? "outstandingAmount=" + outstandingAmount + ", " : "") +
            (rouAssetIdentifier != null ? "rouAssetIdentifier=" + rouAssetIdentifier + ", " : "") +
            (rouDepreciationIdentifier != null ? "rouDepreciationIdentifier=" + rouDepreciationIdentifier + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (activated != null ? "activated=" + activated + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (batchJobIdentifier != null ? "batchJobIdentifier=" + batchJobIdentifier + ", " : "") +
            (depreciationAmountStepIdentifier != null ? "depreciationAmountStepIdentifier=" + depreciationAmountStepIdentifier + ", " : "") +
            (outstandingAmountStepIdentifier != null ? "outstandingAmountStepIdentifier=" + outstandingAmountStepIdentifier + ", " : "") +
            (flagAmortisedStepIdentifier != null ? "flagAmortisedStepIdentifier=" + flagAmortisedStepIdentifier + ", " : "") +
            (compilationTime != null ? "compilationTime=" + compilationTime + ", " : "") +
            (invalidated != null ? "invalidated=" + invalidated + ", " : "") +
            (debitAccountId != null ? "debitAccountId=" + debitAccountId + ", " : "") +
            (creditAccountId != null ? "creditAccountId=" + creditAccountId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (leaseContractId != null ? "leaseContractId=" + leaseContractId + ", " : "") +
            (rouMetadataId != null ? "rouMetadataId=" + rouMetadataId + ", " : "") +
            (leasePeriodId != null ? "leasePeriodId=" + leasePeriodId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
