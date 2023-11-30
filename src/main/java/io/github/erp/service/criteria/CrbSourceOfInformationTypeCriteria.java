package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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

/**
 * Criteria class for the {@link io.github.erp.domain.CrbSourceOfInformationType} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbSourceOfInformationTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-source-of-information-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbSourceOfInformationTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sourceOfInformationTypeCode;

    private StringFilter sourceOfInformationTypeDescription;

    private Boolean distinct;

    public CrbSourceOfInformationTypeCriteria() {}

    public CrbSourceOfInformationTypeCriteria(CrbSourceOfInformationTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sourceOfInformationTypeCode = other.sourceOfInformationTypeCode == null ? null : other.sourceOfInformationTypeCode.copy();
        this.sourceOfInformationTypeDescription =
            other.sourceOfInformationTypeDescription == null ? null : other.sourceOfInformationTypeDescription.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbSourceOfInformationTypeCriteria copy() {
        return new CrbSourceOfInformationTypeCriteria(this);
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

    public StringFilter getSourceOfInformationTypeCode() {
        return sourceOfInformationTypeCode;
    }

    public StringFilter sourceOfInformationTypeCode() {
        if (sourceOfInformationTypeCode == null) {
            sourceOfInformationTypeCode = new StringFilter();
        }
        return sourceOfInformationTypeCode;
    }

    public void setSourceOfInformationTypeCode(StringFilter sourceOfInformationTypeCode) {
        this.sourceOfInformationTypeCode = sourceOfInformationTypeCode;
    }

    public StringFilter getSourceOfInformationTypeDescription() {
        return sourceOfInformationTypeDescription;
    }

    public StringFilter sourceOfInformationTypeDescription() {
        if (sourceOfInformationTypeDescription == null) {
            sourceOfInformationTypeDescription = new StringFilter();
        }
        return sourceOfInformationTypeDescription;
    }

    public void setSourceOfInformationTypeDescription(StringFilter sourceOfInformationTypeDescription) {
        this.sourceOfInformationTypeDescription = sourceOfInformationTypeDescription;
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
        final CrbSourceOfInformationTypeCriteria that = (CrbSourceOfInformationTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sourceOfInformationTypeCode, that.sourceOfInformationTypeCode) &&
            Objects.equals(sourceOfInformationTypeDescription, that.sourceOfInformationTypeDescription) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceOfInformationTypeCode, sourceOfInformationTypeDescription, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbSourceOfInformationTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sourceOfInformationTypeCode != null ? "sourceOfInformationTypeCode=" + sourceOfInformationTypeCode + ", " : "") +
            (sourceOfInformationTypeDescription != null ? "sourceOfInformationTypeDescription=" + sourceOfInformationTypeDescription + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
