package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
 * Criteria class for the {@link io.github.erp.domain.GlMapping} entity. This class is used
 * in {@link io.github.erp.web.rest.GlMappingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gl-mappings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GlMappingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter subGLCode;

    private StringFilter subGLDescription;

    private StringFilter mainGLCode;

    private StringFilter mainGLDescription;

    private StringFilter glType;

    private Boolean distinct;

    public GlMappingCriteria() {}

    public GlMappingCriteria(GlMappingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.subGLCode = other.subGLCode == null ? null : other.subGLCode.copy();
        this.subGLDescription = other.subGLDescription == null ? null : other.subGLDescription.copy();
        this.mainGLCode = other.mainGLCode == null ? null : other.mainGLCode.copy();
        this.mainGLDescription = other.mainGLDescription == null ? null : other.mainGLDescription.copy();
        this.glType = other.glType == null ? null : other.glType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GlMappingCriteria copy() {
        return new GlMappingCriteria(this);
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

    public StringFilter getSubGLCode() {
        return subGLCode;
    }

    public StringFilter subGLCode() {
        if (subGLCode == null) {
            subGLCode = new StringFilter();
        }
        return subGLCode;
    }

    public void setSubGLCode(StringFilter subGLCode) {
        this.subGLCode = subGLCode;
    }

    public StringFilter getSubGLDescription() {
        return subGLDescription;
    }

    public StringFilter subGLDescription() {
        if (subGLDescription == null) {
            subGLDescription = new StringFilter();
        }
        return subGLDescription;
    }

    public void setSubGLDescription(StringFilter subGLDescription) {
        this.subGLDescription = subGLDescription;
    }

    public StringFilter getMainGLCode() {
        return mainGLCode;
    }

    public StringFilter mainGLCode() {
        if (mainGLCode == null) {
            mainGLCode = new StringFilter();
        }
        return mainGLCode;
    }

    public void setMainGLCode(StringFilter mainGLCode) {
        this.mainGLCode = mainGLCode;
    }

    public StringFilter getMainGLDescription() {
        return mainGLDescription;
    }

    public StringFilter mainGLDescription() {
        if (mainGLDescription == null) {
            mainGLDescription = new StringFilter();
        }
        return mainGLDescription;
    }

    public void setMainGLDescription(StringFilter mainGLDescription) {
        this.mainGLDescription = mainGLDescription;
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
        final GlMappingCriteria that = (GlMappingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(subGLCode, that.subGLCode) &&
            Objects.equals(subGLDescription, that.subGLDescription) &&
            Objects.equals(mainGLCode, that.mainGLCode) &&
            Objects.equals(mainGLDescription, that.mainGLDescription) &&
            Objects.equals(glType, that.glType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subGLCode, subGLDescription, mainGLCode, mainGLDescription, glType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GlMappingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (subGLCode != null ? "subGLCode=" + subGLCode + ", " : "") +
            (subGLDescription != null ? "subGLDescription=" + subGLDescription + ", " : "") +
            (mainGLCode != null ? "mainGLCode=" + mainGLCode + ", " : "") +
            (mainGLDescription != null ? "mainGLDescription=" + mainGLDescription + ", " : "") +
            (glType != null ? "glType=" + glType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
