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

import io.github.erp.domain.enumeration.NVBCompilationStatus;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.NbvCompilationJob} entity. This class is used
 * in {@link io.github.erp.web.rest.NbvCompilationJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /nbv-compilation-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NbvCompilationJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NVBCompilationStatus
     */
    public static class NVBCompilationStatusFilter extends Filter<NVBCompilationStatus> {

        public NVBCompilationStatusFilter() {}

        public NVBCompilationStatusFilter(NVBCompilationStatusFilter filter) {
            super(filter);
        }

        @Override
        public NVBCompilationStatusFilter copy() {
            return new NVBCompilationStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter compilationJobIdentifier;

    private ZonedDateTimeFilter compilationJobTimeOfRequest;

    private IntegerFilter entitiesAffected;

    private NVBCompilationStatusFilter compilationStatus;

    private StringFilter jobTitle;

    private IntegerFilter numberOfBatches;

    private IntegerFilter numberOfProcessedBatches;

    private DurationFilter processingTime;

    private LongFilter activePeriodId;

    private LongFilter initiatedById;

    private Boolean distinct;

    public NbvCompilationJobCriteria() {}

    public NbvCompilationJobCriteria(NbvCompilationJobCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.compilationJobIdentifier = other.compilationJobIdentifier == null ? null : other.compilationJobIdentifier.copy();
        this.compilationJobTimeOfRequest = other.compilationJobTimeOfRequest == null ? null : other.compilationJobTimeOfRequest.copy();
        this.entitiesAffected = other.entitiesAffected == null ? null : other.entitiesAffected.copy();
        this.compilationStatus = other.compilationStatus == null ? null : other.compilationStatus.copy();
        this.jobTitle = other.jobTitle == null ? null : other.jobTitle.copy();
        this.numberOfBatches = other.numberOfBatches == null ? null : other.numberOfBatches.copy();
        this.numberOfProcessedBatches = other.numberOfProcessedBatches == null ? null : other.numberOfProcessedBatches.copy();
        this.processingTime = other.processingTime == null ? null : other.processingTime.copy();
        this.activePeriodId = other.activePeriodId == null ? null : other.activePeriodId.copy();
        this.initiatedById = other.initiatedById == null ? null : other.initiatedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NbvCompilationJobCriteria copy() {
        return new NbvCompilationJobCriteria(this);
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

    public UUIDFilter getCompilationJobIdentifier() {
        return compilationJobIdentifier;
    }

    public UUIDFilter compilationJobIdentifier() {
        if (compilationJobIdentifier == null) {
            compilationJobIdentifier = new UUIDFilter();
        }
        return compilationJobIdentifier;
    }

    public void setCompilationJobIdentifier(UUIDFilter compilationJobIdentifier) {
        this.compilationJobIdentifier = compilationJobIdentifier;
    }

    public ZonedDateTimeFilter getCompilationJobTimeOfRequest() {
        return compilationJobTimeOfRequest;
    }

    public ZonedDateTimeFilter compilationJobTimeOfRequest() {
        if (compilationJobTimeOfRequest == null) {
            compilationJobTimeOfRequest = new ZonedDateTimeFilter();
        }
        return compilationJobTimeOfRequest;
    }

    public void setCompilationJobTimeOfRequest(ZonedDateTimeFilter compilationJobTimeOfRequest) {
        this.compilationJobTimeOfRequest = compilationJobTimeOfRequest;
    }

    public IntegerFilter getEntitiesAffected() {
        return entitiesAffected;
    }

    public IntegerFilter entitiesAffected() {
        if (entitiesAffected == null) {
            entitiesAffected = new IntegerFilter();
        }
        return entitiesAffected;
    }

    public void setEntitiesAffected(IntegerFilter entitiesAffected) {
        this.entitiesAffected = entitiesAffected;
    }

    public NVBCompilationStatusFilter getCompilationStatus() {
        return compilationStatus;
    }

    public NVBCompilationStatusFilter compilationStatus() {
        if (compilationStatus == null) {
            compilationStatus = new NVBCompilationStatusFilter();
        }
        return compilationStatus;
    }

    public void setCompilationStatus(NVBCompilationStatusFilter compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            jobTitle = new StringFilter();
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public IntegerFilter getNumberOfBatches() {
        return numberOfBatches;
    }

    public IntegerFilter numberOfBatches() {
        if (numberOfBatches == null) {
            numberOfBatches = new IntegerFilter();
        }
        return numberOfBatches;
    }

    public void setNumberOfBatches(IntegerFilter numberOfBatches) {
        this.numberOfBatches = numberOfBatches;
    }

    public IntegerFilter getNumberOfProcessedBatches() {
        return numberOfProcessedBatches;
    }

    public IntegerFilter numberOfProcessedBatches() {
        if (numberOfProcessedBatches == null) {
            numberOfProcessedBatches = new IntegerFilter();
        }
        return numberOfProcessedBatches;
    }

    public void setNumberOfProcessedBatches(IntegerFilter numberOfProcessedBatches) {
        this.numberOfProcessedBatches = numberOfProcessedBatches;
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

    public LongFilter getActivePeriodId() {
        return activePeriodId;
    }

    public LongFilter activePeriodId() {
        if (activePeriodId == null) {
            activePeriodId = new LongFilter();
        }
        return activePeriodId;
    }

    public void setActivePeriodId(LongFilter activePeriodId) {
        this.activePeriodId = activePeriodId;
    }

    public LongFilter getInitiatedById() {
        return initiatedById;
    }

    public LongFilter initiatedById() {
        if (initiatedById == null) {
            initiatedById = new LongFilter();
        }
        return initiatedById;
    }

    public void setInitiatedById(LongFilter initiatedById) {
        this.initiatedById = initiatedById;
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
        final NbvCompilationJobCriteria that = (NbvCompilationJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(compilationJobIdentifier, that.compilationJobIdentifier) &&
            Objects.equals(compilationJobTimeOfRequest, that.compilationJobTimeOfRequest) &&
            Objects.equals(entitiesAffected, that.entitiesAffected) &&
            Objects.equals(compilationStatus, that.compilationStatus) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(numberOfBatches, that.numberOfBatches) &&
            Objects.equals(numberOfProcessedBatches, that.numberOfProcessedBatches) &&
            Objects.equals(processingTime, that.processingTime) &&
            Objects.equals(activePeriodId, that.activePeriodId) &&
            Objects.equals(initiatedById, that.initiatedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            compilationJobIdentifier,
            compilationJobTimeOfRequest,
            entitiesAffected,
            compilationStatus,
            jobTitle,
            numberOfBatches,
            numberOfProcessedBatches,
            processingTime,
            activePeriodId,
            initiatedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NbvCompilationJobCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (compilationJobIdentifier != null ? "compilationJobIdentifier=" + compilationJobIdentifier + ", " : "") +
            (compilationJobTimeOfRequest != null ? "compilationJobTimeOfRequest=" + compilationJobTimeOfRequest + ", " : "") +
            (entitiesAffected != null ? "entitiesAffected=" + entitiesAffected + ", " : "") +
            (compilationStatus != null ? "compilationStatus=" + compilationStatus + ", " : "") +
            (jobTitle != null ? "jobTitle=" + jobTitle + ", " : "") +
            (numberOfBatches != null ? "numberOfBatches=" + numberOfBatches + ", " : "") +
            (numberOfProcessedBatches != null ? "numberOfProcessedBatches=" + numberOfProcessedBatches + ", " : "") +
            (processingTime != null ? "processingTime=" + processingTime + ", " : "") +
            (activePeriodId != null ? "activePeriodId=" + activePeriodId + ", " : "") +
            (initiatedById != null ? "initiatedById=" + initiatedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
