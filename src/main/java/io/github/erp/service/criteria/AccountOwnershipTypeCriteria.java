package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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
 * Criteria class for the {@link io.github.erp.domain.AccountOwnershipType} entity. This class is used
 * in {@link io.github.erp.web.rest.AccountOwnershipTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /account-ownership-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccountOwnershipTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter accountOwnershipTypeCode;

    private StringFilter accountOwnershipType;

    private Boolean distinct;

    public AccountOwnershipTypeCriteria() {}

    public AccountOwnershipTypeCriteria(AccountOwnershipTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.accountOwnershipTypeCode = other.accountOwnershipTypeCode == null ? null : other.accountOwnershipTypeCode.copy();
        this.accountOwnershipType = other.accountOwnershipType == null ? null : other.accountOwnershipType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AccountOwnershipTypeCriteria copy() {
        return new AccountOwnershipTypeCriteria(this);
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

    public StringFilter getAccountOwnershipTypeCode() {
        return accountOwnershipTypeCode;
    }

    public StringFilter accountOwnershipTypeCode() {
        if (accountOwnershipTypeCode == null) {
            accountOwnershipTypeCode = new StringFilter();
        }
        return accountOwnershipTypeCode;
    }

    public void setAccountOwnershipTypeCode(StringFilter accountOwnershipTypeCode) {
        this.accountOwnershipTypeCode = accountOwnershipTypeCode;
    }

    public StringFilter getAccountOwnershipType() {
        return accountOwnershipType;
    }

    public StringFilter accountOwnershipType() {
        if (accountOwnershipType == null) {
            accountOwnershipType = new StringFilter();
        }
        return accountOwnershipType;
    }

    public void setAccountOwnershipType(StringFilter accountOwnershipType) {
        this.accountOwnershipType = accountOwnershipType;
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
        final AccountOwnershipTypeCriteria that = (AccountOwnershipTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(accountOwnershipTypeCode, that.accountOwnershipTypeCode) &&
            Objects.equals(accountOwnershipType, that.accountOwnershipType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountOwnershipTypeCode, accountOwnershipType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountOwnershipTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (accountOwnershipTypeCode != null ? "accountOwnershipTypeCode=" + accountOwnershipTypeCode + ", " : "") +
            (accountOwnershipType != null ? "accountOwnershipType=" + accountOwnershipType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
