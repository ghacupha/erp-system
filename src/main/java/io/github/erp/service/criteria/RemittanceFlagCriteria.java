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

import io.github.erp.domain.enumeration.RemittanceType;
import io.github.erp.domain.enumeration.RemittanceTypeFlag;
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
 * Criteria class for the {@link io.github.erp.domain.RemittanceFlag} entity. This class is used
 * in {@link io.github.erp.web.rest.RemittanceFlagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /remittance-flags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RemittanceFlagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RemittanceTypeFlag
     */
    public static class RemittanceTypeFlagFilter extends Filter<RemittanceTypeFlag> {

        public RemittanceTypeFlagFilter() {}

        public RemittanceTypeFlagFilter(RemittanceTypeFlagFilter filter) {
            super(filter);
        }

        @Override
        public RemittanceTypeFlagFilter copy() {
            return new RemittanceTypeFlagFilter(this);
        }
    }

    /**
     * Class for filtering RemittanceType
     */
    public static class RemittanceTypeFilter extends Filter<RemittanceType> {

        public RemittanceTypeFilter() {}

        public RemittanceTypeFilter(RemittanceTypeFilter filter) {
            super(filter);
        }

        @Override
        public RemittanceTypeFilter copy() {
            return new RemittanceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private RemittanceTypeFlagFilter remittanceTypeFlag;

    private RemittanceTypeFilter remittanceType;

    private Boolean distinct;

    public RemittanceFlagCriteria() {}

    public RemittanceFlagCriteria(RemittanceFlagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remittanceTypeFlag = other.remittanceTypeFlag == null ? null : other.remittanceTypeFlag.copy();
        this.remittanceType = other.remittanceType == null ? null : other.remittanceType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RemittanceFlagCriteria copy() {
        return new RemittanceFlagCriteria(this);
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

    public RemittanceTypeFlagFilter getRemittanceTypeFlag() {
        return remittanceTypeFlag;
    }

    public RemittanceTypeFlagFilter remittanceTypeFlag() {
        if (remittanceTypeFlag == null) {
            remittanceTypeFlag = new RemittanceTypeFlagFilter();
        }
        return remittanceTypeFlag;
    }

    public void setRemittanceTypeFlag(RemittanceTypeFlagFilter remittanceTypeFlag) {
        this.remittanceTypeFlag = remittanceTypeFlag;
    }

    public RemittanceTypeFilter getRemittanceType() {
        return remittanceType;
    }

    public RemittanceTypeFilter remittanceType() {
        if (remittanceType == null) {
            remittanceType = new RemittanceTypeFilter();
        }
        return remittanceType;
    }

    public void setRemittanceType(RemittanceTypeFilter remittanceType) {
        this.remittanceType = remittanceType;
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
        final RemittanceFlagCriteria that = (RemittanceFlagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remittanceTypeFlag, that.remittanceTypeFlag) &&
            Objects.equals(remittanceType, that.remittanceType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, remittanceTypeFlag, remittanceType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RemittanceFlagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remittanceTypeFlag != null ? "remittanceTypeFlag=" + remittanceTypeFlag + ", " : "") +
            (remittanceType != null ? "remittanceType=" + remittanceType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
