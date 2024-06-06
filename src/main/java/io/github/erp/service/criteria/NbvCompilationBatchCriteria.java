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
import io.github.erp.domain.enumeration.CompilationBatchStatusTypes;
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
 * Criteria class for the {@link io.github.erp.domain.NbvCompilationBatch} entity. This class is used
 * in {@link io.github.erp.web.rest.NbvCompilationBatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /nbv-compilation-batches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NbvCompilationBatchCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CompilationBatchStatusTypes
     */
    public static class CompilationBatchStatusTypesFilter extends Filter<CompilationBatchStatusTypes> {

        public CompilationBatchStatusTypesFilter() {}

        public CompilationBatchStatusTypesFilter(CompilationBatchStatusTypesFilter filter) {
            super(filter);
        }

        @Override
        public CompilationBatchStatusTypesFilter copy() {
            return new CompilationBatchStatusTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter startIndex;

    private IntegerFilter endIndex;

    private CompilationBatchStatusTypesFilter compilationBatchStatus;

    private UUIDFilter compilationBatchIdentifier;

    private UUIDFilter compilationJobidentifier;

    private UUIDFilter depreciationPeriodIdentifier;

    private UUIDFilter fiscalMonthIdentifier;

    private IntegerFilter batchSize;

    private IntegerFilter processedItems;

    private IntegerFilter sequenceNumber;

    private BooleanFilter isLastBatch;

    private DurationFilter processingTime;

    private IntegerFilter totalItems;

    private LongFilter nbvCompilationJobId;

    private Boolean distinct;

    public NbvCompilationBatchCriteria() {}

    public NbvCompilationBatchCriteria(NbvCompilationBatchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startIndex = other.startIndex == null ? null : other.startIndex.copy();
        this.endIndex = other.endIndex == null ? null : other.endIndex.copy();
        this.compilationBatchStatus = other.compilationBatchStatus == null ? null : other.compilationBatchStatus.copy();
        this.compilationBatchIdentifier = other.compilationBatchIdentifier == null ? null : other.compilationBatchIdentifier.copy();
        this.compilationJobidentifier = other.compilationJobidentifier == null ? null : other.compilationJobidentifier.copy();
        this.depreciationPeriodIdentifier = other.depreciationPeriodIdentifier == null ? null : other.depreciationPeriodIdentifier.copy();
        this.fiscalMonthIdentifier = other.fiscalMonthIdentifier == null ? null : other.fiscalMonthIdentifier.copy();
        this.batchSize = other.batchSize == null ? null : other.batchSize.copy();
        this.processedItems = other.processedItems == null ? null : other.processedItems.copy();
        this.sequenceNumber = other.sequenceNumber == null ? null : other.sequenceNumber.copy();
        this.isLastBatch = other.isLastBatch == null ? null : other.isLastBatch.copy();
        this.processingTime = other.processingTime == null ? null : other.processingTime.copy();
        this.totalItems = other.totalItems == null ? null : other.totalItems.copy();
        this.nbvCompilationJobId = other.nbvCompilationJobId == null ? null : other.nbvCompilationJobId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NbvCompilationBatchCriteria copy() {
        return new NbvCompilationBatchCriteria(this);
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

    public CompilationBatchStatusTypesFilter getCompilationBatchStatus() {
        return compilationBatchStatus;
    }

    public CompilationBatchStatusTypesFilter compilationBatchStatus() {
        if (compilationBatchStatus == null) {
            compilationBatchStatus = new CompilationBatchStatusTypesFilter();
        }
        return compilationBatchStatus;
    }

    public void setCompilationBatchStatus(CompilationBatchStatusTypesFilter compilationBatchStatus) {
        this.compilationBatchStatus = compilationBatchStatus;
    }

    public UUIDFilter getCompilationBatchIdentifier() {
        return compilationBatchIdentifier;
    }

    public UUIDFilter compilationBatchIdentifier() {
        if (compilationBatchIdentifier == null) {
            compilationBatchIdentifier = new UUIDFilter();
        }
        return compilationBatchIdentifier;
    }

    public void setCompilationBatchIdentifier(UUIDFilter compilationBatchIdentifier) {
        this.compilationBatchIdentifier = compilationBatchIdentifier;
    }

    public UUIDFilter getCompilationJobidentifier() {
        return compilationJobidentifier;
    }

    public UUIDFilter compilationJobidentifier() {
        if (compilationJobidentifier == null) {
            compilationJobidentifier = new UUIDFilter();
        }
        return compilationJobidentifier;
    }

    public void setCompilationJobidentifier(UUIDFilter compilationJobidentifier) {
        this.compilationJobidentifier = compilationJobidentifier;
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

    public LongFilter getNbvCompilationJobId() {
        return nbvCompilationJobId;
    }

    public LongFilter nbvCompilationJobId() {
        if (nbvCompilationJobId == null) {
            nbvCompilationJobId = new LongFilter();
        }
        return nbvCompilationJobId;
    }

    public void setNbvCompilationJobId(LongFilter nbvCompilationJobId) {
        this.nbvCompilationJobId = nbvCompilationJobId;
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
        final NbvCompilationBatchCriteria that = (NbvCompilationBatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startIndex, that.startIndex) &&
            Objects.equals(endIndex, that.endIndex) &&
            Objects.equals(compilationBatchStatus, that.compilationBatchStatus) &&
            Objects.equals(compilationBatchIdentifier, that.compilationBatchIdentifier) &&
            Objects.equals(compilationJobidentifier, that.compilationJobidentifier) &&
            Objects.equals(depreciationPeriodIdentifier, that.depreciationPeriodIdentifier) &&
            Objects.equals(fiscalMonthIdentifier, that.fiscalMonthIdentifier) &&
            Objects.equals(batchSize, that.batchSize) &&
            Objects.equals(processedItems, that.processedItems) &&
            Objects.equals(sequenceNumber, that.sequenceNumber) &&
            Objects.equals(isLastBatch, that.isLastBatch) &&
            Objects.equals(processingTime, that.processingTime) &&
            Objects.equals(totalItems, that.totalItems) &&
            Objects.equals(nbvCompilationJobId, that.nbvCompilationJobId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startIndex,
            endIndex,
            compilationBatchStatus,
            compilationBatchIdentifier,
            compilationJobidentifier,
            depreciationPeriodIdentifier,
            fiscalMonthIdentifier,
            batchSize,
            processedItems,
            sequenceNumber,
            isLastBatch,
            processingTime,
            totalItems,
            nbvCompilationJobId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NbvCompilationBatchCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startIndex != null ? "startIndex=" + startIndex + ", " : "") +
            (endIndex != null ? "endIndex=" + endIndex + ", " : "") +
            (compilationBatchStatus != null ? "compilationBatchStatus=" + compilationBatchStatus + ", " : "") +
            (compilationBatchIdentifier != null ? "compilationBatchIdentifier=" + compilationBatchIdentifier + ", " : "") +
            (compilationJobidentifier != null ? "compilationJobidentifier=" + compilationJobidentifier + ", " : "") +
            (depreciationPeriodIdentifier != null ? "depreciationPeriodIdentifier=" + depreciationPeriodIdentifier + ", " : "") +
            (fiscalMonthIdentifier != null ? "fiscalMonthIdentifier=" + fiscalMonthIdentifier + ", " : "") +
            (batchSize != null ? "batchSize=" + batchSize + ", " : "") +
            (processedItems != null ? "processedItems=" + processedItems + ", " : "") +
            (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber + ", " : "") +
            (isLastBatch != null ? "isLastBatch=" + isLastBatch + ", " : "") +
            (processingTime != null ? "processingTime=" + processingTime + ", " : "") +
            (totalItems != null ? "totalItems=" + totalItems + ", " : "") +
            (nbvCompilationJobId != null ? "nbvCompilationJobId=" + nbvCompilationJobId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
