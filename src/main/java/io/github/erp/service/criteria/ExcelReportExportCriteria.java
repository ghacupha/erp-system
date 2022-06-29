package io.github.erp.service.criteria;

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
 * Criteria class for the {@link io.github.erp.domain.ExcelReportExport} entity. This class is used
 * in {@link io.github.erp.web.rest.ExcelReportExportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /excel-report-exports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExcelReportExportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reportName;

    private ZonedDateTimeFilter reportTimeStamp;

    private UUIDFilter reportId;

    private LongFilter placeholderId;

    private LongFilter parametersId;

    private LongFilter reportStatusId;

    private Boolean distinct;

    public ExcelReportExportCriteria() {}

    public ExcelReportExportCriteria(ExcelReportExportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportName = other.reportName == null ? null : other.reportName.copy();
        this.reportTimeStamp = other.reportTimeStamp == null ? null : other.reportTimeStamp.copy();
        this.reportId = other.reportId == null ? null : other.reportId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.parametersId = other.parametersId == null ? null : other.parametersId.copy();
        this.reportStatusId = other.reportStatusId == null ? null : other.reportStatusId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExcelReportExportCriteria copy() {
        return new ExcelReportExportCriteria(this);
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

    public ZonedDateTimeFilter getReportTimeStamp() {
        return reportTimeStamp;
    }

    public ZonedDateTimeFilter reportTimeStamp() {
        if (reportTimeStamp == null) {
            reportTimeStamp = new ZonedDateTimeFilter();
        }
        return reportTimeStamp;
    }

    public void setReportTimeStamp(ZonedDateTimeFilter reportTimeStamp) {
        this.reportTimeStamp = reportTimeStamp;
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

    public LongFilter getReportStatusId() {
        return reportStatusId;
    }

    public LongFilter reportStatusId() {
        if (reportStatusId == null) {
            reportStatusId = new LongFilter();
        }
        return reportStatusId;
    }

    public void setReportStatusId(LongFilter reportStatusId) {
        this.reportStatusId = reportStatusId;
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
        final ExcelReportExportCriteria that = (ExcelReportExportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportName, that.reportName) &&
            Objects.equals(reportTimeStamp, that.reportTimeStamp) &&
            Objects.equals(reportId, that.reportId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(parametersId, that.parametersId) &&
            Objects.equals(reportStatusId, that.reportStatusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportName, reportTimeStamp, reportId, placeholderId, parametersId, reportStatusId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExcelReportExportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportName != null ? "reportName=" + reportName + ", " : "") +
            (reportTimeStamp != null ? "reportTimeStamp=" + reportTimeStamp + ", " : "") +
            (reportId != null ? "reportId=" + reportId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (parametersId != null ? "parametersId=" + parametersId + ", " : "") +
            (reportStatusId != null ? "reportStatusId=" + reportStatusId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
