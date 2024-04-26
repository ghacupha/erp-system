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
import io.github.erp.domain.enumeration.CreditCardOwnershipTypes;
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
 * Criteria class for the {@link io.github.erp.domain.CreditCardOwnership} entity. This class is used
 * in {@link io.github.erp.web.rest.CreditCardOwnershipResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /credit-card-ownerships?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CreditCardOwnershipCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CreditCardOwnershipTypes
     */
    public static class CreditCardOwnershipTypesFilter extends Filter<CreditCardOwnershipTypes> {

        public CreditCardOwnershipTypesFilter() {}

        public CreditCardOwnershipTypesFilter(CreditCardOwnershipTypesFilter filter) {
            super(filter);
        }

        @Override
        public CreditCardOwnershipTypesFilter copy() {
            return new CreditCardOwnershipTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter creditCardOwnershipCategoryCode;

    private CreditCardOwnershipTypesFilter creditCardOwnershipCategoryType;

    private Boolean distinct;

    public CreditCardOwnershipCriteria() {}

    public CreditCardOwnershipCriteria(CreditCardOwnershipCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creditCardOwnershipCategoryCode =
            other.creditCardOwnershipCategoryCode == null ? null : other.creditCardOwnershipCategoryCode.copy();
        this.creditCardOwnershipCategoryType =
            other.creditCardOwnershipCategoryType == null ? null : other.creditCardOwnershipCategoryType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CreditCardOwnershipCriteria copy() {
        return new CreditCardOwnershipCriteria(this);
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

    public StringFilter getCreditCardOwnershipCategoryCode() {
        return creditCardOwnershipCategoryCode;
    }

    public StringFilter creditCardOwnershipCategoryCode() {
        if (creditCardOwnershipCategoryCode == null) {
            creditCardOwnershipCategoryCode = new StringFilter();
        }
        return creditCardOwnershipCategoryCode;
    }

    public void setCreditCardOwnershipCategoryCode(StringFilter creditCardOwnershipCategoryCode) {
        this.creditCardOwnershipCategoryCode = creditCardOwnershipCategoryCode;
    }

    public CreditCardOwnershipTypesFilter getCreditCardOwnershipCategoryType() {
        return creditCardOwnershipCategoryType;
    }

    public CreditCardOwnershipTypesFilter creditCardOwnershipCategoryType() {
        if (creditCardOwnershipCategoryType == null) {
            creditCardOwnershipCategoryType = new CreditCardOwnershipTypesFilter();
        }
        return creditCardOwnershipCategoryType;
    }

    public void setCreditCardOwnershipCategoryType(CreditCardOwnershipTypesFilter creditCardOwnershipCategoryType) {
        this.creditCardOwnershipCategoryType = creditCardOwnershipCategoryType;
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
        final CreditCardOwnershipCriteria that = (CreditCardOwnershipCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creditCardOwnershipCategoryCode, that.creditCardOwnershipCategoryCode) &&
            Objects.equals(creditCardOwnershipCategoryType, that.creditCardOwnershipCategoryType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creditCardOwnershipCategoryCode, creditCardOwnershipCategoryType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditCardOwnershipCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creditCardOwnershipCategoryCode != null ? "creditCardOwnershipCategoryCode=" + creditCardOwnershipCategoryCode + ", " : "") +
            (creditCardOwnershipCategoryType != null ? "creditCardOwnershipCategoryType=" + creditCardOwnershipCategoryType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
