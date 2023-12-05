package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
 * Criteria class for the {@link io.github.erp.domain.CountySubCountyCode} entity. This class is used
 * in {@link io.github.erp.web.rest.CountySubCountyCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /county-sub-county-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountySubCountyCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter subCountyCode;

    private StringFilter subCountyName;

    private StringFilter countyCode;

    private StringFilter countyName;

    private Boolean distinct;

    public CountySubCountyCodeCriteria() {}

    public CountySubCountyCodeCriteria(CountySubCountyCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.subCountyCode = other.subCountyCode == null ? null : other.subCountyCode.copy();
        this.subCountyName = other.subCountyName == null ? null : other.subCountyName.copy();
        this.countyCode = other.countyCode == null ? null : other.countyCode.copy();
        this.countyName = other.countyName == null ? null : other.countyName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountySubCountyCodeCriteria copy() {
        return new CountySubCountyCodeCriteria(this);
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

    public StringFilter getSubCountyCode() {
        return subCountyCode;
    }

    public StringFilter subCountyCode() {
        if (subCountyCode == null) {
            subCountyCode = new StringFilter();
        }
        return subCountyCode;
    }

    public void setSubCountyCode(StringFilter subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public StringFilter getSubCountyName() {
        return subCountyName;
    }

    public StringFilter subCountyName() {
        if (subCountyName == null) {
            subCountyName = new StringFilter();
        }
        return subCountyName;
    }

    public void setSubCountyName(StringFilter subCountyName) {
        this.subCountyName = subCountyName;
    }

    public StringFilter getCountyCode() {
        return countyCode;
    }

    public StringFilter countyCode() {
        if (countyCode == null) {
            countyCode = new StringFilter();
        }
        return countyCode;
    }

    public void setCountyCode(StringFilter countyCode) {
        this.countyCode = countyCode;
    }

    public StringFilter getCountyName() {
        return countyName;
    }

    public StringFilter countyName() {
        if (countyName == null) {
            countyName = new StringFilter();
        }
        return countyName;
    }

    public void setCountyName(StringFilter countyName) {
        this.countyName = countyName;
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
        final CountySubCountyCodeCriteria that = (CountySubCountyCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(subCountyCode, that.subCountyCode) &&
            Objects.equals(subCountyName, that.subCountyName) &&
            Objects.equals(countyCode, that.countyCode) &&
            Objects.equals(countyName, that.countyName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subCountyCode, subCountyName, countyCode, countyName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountySubCountyCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (subCountyCode != null ? "subCountyCode=" + subCountyCode + ", " : "") +
            (subCountyName != null ? "subCountyName=" + subCountyName + ", " : "") +
            (countyCode != null ? "countyCode=" + countyCode + ", " : "") +
            (countyName != null ? "countyName=" + countyName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
