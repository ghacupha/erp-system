package io.github.erp.service.criteria;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.domain.enumeration.ShareholdingFlagTypes;
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
 * Criteria class for the {@link io.github.erp.domain.ShareHoldingFlag} entity. This class is used
 * in {@link io.github.erp.web.rest.ShareHoldingFlagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /share-holding-flags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShareHoldingFlagCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ShareholdingFlagTypes
     */
    public static class ShareholdingFlagTypesFilter extends Filter<ShareholdingFlagTypes> {

        public ShareholdingFlagTypesFilter() {}

        public ShareholdingFlagTypesFilter(ShareholdingFlagTypesFilter filter) {
            super(filter);
        }

        @Override
        public ShareholdingFlagTypesFilter copy() {
            return new ShareholdingFlagTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ShareholdingFlagTypesFilter shareholdingFlagTypeCode;

    private StringFilter shareholdingFlagType;

    private Boolean distinct;

    public ShareHoldingFlagCriteria() {}

    public ShareHoldingFlagCriteria(ShareHoldingFlagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.shareholdingFlagTypeCode = other.shareholdingFlagTypeCode == null ? null : other.shareholdingFlagTypeCode.copy();
        this.shareholdingFlagType = other.shareholdingFlagType == null ? null : other.shareholdingFlagType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShareHoldingFlagCriteria copy() {
        return new ShareHoldingFlagCriteria(this);
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

    public ShareholdingFlagTypesFilter getShareholdingFlagTypeCode() {
        return shareholdingFlagTypeCode;
    }

    public ShareholdingFlagTypesFilter shareholdingFlagTypeCode() {
        if (shareholdingFlagTypeCode == null) {
            shareholdingFlagTypeCode = new ShareholdingFlagTypesFilter();
        }
        return shareholdingFlagTypeCode;
    }

    public void setShareholdingFlagTypeCode(ShareholdingFlagTypesFilter shareholdingFlagTypeCode) {
        this.shareholdingFlagTypeCode = shareholdingFlagTypeCode;
    }

    public StringFilter getShareholdingFlagType() {
        return shareholdingFlagType;
    }

    public StringFilter shareholdingFlagType() {
        if (shareholdingFlagType == null) {
            shareholdingFlagType = new StringFilter();
        }
        return shareholdingFlagType;
    }

    public void setShareholdingFlagType(StringFilter shareholdingFlagType) {
        this.shareholdingFlagType = shareholdingFlagType;
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
        final ShareHoldingFlagCriteria that = (ShareHoldingFlagCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(shareholdingFlagTypeCode, that.shareholdingFlagTypeCode) &&
            Objects.equals(shareholdingFlagType, that.shareholdingFlagType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shareholdingFlagTypeCode, shareholdingFlagType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareHoldingFlagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (shareholdingFlagTypeCode != null ? "shareholdingFlagTypeCode=" + shareholdingFlagTypeCode + ", " : "") +
            (shareholdingFlagType != null ? "shareholdingFlagType=" + shareholdingFlagType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
