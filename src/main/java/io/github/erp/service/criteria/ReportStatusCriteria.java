package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.0-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

/**
 * Criteria class for the {@link io.github.erp.domain.ReportStatus} entity. This class is used
 * in {@link io.github.erp.web.rest.ReportStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /report-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reportName;

    private UUIDFilter reportId;

    private LongFilter placeholderId;

    private LongFilter processStatusId;

    private Boolean distinct;

    public ReportStatusCriteria() {}

    public ReportStatusCriteria(ReportStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportName = other.reportName == null ? null : other.reportName.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.processStatusId = other.processStatusId == null ? null : other.processStatusId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportStatusCriteria copy() {
        return new ReportStatusCriteria(this);
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

    public StringFilter getReportName() {
        return reportName;
    }

    public StringFilter reportName() {
        if (reportName == null) {
            reportName = new StringFilter();
        }
        return reportName;
    }

    public void setReportName(StringFilter reportName) {
        this.reportName = reportName;
    }

    public UUIDFilter getReportId() {
        return reportId;
    }

    public UUIDFilter reportId() {
        if (reportId == null) {
            reportId = new UUIDFilter();
        }
        return reportId;
    }

    public void setReportId(UUIDFilter reportId) {
        this.reportId = reportId;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public LongFilter getProcessStatusId() {
        return processStatusId;
    }

    public LongFilter processStatusId() {
        if (processStatusId == null) {
            processStatusId = new LongFilter();
        }
        return processStatusId;
    }

    public void setProcessStatusId(LongFilter processStatusId) {
        this.processStatusId = processStatusId;
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
        final ReportStatusCriteria that = (ReportStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportName, that.reportName) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(processStatusId, that.processStatusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportName, reportId, placeholderId, processStatusId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportStatusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportName != null ? "reportName=" + reportName + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (processStatusId != null ? "processStatusId=" + processStatusId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
