package io.github.erp.service.criteria;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
