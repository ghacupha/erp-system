package io.github.erp.service.criteria;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.DepreciationEntry} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationEntryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter postedAt;

    private BigDecimalFilter depreciationAmount;

    private LongFilter assetNumber;

    private UUIDFilter depreciationPeriodIdentifier;

    private UUIDFilter depreciationJobIdentifier;

    private UUIDFilter fiscalMonthIdentifier;

    private UUIDFilter fiscalQuarterIdentifier;

    private IntegerFilter batchSequenceNumber;

    private StringFilter processedItems;

    private IntegerFilter totalItemsProcessed;

    private LongFilter elapsedMonths;

    private LongFilter priorMonths;

    private BigDecimalFilter usefulLifeYears;

    private BigDecimalFilter previousNBV;

    private BigDecimalFilter netBookValue;

    private LocalDateFilter depreciationPeriodStartDate;

    private LocalDateFilter depreciationPeriodEndDate;

    private LocalDateFilter capitalizationDate;

    private LongFilter serviceOutletId;

    private LongFilter assetCategoryId;

    private LongFilter depreciationMethodId;

    private LongFilter assetRegistrationId;

    private LongFilter depreciationPeriodId;

    private LongFilter fiscalMonthId;

    private LongFilter fiscalQuarterId;

    private LongFilter fiscalYearId;

    private LongFilter depreciationJobId;

    private LongFilter depreciationBatchSequenceId;

    private Boolean distinct;

    public DepreciationEntryCriteria() {}

    public DepreciationEntryCriteria(DepreciationEntryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.postedAt = other.postedAt == null ? null : other.postedAt.copy();
        this.depreciationAmount = other.depreciationAmount == null ? null : other.depreciationAmount.copy();
        this.assetNumber = other.assetNumber == null ? null : other.assetNumber.copy();
        this.depreciationPeriodIdentifier = other.depreciationPeriodIdentifier == null ? null : other.depreciationPeriodIdentifier.copy();
        this.depreciationJobIdentifier = other.depreciationJobIdentifier == null ? null : other.depreciationJobIdentifier.copy();
        this.fiscalMonthIdentifier = other.fiscalMonthIdentifier == null ? null : other.fiscalMonthIdentifier.copy();
        this.fiscalQuarterIdentifier = other.fiscalQuarterIdentifier == null ? null : other.fiscalQuarterIdentifier.copy();
        this.batchSequenceNumber = other.batchSequenceNumber == null ? null : other.batchSequenceNumber.copy();
        this.processedItems = other.processedItems == null ? null : other.processedItems.copy();
        this.totalItemsProcessed = other.totalItemsProcessed == null ? null : other.totalItemsProcessed.copy();
        this.elapsedMonths = other.elapsedMonths == null ? null : other.elapsedMonths.copy();
        this.priorMonths = other.priorMonths == null ? null : other.priorMonths.copy();
        this.usefulLifeYears = other.usefulLifeYears == null ? null : other.usefulLifeYears.copy();
        this.previousNBV = other.previousNBV == null ? null : other.previousNBV.copy();
        this.netBookValue = other.netBookValue == null ? null : other.netBookValue.copy();
        this.depreciationPeriodStartDate = other.depreciationPeriodStartDate == null ? null : other.depreciationPeriodStartDate.copy();
        this.depreciationPeriodEndDate = other.depreciationPeriodEndDate == null ? null : other.depreciationPeriodEndDate.copy();
        this.capitalizationDate = other.capitalizationDate == null ? null : other.capitalizationDate.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.depreciationMethodId = other.depreciationMethodId == null ? null : other.depreciationMethodId.copy();
        this.assetRegistrationId = other.assetRegistrationId == null ? null : other.assetRegistrationId.copy();
        this.depreciationPeriodId = other.depreciationPeriodId == null ? null : other.depreciationPeriodId.copy();
        this.fiscalMonthId = other.fiscalMonthId == null ? null : other.fiscalMonthId.copy();
        this.fiscalQuarterId = other.fiscalQuarterId == null ? null : other.fiscalQuarterId.copy();
        this.fiscalYearId = other.fiscalYearId == null ? null : other.fiscalYearId.copy();
        this.depreciationJobId = other.depreciationJobId == null ? null : other.depreciationJobId.copy();
        this.depreciationBatchSequenceId = other.depreciationBatchSequenceId == null ? null : other.depreciationBatchSequenceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationEntryCriteria copy() {
        return new DepreciationEntryCriteria(this);
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

    public ZonedDateTimeFilter getPostedAt() {
        return postedAt;
    }

    public ZonedDateTimeFilter postedAt() {
        if (postedAt == null) {
            postedAt = new ZonedDateTimeFilter();
        }
        return postedAt;
    }

    public void setPostedAt(ZonedDateTimeFilter postedAt) {
        this.postedAt = postedAt;
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

    public UUIDFilter getDepreciationPeriodIdentifier() {
        return depreciationPeriodIdentifier;
    }

    public UUIDFilter depreciationPeriodIdentifier() {
        if (depreciationPeriodIdentifier == null) {
            depreciationPeriodIdentifier = new UUIDFilter();
        }
        return depreciationPeriodIdentifier;
    }

    public void setDepreciationPeriodIdentifier(UUIDFilter depreciationPeriodIdentifier) {
        this.depreciationPeriodIdentifier = depreciationPeriodIdentifier;
    }

    public UUIDFilter getDepreciationJobIdentifier() {
        return depreciationJobIdentifier;
    }

    public UUIDFilter depreciationJobIdentifier() {
        if (depreciationJobIdentifier == null) {
            depreciationJobIdentifier = new UUIDFilter();
        }
        return depreciationJobIdentifier;
    }

    public void setDepreciationJobIdentifier(UUIDFilter depreciationJobIdentifier) {
        this.depreciationJobIdentifier = depreciationJobIdentifier;
    }

    public UUIDFilter getFiscalMonthIdentifier() {
        return fiscalMonthIdentifier;
    }

    public UUIDFilter fiscalMonthIdentifier() {
        if (fiscalMonthIdentifier == null) {
            fiscalMonthIdentifier = new UUIDFilter();
        }
        return fiscalMonthIdentifier;
    }

    public void setFiscalMonthIdentifier(UUIDFilter fiscalMonthIdentifier) {
        this.fiscalMonthIdentifier = fiscalMonthIdentifier;
    }

    public UUIDFilter getFiscalQuarterIdentifier() {
        return fiscalQuarterIdentifier;
    }

    public UUIDFilter fiscalQuarterIdentifier() {
        if (fiscalQuarterIdentifier == null) {
            fiscalQuarterIdentifier = new UUIDFilter();
        }
        return fiscalQuarterIdentifier;
    }

    public void setFiscalQuarterIdentifier(UUIDFilter fiscalQuarterIdentifier) {
        this.fiscalQuarterIdentifier = fiscalQuarterIdentifier;
    }

    public IntegerFilter getBatchSequenceNumber() {
        return batchSequenceNumber;
    }

    public IntegerFilter batchSequenceNumber() {
        if (batchSequenceNumber == null) {
            batchSequenceNumber = new IntegerFilter();
        }
        return batchSequenceNumber;
    }

    public void setBatchSequenceNumber(IntegerFilter batchSequenceNumber) {
        this.batchSequenceNumber = batchSequenceNumber;
    }

    public StringFilter getProcessedItems() {
        return processedItems;
    }

    public StringFilter processedItems() {
        if (processedItems == null) {
            processedItems = new StringFilter();
        }
        return processedItems;
    }

    public void setProcessedItems(StringFilter processedItems) {
        this.processedItems = processedItems;
    }

    public IntegerFilter getTotalItemsProcessed() {
        return totalItemsProcessed;
    }

    public IntegerFilter totalItemsProcessed() {
        if (totalItemsProcessed == null) {
            totalItemsProcessed = new IntegerFilter();
        }
        return totalItemsProcessed;
    }

    public void setTotalItemsProcessed(IntegerFilter totalItemsProcessed) {
        this.totalItemsProcessed = totalItemsProcessed;
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

    public LocalDateFilter getCapitalizationDate() {
        return capitalizationDate;
    }

    public LocalDateFilter capitalizationDate() {
        if (capitalizationDate == null) {
            capitalizationDate = new LocalDateFilter();
        }
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDateFilter capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public LongFilter getServiceOutletId() {
        return serviceOutletId;
    }

    public LongFilter serviceOutletId() {
        if (serviceOutletId == null) {
            serviceOutletId = new LongFilter();
        }
        return serviceOutletId;
    }

    public void setServiceOutletId(LongFilter serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
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

    public LongFilter getDepreciationMethodId() {
        return depreciationMethodId;
    }

    public LongFilter depreciationMethodId() {
        if (depreciationMethodId == null) {
            depreciationMethodId = new LongFilter();
        }
        return depreciationMethodId;
    }

    public void setDepreciationMethodId(LongFilter depreciationMethodId) {
        this.depreciationMethodId = depreciationMethodId;
    }

    public LongFilter getAssetRegistrationId() {
        return assetRegistrationId;
    }

    public LongFilter assetRegistrationId() {
        if (assetRegistrationId == null) {
            assetRegistrationId = new LongFilter();
        }
        return assetRegistrationId;
    }

    public void setAssetRegistrationId(LongFilter assetRegistrationId) {
        this.assetRegistrationId = assetRegistrationId;
    }

    public LongFilter getDepreciationPeriodId() {
        return depreciationPeriodId;
    }

    public LongFilter depreciationPeriodId() {
        if (depreciationPeriodId == null) {
            depreciationPeriodId = new LongFilter();
        }
        return depreciationPeriodId;
    }

    public void setDepreciationPeriodId(LongFilter depreciationPeriodId) {
        this.depreciationPeriodId = depreciationPeriodId;
    }

    public LongFilter getFiscalMonthId() {
        return fiscalMonthId;
    }

    public LongFilter fiscalMonthId() {
        if (fiscalMonthId == null) {
            fiscalMonthId = new LongFilter();
        }
        return fiscalMonthId;
    }

    public void setFiscalMonthId(LongFilter fiscalMonthId) {
        this.fiscalMonthId = fiscalMonthId;
    }

    public LongFilter getFiscalQuarterId() {
        return fiscalQuarterId;
    }

    public LongFilter fiscalQuarterId() {
        if (fiscalQuarterId == null) {
            fiscalQuarterId = new LongFilter();
        }
        return fiscalQuarterId;
    }

    public void setFiscalQuarterId(LongFilter fiscalQuarterId) {
        this.fiscalQuarterId = fiscalQuarterId;
    }

    public LongFilter getFiscalYearId() {
        return fiscalYearId;
    }

    public LongFilter fiscalYearId() {
        if (fiscalYearId == null) {
            fiscalYearId = new LongFilter();
        }
        return fiscalYearId;
    }

    public void setFiscalYearId(LongFilter fiscalYearId) {
        this.fiscalYearId = fiscalYearId;
    }

    public LongFilter getDepreciationJobId() {
        return depreciationJobId;
    }

    public LongFilter depreciationJobId() {
        if (depreciationJobId == null) {
            depreciationJobId = new LongFilter();
        }
        return depreciationJobId;
    }

    public void setDepreciationJobId(LongFilter depreciationJobId) {
        this.depreciationJobId = depreciationJobId;
    }

    public LongFilter getDepreciationBatchSequenceId() {
        return depreciationBatchSequenceId;
    }

    public LongFilter depreciationBatchSequenceId() {
        if (depreciationBatchSequenceId == null) {
            depreciationBatchSequenceId = new LongFilter();
        }
        return depreciationBatchSequenceId;
    }

    public void setDepreciationBatchSequenceId(LongFilter depreciationBatchSequenceId) {
        this.depreciationBatchSequenceId = depreciationBatchSequenceId;
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
        final DepreciationEntryCriteria that = (DepreciationEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(postedAt, that.postedAt) &&
            Objects.equals(depreciationAmount, that.depreciationAmount) &&
            Objects.equals(assetNumber, that.assetNumber) &&
            Objects.equals(depreciationPeriodIdentifier, that.depreciationPeriodIdentifier) &&
            Objects.equals(depreciationJobIdentifier, that.depreciationJobIdentifier) &&
            Objects.equals(fiscalMonthIdentifier, that.fiscalMonthIdentifier) &&
            Objects.equals(fiscalQuarterIdentifier, that.fiscalQuarterIdentifier) &&
            Objects.equals(batchSequenceNumber, that.batchSequenceNumber) &&
            Objects.equals(processedItems, that.processedItems) &&
            Objects.equals(totalItemsProcessed, that.totalItemsProcessed) &&
            Objects.equals(elapsedMonths, that.elapsedMonths) &&
            Objects.equals(priorMonths, that.priorMonths) &&
            Objects.equals(usefulLifeYears, that.usefulLifeYears) &&
            Objects.equals(previousNBV, that.previousNBV) &&
            Objects.equals(netBookValue, that.netBookValue) &&
            Objects.equals(depreciationPeriodStartDate, that.depreciationPeriodStartDate) &&
            Objects.equals(depreciationPeriodEndDate, that.depreciationPeriodEndDate) &&
            Objects.equals(capitalizationDate, that.capitalizationDate) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(depreciationMethodId, that.depreciationMethodId) &&
            Objects.equals(assetRegistrationId, that.assetRegistrationId) &&
            Objects.equals(depreciationPeriodId, that.depreciationPeriodId) &&
            Objects.equals(fiscalMonthId, that.fiscalMonthId) &&
            Objects.equals(fiscalQuarterId, that.fiscalQuarterId) &&
            Objects.equals(fiscalYearId, that.fiscalYearId) &&
            Objects.equals(depreciationJobId, that.depreciationJobId) &&
            Objects.equals(depreciationBatchSequenceId, that.depreciationBatchSequenceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            postedAt,
            depreciationAmount,
            assetNumber,
            depreciationPeriodIdentifier,
            depreciationJobIdentifier,
            fiscalMonthIdentifier,
            fiscalQuarterIdentifier,
            batchSequenceNumber,
            processedItems,
            totalItemsProcessed,
            elapsedMonths,
            priorMonths,
            usefulLifeYears,
            previousNBV,
            netBookValue,
            depreciationPeriodStartDate,
            depreciationPeriodEndDate,
            capitalizationDate,
            serviceOutletId,
            assetCategoryId,
            depreciationMethodId,
            assetRegistrationId,
            depreciationPeriodId,
            fiscalMonthId,
            fiscalQuarterId,
            fiscalYearId,
            depreciationJobId,
            depreciationBatchSequenceId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationEntryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (postedAt != null ? "postedAt=" + postedAt + ", " : "") +
            (depreciationAmount != null ? "depreciationAmount=" + depreciationAmount + ", " : "") +
            (assetNumber != null ? "assetNumber=" + assetNumber + ", " : "") +
            (depreciationPeriodIdentifier != null ? "depreciationPeriodIdentifier=" + depreciationPeriodIdentifier + ", " : "") +
            (depreciationJobIdentifier != null ? "depreciationJobIdentifier=" + depreciationJobIdentifier + ", " : "") +
            (fiscalMonthIdentifier != null ? "fiscalMonthIdentifier=" + fiscalMonthIdentifier + ", " : "") +
            (fiscalQuarterIdentifier != null ? "fiscalQuarterIdentifier=" + fiscalQuarterIdentifier + ", " : "") +
            (batchSequenceNumber != null ? "batchSequenceNumber=" + batchSequenceNumber + ", " : "") +
            (processedItems != null ? "processedItems=" + processedItems + ", " : "") +
            (totalItemsProcessed != null ? "totalItemsProcessed=" + totalItemsProcessed + ", " : "") +
            (elapsedMonths != null ? "elapsedMonths=" + elapsedMonths + ", " : "") +
            (priorMonths != null ? "priorMonths=" + priorMonths + ", " : "") +
            (usefulLifeYears != null ? "usefulLifeYears=" + usefulLifeYears + ", " : "") +
            (previousNBV != null ? "previousNBV=" + previousNBV + ", " : "") +
            (netBookValue != null ? "netBookValue=" + netBookValue + ", " : "") +
            (depreciationPeriodStartDate != null ? "depreciationPeriodStartDate=" + depreciationPeriodStartDate + ", " : "") +
            (depreciationPeriodEndDate != null ? "depreciationPeriodEndDate=" + depreciationPeriodEndDate + ", " : "") +
            (capitalizationDate != null ? "capitalizationDate=" + capitalizationDate + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (depreciationMethodId != null ? "depreciationMethodId=" + depreciationMethodId + ", " : "") +
            (assetRegistrationId != null ? "assetRegistrationId=" + assetRegistrationId + ", " : "") +
            (depreciationPeriodId != null ? "depreciationPeriodId=" + depreciationPeriodId + ", " : "") +
            (fiscalMonthId != null ? "fiscalMonthId=" + fiscalMonthId + ", " : "") +
            (fiscalQuarterId != null ? "fiscalQuarterId=" + fiscalQuarterId + ", " : "") +
            (fiscalYearId != null ? "fiscalYearId=" + fiscalYearId + ", " : "") +
            (depreciationJobId != null ? "depreciationJobId=" + depreciationJobId + ", " : "") +
            (depreciationBatchSequenceId != null ? "depreciationBatchSequenceId=" + depreciationBatchSequenceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
