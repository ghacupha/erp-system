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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.DurationFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.DepreciationBatchSequence} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationBatchSequenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-batch-sequences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationBatchSequenceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DepreciationBatchStatusType
     */
    public static class DepreciationBatchStatusTypeFilter extends Filter<DepreciationBatchStatusType> {

        public DepreciationBatchStatusTypeFilter() {}

        public DepreciationBatchStatusTypeFilter(DepreciationBatchStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public DepreciationBatchStatusTypeFilter copy() {
            return new DepreciationBatchStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter startIndex;

    private IntegerFilter endIndex;

    private DepreciationBatchStatusTypeFilter depreciationBatchStatus;

    private UUIDFilter depreciationPeriodIdentifier;

    private UUIDFilter depreciationJobIdentifier;

    private UUIDFilter fiscalMonthIdentifier;

    private UUIDFilter fiscalQuarterIdentifier;

    private IntegerFilter batchSize;

    private IntegerFilter processedItems;

    private IntegerFilter sequenceNumber;

    private BooleanFilter isLastBatch;

    private DurationFilter processingTime;

    private IntegerFilter totalItems;

    private LongFilter depreciationJobId;

    private Boolean distinct;

    public DepreciationBatchSequenceCriteria() {}

    public DepreciationBatchSequenceCriteria(DepreciationBatchSequenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startIndex = other.startIndex == null ? null : other.startIndex.copy();
        this.endIndex = other.endIndex == null ? null : other.endIndex.copy();
        this.depreciationBatchStatus = other.depreciationBatchStatus == null ? null : other.depreciationBatchStatus.copy();
        this.depreciationPeriodIdentifier = other.depreciationPeriodIdentifier == null ? null : other.depreciationPeriodIdentifier.copy();
        this.depreciationJobIdentifier = other.depreciationJobIdentifier == null ? null : other.depreciationJobIdentifier.copy();
        this.fiscalMonthIdentifier = other.fiscalMonthIdentifier == null ? null : other.fiscalMonthIdentifier.copy();
        this.fiscalQuarterIdentifier = other.fiscalQuarterIdentifier == null ? null : other.fiscalQuarterIdentifier.copy();
        this.batchSize = other.batchSize == null ? null : other.batchSize.copy();
        this.processedItems = other.processedItems == null ? null : other.processedItems.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.isLastBatch = other.isLastBatch == null ? null : other.isLastBatch.copy();
        this.processingTime = other.processingTime == null ? null : other.processingTime.copy();
        this.totalItems = other.totalItems == null ? null : other.totalItems.copy();
        this.depreciationJobId = other.depreciationJobId == null ? null : other.depreciationJobId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationBatchSequenceCriteria copy() {
        return new DepreciationBatchSequenceCriteria(this);
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

    public IntegerFilter getStartIndex() {
        return startIndex;
    }

    public IntegerFilter startIndex() {
        if (startIndex == null) {
            startIndex = new IntegerFilter();
        }
        return startIndex;
    }

    public void setStartIndex(IntegerFilter startIndex) {
        this.startIndex = startIndex;
    }

    public IntegerFilter getEndIndex() {
        return endIndex;
    }

    public IntegerFilter endIndex() {
        if (endIndex == null) {
            endIndex = new IntegerFilter();
        }
        return endIndex;
    }

    public void setEndIndex(IntegerFilter endIndex) {
        this.endIndex = endIndex;
    }

    public DepreciationBatchStatusTypeFilter getDepreciationBatchStatus() {
        return depreciationBatchStatus;
    }

    public DepreciationBatchStatusTypeFilter depreciationBatchStatus() {
        if (depreciationBatchStatus == null) {
            depreciationBatchStatus = new DepreciationBatchStatusTypeFilter();
        }
        return depreciationBatchStatus;
    }

    public void setDepreciationBatchStatus(DepreciationBatchStatusTypeFilter depreciationBatchStatus) {
        this.depreciationBatchStatus = depreciationBatchStatus;
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

    public IntegerFilter getBatchSize() {
        return batchSize;
    }

    public IntegerFilter batchSize() {
        if (batchSize == null) {
            batchSize = new IntegerFilter();
        }
        return batchSize;
    }

    public void setBatchSize(IntegerFilter batchSize) {
        this.batchSize = batchSize;
    }

    public IntegerFilter getProcessedItems() {
        return processedItems;
    }

    public IntegerFilter processedItems() {
        if (processedItems == null) {
            processedItems = new IntegerFilter();
        }
        return processedItems;
    }

    public void setProcessedItems(IntegerFilter processedItems) {
        this.processedItems = processedItems;
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

    public BooleanFilter getIsLastBatch() {
        return isLastBatch;
    }

    public BooleanFilter isLastBatch() {
        if (isLastBatch == null) {
            isLastBatch = new BooleanFilter();
        }
        return isLastBatch;
    }

    public void setIsLastBatch(BooleanFilter isLastBatch) {
        this.isLastBatch = isLastBatch;
    }

    public DurationFilter getProcessingTime() {
        return processingTime;
    }

    public DurationFilter processingTime() {
        if (processingTime == null) {
            processingTime = new DurationFilter();
        }
        return processingTime;
    }

    public void setProcessingTime(DurationFilter processingTime) {
        this.processingTime = processingTime;
    }

    public IntegerFilter getTotalItems() {
        return totalItems;
    }

    public IntegerFilter totalItems() {
        if (totalItems == null) {
            totalItems = new IntegerFilter();
        }
        return totalItems;
    }

    public void setTotalItems(IntegerFilter totalItems) {
        this.totalItems = totalItems;
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
        final DepreciationBatchSequenceCriteria that = (DepreciationBatchSequenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startIndex, that.startIndex) &&
            Objects.equals(endIndex, that.endIndex) &&
            Objects.equals(depreciationBatchStatus, that.depreciationBatchStatus) &&
            Objects.equals(depreciationPeriodIdentifier, that.depreciationPeriodIdentifier) &&
            Objects.equals(depreciationJobIdentifier, that.depreciationJobIdentifier) &&
            Objects.equals(fiscalMonthIdentifier, that.fiscalMonthIdentifier) &&
            Objects.equals(fiscalQuarterIdentifier, that.fiscalQuarterIdentifier) &&
            Objects.equals(batchSize, that.batchSize) &&
            Objects.equals(processedItems, that.processedItems) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(isLastBatch, that.isLastBatch) &&
            Objects.equals(processingTime, that.processingTime) &&
            Objects.equals(totalItems, that.totalItems) &&
            Objects.equals(depreciationJobId, that.depreciationJobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startIndex,
            endIndex,
            depreciationBatchStatus,
            depreciationPeriodIdentifier,
            depreciationJobIdentifier,
            fiscalMonthIdentifier,
            fiscalQuarterIdentifier,
            batchSize,
            processedItems,
            sequenceNumber,
            isLastBatch,
            processingTime,
            totalItems,
            depreciationJobId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationBatchSequenceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startIndex != null ? "startIndex=" + startIndex + ", " : "") +
            (endIndex != null ? "endIndex=" + endIndex + ", " : "") +
            (depreciationBatchStatus != null ? "depreciationBatchStatus=" + depreciationBatchStatus + ", " : "") +
            (depreciationPeriodIdentifier != null ? "depreciationPeriodIdentifier=" + depreciationPeriodIdentifier + ", " : "") +
            (depreciationJobIdentifier != null ? "depreciationJobIdentifier=" + depreciationJobIdentifier + ", " : "") +
            (fiscalMonthIdentifier != null ? "fiscalMonthIdentifier=" + fiscalMonthIdentifier + ", " : "") +
            (fiscalQuarterIdentifier != null ? "fiscalQuarterIdentifier=" + fiscalQuarterIdentifier + ", " : "") +
            (batchSize != null ? "batchSize=" + batchSize + ", " : "") +
            (processedItems != null ? "processedItems=" + processedItems + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (isLastBatch != null ? "isLastBatch=" + isLastBatch + ", " : "") +
            (processingTime != null ? "processingTime=" + processingTime + ", " : "") +
            (totalItems != null ? "totalItems=" + totalItems + ", " : "") +
            (depreciationJobId != null ? "depreciationJobId=" + depreciationJobId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
