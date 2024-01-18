package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

/**
 * Criteria class for the {@link io.github.erp.domain.ReportContentType} entity. This class is used
 * in {@link io.github.erp.web.rest.ReportContentTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /report-content-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportContentTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reportTypeName;

    private StringFilter reportFileExtension;

    private LongFilter systemContentTypeId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public ReportContentTypeCriteria() {}

    public ReportContentTypeCriteria(ReportContentTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportTypeName = other.reportTypeName == null ? null : other.reportTypeName.copy();
        this.reportFileExtension = other.reportFileExtension == null ? null : other.reportFileExtension.copy();
        this.systemContentTypeId = other.systemContentTypeId == null ? null : other.systemContentTypeId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportContentTypeCriteria copy() {
        return new ReportContentTypeCriteria(this);
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

    public StringFilter getReportTypeName() {
        return reportTypeName;
    }

    public StringFilter reportTypeName() {
        if (reportTypeName == null) {
            reportTypeName = new StringFilter();
        }
        return reportTypeName;
    }

    public void setReportTypeName(StringFilter reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public StringFilter getReportFileExtension() {
        return reportFileExtension;
    }

    public StringFilter reportFileExtension() {
        if (reportFileExtension == null) {
            reportFileExtension = new StringFilter();
        }
        return reportFileExtension;
    }

    public void setReportFileExtension(StringFilter reportFileExtension) {
        this.reportFileExtension = reportFileExtension;
    }

    public LongFilter getSystemContentTypeId() {
        return systemContentTypeId;
    }

    public LongFilter systemContentTypeId() {
        if (systemContentTypeId == null) {
            systemContentTypeId = new LongFilter();
        }
        return systemContentTypeId;
    }

    public void setSystemContentTypeId(LongFilter systemContentTypeId) {
        this.systemContentTypeId = systemContentTypeId;
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
        final ReportContentTypeCriteria that = (ReportContentTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportTypeName, that.reportTypeName) &&
            Objects.equals(reportFileExtension, that.reportFileExtension) &&
            Objects.equals(systemContentTypeId, that.systemContentTypeId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportTypeName, reportFileExtension, systemContentTypeId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportContentTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportTypeName != null ? "reportTypeName=" + reportTypeName + ", " : "") +
            (reportFileExtension != null ? "reportFileExtension=" + reportFileExtension + ", " : "") +
            (systemContentTypeId != null ? "systemContentTypeId=" + systemContentTypeId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
