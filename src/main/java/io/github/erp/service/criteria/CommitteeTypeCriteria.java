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
