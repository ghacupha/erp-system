package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 3 (Caleb Series) Server ver 0.1.3-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.ReportStatusTypes;
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
 * Criteria class for the {@link io.github.erp.domain.ReportRequisition} entity. This class is used
 * in {@link io.github.erp.web.rest.ReportRequisitionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /report-requisitions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportRequisitionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReportStatusTypes
     */
    public static class ReportStatusTypesFilter extends Filter<ReportStatusTypes> {

        public ReportStatusTypesFilter() {}

        public ReportStatusTypesFilter(ReportStatusTypesFilter filter) {
            super(filter);
        }

        @Override
        public ReportStatusTypesFilter copy() {
            return new ReportStatusTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reportName;

    private ZonedDateTimeFilter reportRequestTime;

    private StringFilter reportPassword;

    private ReportStatusTypesFilter reportStatus;

    private UUIDFilter reportId;

    private LongFilter placeholdersId;

    private LongFilter parametersId;

    private LongFilter reportTemplateId;

    private LongFilter reportContentTypeId;

    private Boolean distinct;

    public ReportRequisitionCriteria() {}

    public ReportRequisitionCriteria(ReportRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportName = other.reportName == null ? null : other.reportName.copy();
        this.reportRequestTime = other.reportRequestTime == null ? null : other.reportRequestTime.copy();
        this.reportPassword = other.reportPassword == null ? null : other.reportPassword.copy();
        this.reportStatus = other.reportStatus == null ? null : other.reportStatus.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.placeholdersId = other.placeholdersId == null ? null : other.placeholdersId.copy();
        this.parametersId = other.parametersId == null ? null : other.parametersId.copy();
        this.reportTemplateId = other.reportTemplateId == null ? null : other.reportTemplateId.copy();
        this.reportContentTypeId = other.reportContentTypeId == null ? null : other.reportContentTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportRequisitionCriteria copy() {
        return new ReportRequisitionCriteria(this);
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

    public ZonedDateTimeFilter getReportRequestTime() {
        return reportRequestTime;
    }

    public ZonedDateTimeFilter reportRequestTime() {
        if (reportRequestTime == null) {
            reportRequestTime = new ZonedDateTimeFilter();
        }
        return reportRequestTime;
    }

    public void setReportRequestTime(ZonedDateTimeFilter reportRequestTime) {
        this.reportRequestTime = reportRequestTime;
    }

    public StringFilter getReportPassword() {
        return reportPassword;
    }

    public StringFilter reportPassword() {
        if (reportPassword == null) {
            reportPassword = new StringFilter();
        }
        return reportPassword;
    }

    public void setReportPassword(StringFilter reportPassword) {
        this.reportPassword = reportPassword;
    }

    public ReportStatusTypesFilter getReportStatus() {
        return reportStatus;
    }

    public ReportStatusTypesFilter reportStatus() {
        if (reportStatus == null) {
            reportStatus = new ReportStatusTypesFilter();
        }
        return reportStatus;
    }

    public void setReportStatus(ReportStatusTypesFilter reportStatus) {
        this.reportStatus = reportStatus;
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

    public LongFilter getPlaceholdersId() {
        return placeholdersId;
    }

    public LongFilter placeholdersId() {
        if (placeholdersId == null) {
            placeholdersId = new LongFilter();
        }
        return placeholdersId;
    }

    public void setPlaceholdersId(LongFilter placeholdersId) {
        this.placeholdersId = placeholdersId;
    }

    public LongFilter getParametersId() {
        return parametersId;
    }

    public LongFilter parametersId() {
        if (parametersId == null) {
            parametersId = new LongFilter();
        }
        return parametersId;
    }

    public void setParametersId(LongFilter parametersId) {
        this.parametersId = parametersId;
    }

    public LongFilter getReportTemplateId() {
        return reportTemplateId;
    }

    public LongFilter reportTemplateId() {
        if (reportTemplateId == null) {
            reportTemplateId = new LongFilter();
        }
        return reportTemplateId;
    }

    public void setReportTemplateId(LongFilter reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    public LongFilter getReportContentTypeId() {
        return reportContentTypeId;
    }

    public LongFilter reportContentTypeId() {
        if (reportContentTypeId == null) {
            reportContentTypeId = new LongFilter();
        }
        return reportContentTypeId;
    }

    public void setReportContentTypeId(LongFilter reportContentTypeId) {
        this.reportContentTypeId = reportContentTypeId;
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
        final ReportRequisitionCriteria that = (ReportRequisitionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportName, that.reportName) &&
            Objects.equals(reportRequestTime, that.reportRequestTime) &&
            Objects.equals(reportPassword, that.reportPassword) &&
            Objects.equals(reportStatus, that.reportStatus) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(placeholdersId, that.placeholdersId) &&
            Objects.equals(parametersId, that.parametersId) &&
            Objects.equals(reportTemplateId, that.reportTemplateId) &&
            Objects.equals(reportContentTypeId, that.reportContentTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportName,
            reportRequestTime,
            reportPassword,
            reportStatus,
            reportId,
            placeholdersId,
            parametersId,
            reportTemplateId,
            reportContentTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportRequisitionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportName != null ? "reportName=" + reportName + ", " : "") +
            (reportRequestTime != null ? "reportRequestTime=" + reportRequestTime + ", " : "") +
            (reportPassword != null ? "reportPassword=" + reportPassword + ", " : "") +
            (reportStatus != null ? "reportStatus=" + reportStatus + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            (placeholdersId != null ? "placeholdersId=" + placeholdersId + ", " : "") +
            (parametersId != null ? "parametersId=" + parametersId + ", " : "") +
            (reportTemplateId != null ? "reportTemplateId=" + reportTemplateId + ", " : "") +
            (reportContentTypeId != null ? "reportContentTypeId=" + reportContentTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
