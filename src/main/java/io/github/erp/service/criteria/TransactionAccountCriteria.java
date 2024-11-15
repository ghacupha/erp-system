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

import io.github.erp.domain.enumeration.AccountSubTypes;
import io.github.erp.domain.enumeration.AccountTypes;
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
 * Criteria class for the {@link io.github.erp.domain.TransactionAccount} entity. This class is used
 * in {@link io.github.erp.web.rest.TransactionAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionAccountCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AccountTypes
     */
    public static class AccountTypesFilter extends Filter<AccountTypes> {

        public AccountTypesFilter() {}

        public AccountTypesFilter(AccountTypesFilter filter) {
            super(filter);
        }

        @Override
        public AccountTypesFilter copy() {
            return new AccountTypesFilter(this);
        }
    }

    /**
     * Class for filtering AccountSubTypes
     */
    public static class AccountSubTypesFilter extends Filter<AccountSubTypes> {

        public AccountSubTypesFilter() {}

        public AccountSubTypesFilter(AccountSubTypesFilter filter) {
            super(filter);
        }

        @Override
        public AccountSubTypesFilter copy() {
            return new AccountSubTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter accountNumber;

    private StringFilter accountName;

    private AccountTypesFilter accountType;

    private AccountSubTypesFilter accountSubType;

    private BooleanFilter dummyAccount;

    private LongFilter accountLedgerId;

    private LongFilter accountCategoryId;

    private LongFilter placeholderId;

    private LongFilter serviceOutletId;

    private LongFilter settlementCurrencyId;

    private Boolean distinct;

    public TransactionAccountCriteria() {}

    public TransactionAccountCriteria(TransactionAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.accountNumber = other.accountNumber == null ? null : other.accountNumber.copy();
        this.accountName = other.accountName == null ? null : other.accountName.copy();
        this.accountType = other.accountType == null ? null : other.accountType.copy();
        this.accountSubType = other.accountSubType == null ? null : other.accountSubType.copy();
        this.dummyAccount = other.dummyAccount == null ? null : other.dummyAccount.copy();
        this.accountLedgerId = other.accountLedgerId == null ? null : other.accountLedgerId.copy();
        this.accountCategoryId = other.accountCategoryId == null ? null : other.accountCategoryId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionAccountCriteria copy() {
        return new TransactionAccountCriteria(this);
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

    public StringFilter getAccountNumber() {
        return accountNumber;
    }

    public StringFilter accountNumber() {
        if (accountNumber == null) {
            accountNumber = new StringFilter();
        }
        return accountNumber;
    }

    public void setAccountNumber(StringFilter accountNumber) {
        this.accountNumber = accountNumber;
    }

    public StringFilter getAccountName() {
        return accountName;
    }

    public StringFilter accountName() {
        if (accountName == null) {
            accountName = new StringFilter();
        }
        return accountName;
    }

    public void setAccountName(StringFilter accountName) {
        this.accountName = accountName;
    }

    public AccountTypesFilter getAccountType() {
        return accountType;
    }

    public AccountTypesFilter accountType() {
        if (accountType == null) {
            accountType = new AccountTypesFilter();
        }
        return accountType;
    }

    public void setAccountType(AccountTypesFilter accountType) {
        this.accountType = accountType;
    }

    public AccountSubTypesFilter getAccountSubType() {
        return accountSubType;
    }

    public AccountSubTypesFilter accountSubType() {
        if (accountSubType == null) {
            accountSubType = new AccountSubTypesFilter();
        }
        return accountSubType;
    }

    public void setAccountSubType(AccountSubTypesFilter accountSubType) {
        this.accountSubType = accountSubType;
    }

    public BooleanFilter getDummyAccount() {
        return dummyAccount;
    }

    public BooleanFilter dummyAccount() {
        if (dummyAccount == null) {
            dummyAccount = new BooleanFilter();
        }
        return dummyAccount;
    }

    public void setDummyAccount(BooleanFilter dummyAccount) {
        this.dummyAccount = dummyAccount;
    }

    public LongFilter getAccountLedgerId() {
        return accountLedgerId;
    }

    public LongFilter accountLedgerId() {
        if (accountLedgerId == null) {
            accountLedgerId = new LongFilter();
        }
        return accountLedgerId;
    }

    public void setAccountLedgerId(LongFilter accountLedgerId) {
        this.accountLedgerId = accountLedgerId;
    }

    public LongFilter getAccountCategoryId() {
        return accountCategoryId;
    }

    public LongFilter accountCategoryId() {
        if (accountCategoryId == null) {
            accountCategoryId = new LongFilter();
        }
        return accountCategoryId;
    }

    public void setAccountCategoryId(LongFilter accountCategoryId) {
        this.accountCategoryId = accountCategoryId;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public LongFilter getServiceOutletId() {
        return serviceOutletId;
    }

    public LongFilter serviceOutletId() {
        if (serviceOutletId == null) {
            serviceOutletId = new LongFilter();
        }
        return serviceOutletId;
    }

    public void setServiceOutletId(LongFilter serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
    }

    public LongFilter getSettlementCurrencyId() {
        return settlementCurrencyId;
    }

    public LongFilter settlementCurrencyId() {
        if (settlementCurrencyId == null) {
            settlementCurrencyId = new LongFilter();
        }
        return settlementCurrencyId;
    }

    public void setSettlementCurrencyId(LongFilter settlementCurrencyId) {
        this.settlementCurrencyId = settlementCurrencyId;
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
        final TransactionAccountCriteria that = (TransactionAccountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(accountName, that.accountName) &&
            Objects.equals(accountType, that.accountType) &&
            Objects.equals(accountSubType, that.accountSubType) &&
            Objects.equals(dummyAccount, that.dummyAccount) &&
            Objects.equals(accountLedgerId, that.accountLedgerId) &&
            Objects.equals(accountCategoryId, that.accountCategoryId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            accountNumber,
            accountName,
            accountType,
            accountSubType,
            dummyAccount,
            accountLedgerId,
            accountCategoryId,
            placeholderId,
            serviceOutletId,
            settlementCurrencyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (accountNumber != null ? "accountNumber=" + accountNumber + ", " : "") +
            (accountName != null ? "accountName=" + accountName + ", " : "") +
            (accountType != null ? "accountType=" + accountType + ", " : "") +
            (accountSubType != null ? "accountSubType=" + accountSubType + ", " : "") +
            (dummyAccount != null ? "dummyAccount=" + dummyAccount + ", " : "") +
            (accountLedgerId != null ? "accountLedgerId=" + accountLedgerId + ", " : "") +
            (accountCategoryId != null ? "accountCategoryId=" + accountCategoryId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
