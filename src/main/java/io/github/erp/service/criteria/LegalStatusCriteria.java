package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
 * Criteria class for the {@link io.github.erp.domain.LegalStatus} entity. This class is used
 * in {@link io.github.erp.web.rest.LegalStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /legal-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LegalStatusCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter legalStatusCode;

    private StringFilter legalStatusType;

    private Boolean distinct;

    public LegalStatusCriteria() {}

    public LegalStatusCriteria(LegalStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.legalStatusCode = other.legalStatusCode == null ? null : other.legalStatusCode.copy();
        this.legalStatusType = other.legalStatusType == null ? null : other.legalStatusType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LegalStatusCriteria copy() {
        return new LegalStatusCriteria(this);
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

    public StringFilter getLegalStatusCode() {
        return legalStatusCode;
    }

    public StringFilter legalStatusCode() {
        if (legalStatusCode == null) {
            legalStatusCode = new StringFilter();
        }
        return legalStatusCode;
    }

    public void setLegalStatusCode(StringFilter legalStatusCode) {
        this.legalStatusCode = legalStatusCode;
    }

    public StringFilter getLegalStatusType() {
        return legalStatusType;
    }

    public StringFilter legalStatusType() {
        if (legalStatusType == null) {
            legalStatusType = new StringFilter();
        }
        return legalStatusType;
    }

    public void setLegalStatusType(StringFilter legalStatusType) {
        this.legalStatusType = legalStatusType;
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
        final LegalStatusCriteria that = (LegalStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(legalStatusCode, that.legalStatusCode) &&
            Objects.equals(legalStatusType, that.legalStatusType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, legalStatusCode, legalStatusType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LegalStatusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (legalStatusCode != null ? "legalStatusCode=" + legalStatusCode + ", " : "") +
            (legalStatusType != null ? "legalStatusType=" + legalStatusType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
