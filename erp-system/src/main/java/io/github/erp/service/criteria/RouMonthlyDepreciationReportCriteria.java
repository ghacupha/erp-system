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
 * Criteria class for the {@link io.github.erp.domain.RouMonthlyDepreciationReport} entity. This class is used
 * in {@link io.github.erp.web.rest.RouMonthlyDepreciationReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-monthly-depreciation-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouMonthlyDepreciationReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter requestId;

    private ZonedDateTimeFilter timeOfRequest;

    private BooleanFilter reportIsCompiled;

    private StringFilter fileChecksum;

    private BooleanFilter tampered;

    private UUIDFilter filename;

    private StringFilter reportParameters;

    private LongFilter requestedById;

    private LongFilter reportingYearId;

    private Boolean distinct;

    public RouMonthlyDepreciationReportCriteria() {}

    public RouMonthlyDepreciationReportCriteria(RouMonthlyDepreciationReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestId = other.requestId == null ? null : other.requestId.copy();
        this.timeOfRequest = other.timeOfRequest == null ? null : other.timeOfRequest.copy();
        this.reportIsCompiled = other.reportIsCompiled == null ? null : other.reportIsCompiled.copy();
        this.fileChecksum = other.fileChecksum == null ? null : other.fileChecksum.copy();
        this.tampered = other.tampered == null ? null : other.tampered.copy();
        this.filename = other.filename == null ? null : other.filename.copy();
        this.reportParameters = other.reportParameters == null ? null : other.reportParameters.copy();
        this.requestedById = other.requestedById == null ? null : other.requestedById.copy();
        this.reportingYearId = other.reportingYearId == null ? null : other.reportingYearId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouMonthlyDepreciationReportCriteria copy() {
        return new RouMonthlyDepreciationReportCriteria(this);
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

    public ZonedDateTimeFilter getTimeOfRequest() {
        return timeOfRequest;
    }

    public ZonedDateTimeFilter timeOfRequest() {
        if (timeOfRequest == null) {
            timeOfRequest = new ZonedDateTimeFilter();
        }
        return timeOfRequest;
    }

    public void setTimeOfRequest(ZonedDateTimeFilter timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public BooleanFilter getReportIsCompiled() {
        return reportIsCompiled;
    }

    public BooleanFilter reportIsCompiled() {
        if (reportIsCompiled == null) {
            reportIsCompiled = new BooleanFilter();
        }
        return reportIsCompiled;
    }

    public void setReportIsCompiled(BooleanFilter reportIsCompiled) {
        this.reportIsCompiled = reportIsCompiled;
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

    public LongFilter getReportingYearId() {
        return reportingYearId;
    }

    public LongFilter reportingYearId() {
        if (reportingYearId == null) {
            reportingYearId = new LongFilter();
        }
        return reportingYearId;
    }

    public void setReportingYearId(LongFilter reportingYearId) {
        this.reportingYearId = reportingYearId;
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
        final RouMonthlyDepreciationReportCriteria that = (RouMonthlyDepreciationReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requestId, that.requestId) &&
            Objects.equals(timeOfRequest, that.timeOfRequest) &&
            Objects.equals(reportIsCompiled, that.reportIsCompiled) &&
            Objects.equals(fileChecksum, that.fileChecksum) &&
            Objects.equals(tampered, that.tampered) &&
            Objects.equals(filename, that.filename) &&
            Objects.equals(reportParameters, that.reportParameters) &&
            Objects.equals(requestedById, that.requestedById) &&
            Objects.equals(reportingYearId, that.reportingYearId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            requestId,
            timeOfRequest,
            reportIsCompiled,
            fileChecksum,
            tampered,
            filename,
            reportParameters,
            requestedById,
            reportingYearId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouMonthlyDepreciationReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requestId != null ? "requestId=" + requestId + ", " : "") +
            (timeOfRequest != null ? "timeOfRequest=" + timeOfRequest + ", " : "") +
            (reportIsCompiled != null ? "reportIsCompiled=" + reportIsCompiled + ", " : "") +
            (fileChecksum != null ? "fileChecksum=" + fileChecksum + ", " : "") +
            (tampered != null ? "tampered=" + tampered + ", " : "") +
            (filename != null ? "filename=" + filename + ", " : "") +
            (reportParameters != null ? "reportParameters=" + reportParameters + ", " : "") +
            (requestedById != null ? "requestedById=" + requestedById + ", " : "") +
            (reportingYearId != null ? "reportingYearId=" + reportingYearId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
