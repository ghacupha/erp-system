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
import io.github.erp.domain.enumeration.SourceOrPurposeOfRemittancFlag;
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
 * Criteria class for the {@link io.github.erp.domain.SourceRemittancePurposeType} entity. This class is used
 * in {@link io.github.erp.web.rest.SourceRemittancePurposeTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /source-remittance-purpose-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SourceRemittancePurposeTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SourceOrPurposeOfRemittancFlag
     */
    public static class SourceOrPurposeOfRemittancFlagFilter extends Filter<SourceOrPurposeOfRemittancFlag> {

        public SourceOrPurposeOfRemittancFlagFilter() {}

        public SourceOrPurposeOfRemittancFlagFilter(SourceOrPurposeOfRemittancFlagFilter filter) {
            super(filter);
        }

        @Override
        public SourceOrPurposeOfRemittancFlagFilter copy() {
            return new SourceOrPurposeOfRemittancFlagFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sourceOrPurposeTypeCode;

    private SourceOrPurposeOfRemittancFlagFilter sourceOrPurposeOfRemittanceFlag;

    private StringFilter sourceOrPurposeOfRemittanceType;

    private Boolean distinct;

    public SourceRemittancePurposeTypeCriteria() {}

    public SourceRemittancePurposeTypeCriteria(SourceRemittancePurposeTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sourceOrPurposeTypeCode = other.sourceOrPurposeTypeCode == null ? null : other.sourceOrPurposeTypeCode.copy();
        this.sourceOrPurposeOfRemittanceFlag =
            other.sourceOrPurposeOfRemittanceFlag == null ? null : other.sourceOrPurposeOfRemittanceFlag.copy();
        this.sourceOrPurposeOfRemittanceType =
            other.sourceOrPurposeOfRemittanceType == null ? null : other.sourceOrPurposeOfRemittanceType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SourceRemittancePurposeTypeCriteria copy() {
        return new SourceRemittancePurposeTypeCriteria(this);
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

    public StringFilter getSourceOrPurposeTypeCode() {
        return sourceOrPurposeTypeCode;
    }

    public StringFilter sourceOrPurposeTypeCode() {
        if (sourceOrPurposeTypeCode == null) {
            sourceOrPurposeTypeCode = new StringFilter();
        }
        return sourceOrPurposeTypeCode;
    }

    public void setSourceOrPurposeTypeCode(StringFilter sourceOrPurposeTypeCode) {
        this.sourceOrPurposeTypeCode = sourceOrPurposeTypeCode;
    }

    public SourceOrPurposeOfRemittancFlagFilter getSourceOrPurposeOfRemittanceFlag() {
        return sourceOrPurposeOfRemittanceFlag;
    }

    public SourceOrPurposeOfRemittancFlagFilter sourceOrPurposeOfRemittanceFlag() {
        if (sourceOrPurposeOfRemittanceFlag == null) {
            sourceOrPurposeOfRemittanceFlag = new SourceOrPurposeOfRemittancFlagFilter();
        }
        return sourceOrPurposeOfRemittanceFlag;
    }

    public void setSourceOrPurposeOfRemittanceFlag(SourceOrPurposeOfRemittancFlagFilter sourceOrPurposeOfRemittanceFlag) {
        this.sourceOrPurposeOfRemittanceFlag = sourceOrPurposeOfRemittanceFlag;
    }

    public StringFilter getSourceOrPurposeOfRemittanceType() {
        return sourceOrPurposeOfRemittanceType;
    }

    public StringFilter sourceOrPurposeOfRemittanceType() {
        if (sourceOrPurposeOfRemittanceType == null) {
            sourceOrPurposeOfRemittanceType = new StringFilter();
        }
        return sourceOrPurposeOfRemittanceType;
    }

    public void setSourceOrPurposeOfRemittanceType(StringFilter sourceOrPurposeOfRemittanceType) {
        this.sourceOrPurposeOfRemittanceType = sourceOrPurposeOfRemittanceType;
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
        final SourceRemittancePurposeTypeCriteria that = (SourceRemittancePurposeTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sourceOrPurposeTypeCode, that.sourceOrPurposeTypeCode) &&
            Objects.equals(sourceOrPurposeOfRemittanceFlag, that.sourceOrPurposeOfRemittanceFlag) &&
            Objects.equals(sourceOrPurposeOfRemittanceType, that.sourceOrPurposeOfRemittanceType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceOrPurposeTypeCode, sourceOrPurposeOfRemittanceFlag, sourceOrPurposeOfRemittanceType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceRemittancePurposeTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sourceOrPurposeTypeCode != null ? "sourceOrPurposeTypeCode=" + sourceOrPurposeTypeCode + ", " : "") +
            (sourceOrPurposeOfRemittanceFlag != null ? "sourceOrPurposeOfRemittanceFlag=" + sourceOrPurposeOfRemittanceFlag + ", " : "") +
            (sourceOrPurposeOfRemittanceType != null ? "sourceOrPurposeOfRemittanceType=" + sourceOrPurposeOfRemittanceType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
