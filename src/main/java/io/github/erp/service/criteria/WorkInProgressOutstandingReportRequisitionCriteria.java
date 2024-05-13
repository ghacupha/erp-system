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
 * Criteria class for the {@link io.github.erp.domain.WorkInProgressOutstandingReportRequisition} entity. This class is used
 * in {@link io.github.erp.web.rest.WorkInProgressOutstandingReportRequisitionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /work-in-progress-outstanding-report-requisitions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkInProgressOutstandingReportRequisitionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter requestId;

    private LocalDateFilter reportDate;

    private ZonedDateTimeFilter timeOfRequisition;

    private StringFilter fileChecksum;

    private BooleanFilter tampered;

    private UUIDFilter filename;

    private StringFilter reportParameters;

    private LongFilter requestedById;

    private LongFilter lastAccessedById;

    private Boolean distinct;

    public WorkInProgressOutstandingReportRequisitionCriteria() {}

    public WorkInProgressOutstandingReportRequisitionCriteria(WorkInProgressOutstandingReportRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestId = other.requestId == null ? null : other.requestId.copy();
        this.reportDate = other.reportDate == null ? null : other.reportDate.copy();
        this.timeOfRequisition = other.timeOfRequisition == null ? null : other.timeOfRequisition.copy();
        this.fileChecksum = other.fileChecksum == null ? null : other.fileChecksum.copy();
        this.tampered = other.tampered == null ? null : other.tampered.copy();
        this.filename = other.filename == null ? null : other.filename.copy();
        this.reportParameters = other.reportParameters == null ? null : other.reportParameters.copy();
        this.requestedById = other.requestedById == null ? null : other.requestedById.copy();
        this.lastAccessedById = other.lastAccessedById == null ? null : other.lastAccessedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WorkInProgressOutstandingReportRequisitionCriteria copy() {
        return new WorkInProgressOutstandingReportRequisitionCriteria(this);
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

    public UUIDFilter getRequestId() {
        return requestId;
    }

    public UUIDFilter requestId() {
        if (requestId == null) {
            requestId = new UUIDFilter();
        }
        return requestId;
    }

    public void setRequestId(UUIDFilter requestId) {
        this.requestId = requestId;
    }

    public LocalDateFilter getReportDate() {
        return reportDate;
    }

    public LocalDateFilter reportDate() {
        if (reportDate == null) {
            reportDate = new LocalDateFilter();
        }
        return reportDate;
    }

    public void setReportDate(LocalDateFilter reportDate) {
        this.reportDate = reportDate;
    }

    public ZonedDateTimeFilter getTimeOfRequisition() {
        return timeOfRequisition;
    }

    public ZonedDateTimeFilter timeOfRequisition() {
        if (timeOfRequisition == null) {
            timeOfRequisition = new ZonedDateTimeFilter();
        }
        return timeOfRequisition;
    }

    public void setTimeOfRequisition(ZonedDateTimeFilter timeOfRequisition) {
        this.timeOfRequisition = timeOfRequisition;
    }

    public StringFilter getFileChecksum() {
        return fileChecksum;
    }

    public StringFilter fileChecksum() {
        if (fileChecksum == null) {
            fileChecksum = new StringFilter();
        }
        return fileChecksum;
    }

    public void setFileChecksum(StringFilter fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    public BooleanFilter getTampered() {
        return tampered;
    }

    public BooleanFilter tampered() {
        if (tampered == null) {
            tampered = new BooleanFilter();
        }
        return tampered;
    }

    public void setTampered(BooleanFilter tampered) {
        this.tampered = tampered;
    }

    public UUIDFilter getFilename() {
        return filename;
    }

    public UUIDFilter filename() {
        if (filename == null) {
            filename = new UUIDFilter();
        }
        return filename;
    }

    public void setFilename(UUIDFilter filename) {
        this.filename = filename;
    }

    public StringFilter getReportParameters() {
        return reportParameters;
    }

    public StringFilter reportParameters() {
        if (reportParameters == null) {
            reportParameters = new StringFilter();
        }
        return reportParameters;
    }

    public void setReportParameters(StringFilter reportParameters) {
        this.reportParameters = reportParameters;
    }

    public LongFilter getRequestedById() {
        return requestedById;
    }

    public LongFilter requestedById() {
        if (requestedById == null) {
            requestedById = new LongFilter();
        }
        return requestedById;
    }

    public void setRequestedById(LongFilter requestedById) {
        this.requestedById = requestedById;
    }

    public LongFilter getLastAccessedById() {
        return lastAccessedById;
    }

    public LongFilter lastAccessedById() {
        if (lastAccessedById == null) {
            lastAccessedById = new LongFilter();
        }
        return lastAccessedById;
    }

    public void setLastAccessedById(LongFilter lastAccessedById) {
        this.lastAccessedById = lastAccessedById;
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
        final WorkInProgressOutstandingReportRequisitionCriteria that = (WorkInProgressOutstandingReportRequisitionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requestId, that.requestId) &&
            Objects.equals(reportDate, that.reportDate) &&
            Objects.equals(timeOfRequisition, that.timeOfRequisition) &&
            Objects.equals(fileChecksum, that.fileChecksum) &&
            Objects.equals(tampered, that.tampered) &&
            Objects.equals(filename, that.filename) &&
            Objects.equals(reportParameters, that.reportParameters) &&
            Objects.equals(requestedById, that.requestedById) &&
            Objects.equals(lastAccessedById, that.lastAccessedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            requestId,
            reportDate,
            timeOfRequisition,
            fileChecksum,
            tampered,
            filename,
            reportParameters,
            requestedById,
            lastAccessedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressOutstandingReportRequisitionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requestId != null ? "requestId=" + requestId + ", " : "") +
            (reportDate != null ? "reportDate=" + reportDate + ", " : "") +
            (timeOfRequisition != null ? "timeOfRequisition=" + timeOfRequisition + ", " : "") +
            (fileChecksum != null ? "fileChecksum=" + fileChecksum + ", " : "") +
            (tampered != null ? "tampered=" + tampered + ", " : "") +
            (filename != null ? "filename=" + filename + ", " : "") +
            (reportParameters != null ? "reportParameters=" + reportParameters + ", " : "") +
            (requestedById != null ? "requestedById=" + requestedById + ", " : "") +
            (lastAccessedById != null ? "lastAccessedById=" + lastAccessedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
