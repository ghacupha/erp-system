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

/**
 * Criteria class for the {@link io.github.erp.domain.ReportingEntity} entity. This class is used
 * in {@link io.github.erp.web.rest.ReportingEntityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reporting-entities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReportingEntityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter entityName;

    private LongFilter reportingCurrencyId;

    private LongFilter retainedEarningsAccountId;

    private Boolean distinct;

    public ReportingEntityCriteria() {}

    public ReportingEntityCriteria(ReportingEntityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entityName = other.entityName == null ? null : other.entityName.copy();
        this.reportingCurrencyId = other.reportingCurrencyId == null ? null : other.reportingCurrencyId.copy();
        this.retainedEarningsAccountId = other.retainedEarningsAccountId == null ? null : other.retainedEarningsAccountId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportingEntityCriteria copy() {
        return new ReportingEntityCriteria(this);
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

    public StringFilter getEntityName() {
        return entityName;
    }

    public StringFilter entityName() {
        if (entityName == null) {
            entityName = new StringFilter();
        }
        return entityName;
    }

    public void setEntityName(StringFilter entityName) {
        this.entityName = entityName;
    }

    public LongFilter getReportingCurrencyId() {
        return reportingCurrencyId;
    }

    public LongFilter reportingCurrencyId() {
        if (reportingCurrencyId == null) {
            reportingCurrencyId = new LongFilter();
        }
        return reportingCurrencyId;
    }

    public void setReportingCurrencyId(LongFilter reportingCurrencyId) {
        this.reportingCurrencyId = reportingCurrencyId;
    }

    public LongFilter getRetainedEarningsAccountId() {
        return retainedEarningsAccountId;
    }

    public LongFilter retainedEarningsAccountId() {
        if (retainedEarningsAccountId == null) {
            retainedEarningsAccountId = new LongFilter();
        }
        return retainedEarningsAccountId;
    }

    public void setRetainedEarningsAccountId(LongFilter retainedEarningsAccountId) {
        this.retainedEarningsAccountId = retainedEarningsAccountId;
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
        final ReportingEntityCriteria that = (ReportingEntityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entityName, that.entityName) &&
            Objects.equals(reportingCurrencyId, that.reportingCurrencyId) &&
            Objects.equals(retainedEarningsAccountId, that.retainedEarningsAccountId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, reportingCurrencyId, retainedEarningsAccountId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingEntityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entityName != null ? "entityName=" + entityName + ", " : "") +
            (reportingCurrencyId != null ? "reportingCurrencyId=" + reportingCurrencyId + ", " : "") +
            (retainedEarningsAccountId != null ? "retainedEarningsAccountId=" + retainedEarningsAccountId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
