package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.PdfReportRequisition} entity. This class is used
 * in {@link io.github.erp.web.rest.PdfReportRequisitionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pdf-report-requisitions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PdfReportRequisitionCriteria implements Serializable, Criteria {

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

    private LocalDateFilter reportDate;

    private StringFilter userPassword;

    private StringFilter ownerPassword;

    private StringFilter reportFileChecksum;

    private ReportStatusTypesFilter reportStatus;

    private UUIDFilter reportId;

    private LongFilter reportTemplateId;

    private LongFilter placeholderId;

    private LongFilter parametersId;

    private Boolean distinct;

    public PdfReportRequisitionCriteria() {}

    public PdfReportRequisitionCriteria(PdfReportRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportName = other.reportName == null ? null : other.reportName.copy();
        this.reportDate = other.reportDate == null ? null : other.reportDate.copy();
        this.userPassword = other.userPassword == null ? null : other.userPassword.copy();
        this.ownerPassword = other.ownerPassword == null ? null : other.ownerPassword.copy();
        this.reportFileChecksum = other.reportFileChecksum == null ? null : other.reportFileChecksum.copy();
        this.reportStatus = other.reportStatus == null ? null : other.reportStatus.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.reportTemplateId = other.reportTemplateId == null ? null : other.reportTemplateId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.parametersId = other.parametersId == null ? null : other.parametersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PdfReportRequisitionCriteria copy() {
        return new PdfReportRequisitionCriteria(this);
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

    public StringFilter getUserPassword() {
        return userPassword;
    }

    public StringFilter userPassword() {
        if (userPassword == null) {
            userPassword = new StringFilter();
        }
        return userPassword;
    }

    public void setUserPassword(StringFilter userPassword) {
        this.userPassword = userPassword;
    }

    public StringFilter getOwnerPassword() {
        return ownerPassword;
    }

    public StringFilter ownerPassword() {
        if (ownerPassword == null) {
            ownerPassword = new StringFilter();
        }
        return ownerPassword;
    }

    public void setOwnerPassword(StringFilter ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    public StringFilter getReportFileChecksum() {
        return reportFileChecksum;
    }

    public StringFilter reportFileChecksum() {
        if (reportFileChecksum == null) {
            reportFileChecksum = new StringFilter();
        }
        return reportFileChecksum;
    }

    public void setReportFileChecksum(StringFilter reportFileChecksum) {
        this.reportFileChecksum = reportFileChecksum;
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
        final PdfReportRequisitionCriteria that = (PdfReportRequisitionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportName, that.reportName) &&
            Objects.equals(reportDate, that.reportDate) &&
            Objects.equals(userPassword, that.userPassword) &&
            Objects.equals(ownerPassword, that.ownerPassword) &&
            Objects.equals(reportFileChecksum, that.reportFileChecksum) &&
            Objects.equals(reportStatus, that.reportStatus) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(reportTemplateId, that.reportTemplateId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(parametersId, that.parametersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportName,
            reportDate,
            userPassword,
            ownerPassword,
            reportFileChecksum,
            reportStatus,
            reportId,
            reportTemplateId,
            placeholderId,
            parametersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PdfReportRequisitionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportName != null ? "reportName=" + reportName + ", " : "") +
            (reportDate != null ? "reportDate=" + reportDate + ", " : "") +
            (userPassword != null ? "userPassword=" + userPassword + ", " : "") +
            (ownerPassword != null ? "ownerPassword=" + ownerPassword + ", " : "") +
            (reportFileChecksum != null ? "reportFileChecksum=" + reportFileChecksum + ", " : "") +
            (reportStatus != null ? "reportStatus=" + reportStatus + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            (reportTemplateId != null ? "reportTemplateId=" + reportTemplateId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (parametersId != null ? "parametersId=" + parametersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
