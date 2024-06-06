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
 * Criteria class for the {@link io.github.erp.domain.AmortizationPostingReportRequisition} entity. This class is used
 * in {@link io.github.erp.web.rest.AmortizationPostingReportRequisitionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /amortization-posting-report-requisitions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AmortizationPostingReportRequisitionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter requestId;

    private ZonedDateTimeFilter timeOfRequisition;

    private StringFilter fileChecksum;

    private BooleanFilter tampered;

    private UUIDFilter filename;

    private StringFilter reportParameters;

    private LongFilter amortizationPeriodId;

    private LongFilter requestedById;

    private LongFilter lastAccessedById;

    private Boolean distinct;

    public AmortizationPostingReportRequisitionCriteria() {}

    public AmortizationPostingReportRequisitionCriteria(AmortizationPostingReportRequisitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestId = other.requestId == null ? null : other.requestId.copy();
        this.timeOfRequisition = other.timeOfRequisition == null ? null : other.timeOfRequisition.copy();
        this.fileChecksum = other.fileChecksum == null ? null : other.fileChecksum.copy();
        this.tampered = other.tampered == null ? null : other.tampered.copy();
        this.filename = other.filename == null ? null : other.filename.copy();
        this.reportParameters = other.reportParameters == null ? null : other.reportParameters.copy();
        this.amortizationPeriodId = other.amortizationPeriodId == null ? null : other.amortizationPeriodId.copy();
        this.requestedById = other.requestedById == null ? null : other.requestedById.copy();
        this.lastAccessedById = other.lastAccessedById == null ? null : other.lastAccessedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AmortizationPostingReportRequisitionCriteria copy() {
        return new AmortizationPostingReportRequisitionCriteria(this);
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

    public LongFilter getAmortizationPeriodId() {
        return amortizationPeriodId;
    }

    public LongFilter amortizationPeriodId() {
        if (amortizationPeriodId == null) {
            amortizationPeriodId = new LongFilter();
        }
        return amortizationPeriodId;
    }

    public void setAmortizationPeriodId(LongFilter amortizationPeriodId) {
        this.amortizationPeriodId = amortizationPeriodId;
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
        final AmortizationPostingReportRequisitionCriteria that = (AmortizationPostingReportRequisitionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requestId, that.requestId) &&
            Objects.equals(timeOfRequisition, that.timeOfRequisition) &&
            Objects.equals(fileChecksum, that.fileChecksum) &&
            Objects.equals(tampered, that.tampered) &&
            Objects.equals(filename, that.filename) &&
            Objects.equals(reportParameters, that.reportParameters) &&
            Objects.equals(amortizationPeriodId, that.amortizationPeriodId) &&
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
            timeOfRequisition,
            fileChecksum,
            tampered,
            filename,
            reportParameters,
            amortizationPeriodId,
            requestedById,
            lastAccessedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmortizationPostingReportRequisitionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requestId != null ? "requestId=" + requestId + ", " : "") +
            (timeOfRequisition != null ? "timeOfRequisition=" + timeOfRequisition + ", " : "") +
            (fileChecksum != null ? "fileChecksum=" + fileChecksum + ", " : "") +
            (tampered != null ? "tampered=" + tampered + ", " : "") +
            (filename != null ? "filename=" + filename + ", " : "") +
            (reportParameters != null ? "reportParameters=" + reportParameters + ", " : "") +
            (amortizationPeriodId != null ? "amortizationPeriodId=" + amortizationPeriodId + ", " : "") +
            (requestedById != null ? "requestedById=" + requestedById + ", " : "") +
            (lastAccessedById != null ? "lastAccessedById=" + lastAccessedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
