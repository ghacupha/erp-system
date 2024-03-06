package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

import io.github.erp.domain.enumeration.AccountStatusTypes;
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
 * Criteria class for the {@link io.github.erp.domain.AccountStatusType} entity. This class is used
 * in {@link io.github.erp.web.rest.AccountStatusTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /account-status-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccountStatusTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AccountStatusTypes
     */
    public static class AccountStatusTypesFilter extends Filter<AccountStatusTypes> {

        public AccountStatusTypesFilter() {}

        public AccountStatusTypesFilter(AccountStatusTypesFilter filter) {
            super(filter);
        }

        @Override
        public AccountStatusTypesFilter copy() {
            return new AccountStatusTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter accountStatusCode;

    private AccountStatusTypesFilter accountStatusType;

    private Boolean distinct;

    public AccountStatusTypeCriteria() {}

    public AccountStatusTypeCriteria(AccountStatusTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.accountStatusCode = other.accountStatusCode == null ? null : other.accountStatusCode.copy();
        this.accountStatusType = other.accountStatusType == null ? null : other.accountStatusType.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AccountStatusTypeCriteria copy() {
        return new AccountStatusTypeCriteria(this);
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

    public StringFilter getAccountStatusCode() {
        return accountStatusCode;
    }

    public StringFilter accountStatusCode() {
        if (accountStatusCode == null) {
            accountStatusCode = new StringFilter();
        }
        return accountStatusCode;
    }

    public void setAccountStatusCode(StringFilter accountStatusCode) {
        this.accountStatusCode = accountStatusCode;
    }

    public AccountStatusTypesFilter getAccountStatusType() {
        return accountStatusType;
    }

    public AccountStatusTypesFilter accountStatusType() {
        if (accountStatusType == null) {
            accountStatusType = new AccountStatusTypesFilter();
        }
        return accountStatusType;
    }

    public void setAccountStatusType(AccountStatusTypesFilter accountStatusType) {
        this.accountStatusType = accountStatusType;
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
        final AccountStatusTypeCriteria that = (AccountStatusTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(accountStatusCode, that.accountStatusCode) &&
            Objects.equals(accountStatusType, that.accountStatusType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountStatusCode, accountStatusType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountStatusTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (accountStatusCode != null ? "accountStatusCode=" + accountStatusCode + ", " : "") +
            (accountStatusType != null ? "accountStatusType=" + accountStatusType + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
