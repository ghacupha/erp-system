package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
 * Criteria class for the {@link io.github.erp.domain.SnaSectorCode} entity. This class is used
 * in {@link io.github.erp.web.rest.SnaSectorCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sna-sector-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SnaSectorCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sectorTypeCode;

    private StringFilter mainSectorCode;

    private StringFilter mainSectorTypeName;

    private StringFilter subSectorCode;

    private StringFilter subSectorName;

    private StringFilter subSubSectorCode;

    private StringFilter subSubSectorName;

    private Boolean distinct;

    public SnaSectorCodeCriteria() {}

    public SnaSectorCodeCriteria(SnaSectorCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sectorTypeCode = other.sectorTypeCode == null ? null : other.sectorTypeCode.copy();
        this.mainSectorCode = other.mainSectorCode == null ? null : other.mainSectorCode.copy();
        this.mainSectorTypeName = other.mainSectorTypeName == null ? null : other.mainSectorTypeName.copy();
        this.subSectorCode = other.subSectorCode == null ? null : other.subSectorCode.copy();
        this.subSectorName = other.subSectorName == null ? null : other.subSectorName.copy();
        this.subSubSectorCode = other.subSubSectorCode == null ? null : other.subSubSectorCode.copy();
        this.subSubSectorName = other.subSubSectorName == null ? null : other.subSubSectorName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SnaSectorCodeCriteria copy() {
        return new SnaSectorCodeCriteria(this);
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

    public StringFilter getSectorTypeCode() {
        return sectorTypeCode;
    }

    public StringFilter sectorTypeCode() {
        if (sectorTypeCode == null) {
            sectorTypeCode = new StringFilter();
        }
        return sectorTypeCode;
    }

    public void setSectorTypeCode(StringFilter sectorTypeCode) {
        this.sectorTypeCode = sectorTypeCode;
    }

    public StringFilter getMainSectorCode() {
        return mainSectorCode;
    }

    public StringFilter mainSectorCode() {
        if (mainSectorCode == null) {
            mainSectorCode = new StringFilter();
        }
        return mainSectorCode;
    }

    public void setMainSectorCode(StringFilter mainSectorCode) {
        this.mainSectorCode = mainSectorCode;
    }

    public StringFilter getMainSectorTypeName() {
        return mainSectorTypeName;
    }

    public StringFilter mainSectorTypeName() {
        if (mainSectorTypeName == null) {
            mainSectorTypeName = new StringFilter();
        }
        return mainSectorTypeName;
    }

    public void setMainSectorTypeName(StringFilter mainSectorTypeName) {
        this.mainSectorTypeName = mainSectorTypeName;
    }

    public StringFilter getSubSectorCode() {
        return subSectorCode;
    }

    public StringFilter subSectorCode() {
        if (subSectorCode == null) {
            subSectorCode = new StringFilter();
        }
        return subSectorCode;
    }

    public void setSubSectorCode(StringFilter subSectorCode) {
        this.subSectorCode = subSectorCode;
    }

    public StringFilter getSubSectorName() {
        return subSectorName;
    }

    public StringFilter subSectorName() {
        if (subSectorName == null) {
            subSectorName = new StringFilter();
        }
        return subSectorName;
    }

    public void setSubSectorName(StringFilter subSectorName) {
        this.subSectorName = subSectorName;
    }

    public StringFilter getSubSubSectorCode() {
        return subSubSectorCode;
    }

    public StringFilter subSubSectorCode() {
        if (subSubSectorCode == null) {
            subSubSectorCode = new StringFilter();
        }
        return subSubSectorCode;
    }

    public void setSubSubSectorCode(StringFilter subSubSectorCode) {
        this.subSubSectorCode = subSubSectorCode;
    }

    public StringFilter getSubSubSectorName() {
        return subSubSectorName;
    }

    public StringFilter subSubSectorName() {
        if (subSubSectorName == null) {
            subSubSectorName = new StringFilter();
        }
        return subSubSectorName;
    }

    public void setSubSubSectorName(StringFilter subSubSectorName) {
        this.subSubSectorName = subSubSectorName;
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
        final SnaSectorCodeCriteria that = (SnaSectorCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sectorTypeCode, that.sectorTypeCode) &&
            Objects.equals(mainSectorCode, that.mainSectorCode) &&
            Objects.equals(mainSectorTypeName, that.mainSectorTypeName) &&
            Objects.equals(subSectorCode, that.subSectorCode) &&
            Objects.equals(subSectorName, that.subSectorName) &&
            Objects.equals(subSubSectorCode, that.subSubSectorCode) &&
            Objects.equals(subSubSectorName, that.subSubSectorName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sectorTypeCode,
            mainSectorCode,
            mainSectorTypeName,
            subSectorCode,
            subSectorName,
            subSubSectorCode,
            subSubSectorName,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SnaSectorCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sectorTypeCode != null ? "sectorTypeCode=" + sectorTypeCode + ", " : "") +
            (mainSectorCode != null ? "mainSectorCode=" + mainSectorCode + ", " : "") +
            (mainSectorTypeName != null ? "mainSectorTypeName=" + mainSectorTypeName + ", " : "") +
            (subSectorCode != null ? "subSectorCode=" + subSectorCode + ", " : "") +
            (subSectorName != null ? "subSectorName=" + subSectorName + ", " : "") +
            (subSubSectorCode != null ? "subSubSectorCode=" + subSubSectorCode + ", " : "") +
            (subSubSectorName != null ? "subSubSectorName=" + subSubSectorName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
