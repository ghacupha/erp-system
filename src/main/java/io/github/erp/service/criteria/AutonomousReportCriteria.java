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
 * Criteria class for the {@link io.github.erp.domain.AutonomousReport} entity. This class is used
 * in {@link io.github.erp.web.rest.AutonomousReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /autonomous-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AutonomousReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reportName;

    private StringFilter reportParameters;

    private ZonedDateTimeFilter createdAt;

    private UUIDFilter reportFilename;

    private StringFilter fileChecksum;

    private BooleanFilter reportTampered;

    private LongFilter reportMappingId;

    private LongFilter placeholderId;

    private LongFilter createdById;

    private Boolean distinct;

    public AutonomousReportCriteria() {}

    public AutonomousReportCriteria(AutonomousReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportName = other.reportName == null ? null : other.reportName.copy();
        this.reportParameters = other.reportParameters == null ? null : other.reportParameters.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.reportFilename = other.reportFilename == null ? null : other.reportFilename.copy();
        this.fileChecksum = other.fileChecksum == null ? null : other.fileChecksum.copy();
        this.reportTampered = other.reportTampered == null ? null : other.reportTampered.copy();
        this.reportMappingId = other.reportMappingId == null ? null : other.reportMappingId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AutonomousReportCriteria copy() {
        return new AutonomousReportCriteria(this);
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

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public UUIDFilter getReportFilename() {
        return reportFilename;
    }

    public UUIDFilter reportFilename() {
        if (reportFilename == null) {
            reportFilename = new UUIDFilter();
        }
        return reportFilename;
    }

    public void setReportFilename(UUIDFilter reportFilename) {
        this.reportFilename = reportFilename;
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

    public BooleanFilter getReportTampered() {
        return reportTampered;
    }

    public BooleanFilter reportTampered() {
        if (reportTampered == null) {
            reportTampered = new BooleanFilter();
        }
        return reportTampered;
    }

    public void setReportTampered(BooleanFilter reportTampered) {
        this.reportTampered = reportTampered;
    }

    public LongFilter getReportMappingId() {
        return reportMappingId;
    }

    public LongFilter reportMappingId() {
        if (reportMappingId == null) {
            reportMappingId = new LongFilter();
        }
        return reportMappingId;
    }

    public void setReportMappingId(LongFilter reportMappingId) {
        this.reportMappingId = reportMappingId;
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

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
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
        final AutonomousReportCriteria that = (AutonomousReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportName, that.reportName) &&
            Objects.equals(reportParameters, that.reportParameters) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(reportFilename, that.reportFilename) &&
            Objects.equals(fileChecksum, that.fileChecksum) &&
            Objects.equals(reportTampered, that.reportTampered) &&
            Objects.equals(reportMappingId, that.reportMappingId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reportName,
            reportParameters,
            createdAt,
            reportFilename,
            fileChecksum,
            reportTampered,
            reportMappingId,
            placeholderId,
            createdById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutonomousReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportName != null ? "reportName=" + reportName + ", " : "") +
            (reportParameters != null ? "reportParameters=" + reportParameters + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (reportFilename != null ? "reportFilename=" + reportFilename + ", " : "") +
            (fileChecksum != null ? "fileChecksum=" + fileChecksum + ", " : "") +
            (reportTampered != null ? "reportTampered=" + reportTampered + ", " : "") +
            (reportMappingId != null ? "reportMappingId=" + reportMappingId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
