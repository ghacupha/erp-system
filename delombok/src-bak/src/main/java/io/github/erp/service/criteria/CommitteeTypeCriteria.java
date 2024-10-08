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
 * Criteria class for the {@link io.github.erp.domain.CommitteeType} entity. This class is used
 * in {@link io.github.erp.web.rest.CommitteeTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /committee-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommitteeTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter committeeTypeCode;

    private StringFilter committeeType;

    private Boolean distinct;

    public CommitteeTypeCriteria() {}

    public CommitteeTypeCriteria(CommitteeTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.committeeTypeCode = other.committeeTypeCode == null ? null : other.committeeTypeCode.copy();
        this.committeeType = other.committeeType == null ? null : other.committeeType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommitteeTypeCriteria copy() {
        return new CommitteeTypeCriteria(this);
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

    public StringFilter getCommitteeTypeCode() {
        return committeeTypeCode;
    }

    public StringFilter committeeTypeCode() {
        if (committeeTypeCode == null) {
            committeeTypeCode = new StringFilter();
        }
        return committeeTypeCode;
    }

    public void setCommitteeTypeCode(StringFilter committeeTypeCode) {
        this.committeeTypeCode = committeeTypeCode;
    }

    public StringFilter getCommitteeType() {
        return committeeType;
    }

    public StringFilter committeeType() {
        if (committeeType == null) {
            committeeType = new StringFilter();
        }
        return committeeType;
    }

    public void setCommitteeType(StringFilter committeeType) {
        this.committeeType = committeeType;
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
        final CommitteeTypeCriteria that = (CommitteeTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(committeeTypeCode, that.committeeTypeCode) &&
            Objects.equals(committeeType, that.committeeType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, committeeTypeCode, committeeType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommitteeTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (committeeTypeCode != null ? "committeeTypeCode=" + committeeTypeCode + ", " : "") +
            (committeeType != null ? "committeeType=" + committeeType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
