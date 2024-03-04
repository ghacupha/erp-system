package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
 * Criteria class for the {@link io.github.erp.domain.CrbGlCode} entity. This class is used
 * in {@link io.github.erp.web.rest.CrbGlCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /crb-gl-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CrbGlCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter glCode;

    private StringFilter glDescription;

    private StringFilter glType;

    private StringFilter institutionCategory;

    private Boolean distinct;

    public CrbGlCodeCriteria() {}

    public CrbGlCodeCriteria(CrbGlCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.glCode = other.glCode == null ? null : other.glCode.copy();
        this.glDescription = other.glDescription == null ? null : other.glDescription.copy();
        this.glType = other.glType == null ? null : other.glType.copy();
        this.institutionCategory = other.institutionCategory == null ? null : other.institutionCategory.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CrbGlCodeCriteria copy() {
        return new CrbGlCodeCriteria(this);
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

    public StringFilter getGlCode() {
        return glCode;
    }

    public StringFilter glCode() {
        if (glCode == null) {
            glCode = new StringFilter();
        }
        return glCode;
    }

    public void setGlCode(StringFilter glCode) {
        this.glCode = glCode;
    }

    public StringFilter getGlDescription() {
        return glDescription;
    }

    public StringFilter glDescription() {
        if (glDescription == null) {
            glDescription = new StringFilter();
        }
        return glDescription;
    }

    public void setGlDescription(StringFilter glDescription) {
        this.glDescription = glDescription;
    }

    public StringFilter getGlType() {
        return glType;
    }

    public StringFilter glType() {
        if (glType == null) {
            glType = new StringFilter();
        }
        return glType;
    }

    public void setGlType(StringFilter glType) {
        this.glType = glType;
    }

    public StringFilter getInstitutionCategory() {
        return institutionCategory;
    }

    public StringFilter institutionCategory() {
        if (institutionCategory == null) {
            institutionCategory = new StringFilter();
        }
        return institutionCategory;
    }

    public void setInstitutionCategory(StringFilter institutionCategory) {
        this.institutionCategory = institutionCategory;
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
        final CrbGlCodeCriteria that = (CrbGlCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(glCode, that.glCode) &&
            Objects.equals(glDescription, that.glDescription) &&
            Objects.equals(glType, that.glType) &&
            Objects.equals(institutionCategory, that.institutionCategory) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, glCode, glDescription, glType, institutionCategory, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbGlCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (glCode != null ? "glCode=" + glCode + ", " : "") +
            (glDescription != null ? "glDescription=" + glDescription + ", " : "") +
            (glType != null ? "glType=" + glType + ", " : "") +
            (institutionCategory != null ? "institutionCategory=" + institutionCategory + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
