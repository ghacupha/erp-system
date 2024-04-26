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
import io.github.erp.domain.enumeration.ShareHolderTypes;
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
 * Criteria class for the {@link io.github.erp.domain.ShareholderType} entity. This class is used
 * in {@link io.github.erp.web.rest.ShareholderTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shareholder-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShareholderTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ShareHolderTypes
     */
    public static class ShareHolderTypesFilter extends Filter<ShareHolderTypes> {

        public ShareHolderTypesFilter() {}

        public ShareHolderTypesFilter(ShareHolderTypesFilter filter) {
            super(filter);
        }

        @Override
        public ShareHolderTypesFilter copy() {
            return new ShareHolderTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter shareHolderTypeCode;

    private ShareHolderTypesFilter shareHolderType;

    private Boolean distinct;

    public ShareholderTypeCriteria() {}

    public ShareholderTypeCriteria(ShareholderTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.shareHolderTypeCode = other.shareHolderTypeCode == null ? null : other.shareHolderTypeCode.copy();
        this.shareHolderType = other.shareHolderType == null ? null : other.shareHolderType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShareholderTypeCriteria copy() {
        return new ShareholderTypeCriteria(this);
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

    public StringFilter getShareHolderTypeCode() {
        return shareHolderTypeCode;
    }

    public StringFilter shareHolderTypeCode() {
        if (shareHolderTypeCode == null) {
            shareHolderTypeCode = new StringFilter();
        }
        return shareHolderTypeCode;
    }

    public void setShareHolderTypeCode(StringFilter shareHolderTypeCode) {
        this.shareHolderTypeCode = shareHolderTypeCode;
    }

    public ShareHolderTypesFilter getShareHolderType() {
        return shareHolderType;
    }

    public ShareHolderTypesFilter shareHolderType() {
        if (shareHolderType == null) {
            shareHolderType = new ShareHolderTypesFilter();
        }
        return shareHolderType;
    }

    public void setShareHolderType(ShareHolderTypesFilter shareHolderType) {
        this.shareHolderType = shareHolderType;
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
        final ShareholderTypeCriteria that = (ShareholderTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(shareHolderTypeCode, that.shareHolderTypeCode) &&
            Objects.equals(shareHolderType, that.shareHolderType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shareHolderTypeCode, shareHolderType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareholderTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (shareHolderTypeCode != null ? "shareHolderTypeCode=" + shareHolderTypeCode + ", " : "") +
            (shareHolderType != null ? "shareHolderType=" + shareHolderType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
