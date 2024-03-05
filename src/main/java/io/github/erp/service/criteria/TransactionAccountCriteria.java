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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter accountNumber;

    private StringFilter accountName;

    private LongFilter parentAccountId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public TransactionAccountCriteria() {}

    public TransactionAccountCriteria(TransactionAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.accountNumber = other.accountNumber == null ? null : other.accountNumber.copy();
        this.accountName = other.accountName == null ? null : other.accountName.copy();
        this.parentAccountId = other.parentAccountId == null ? null : other.parentAccountId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
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

    public LongFilter getParentAccountId() {
        return parentAccountId;
    }

    public LongFilter parentAccountId() {
        if (parentAccountId == null) {
            parentAccountId = new LongFilter();
        }
        return parentAccountId;
    }

    public void setParentAccountId(LongFilter parentAccountId) {
        this.parentAccountId = parentAccountId;
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
            Objects.equals(parentAccountId, that.parentAccountId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, accountName, parentAccountId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (accountNumber != null ? "accountNumber=" + accountNumber + ", " : "") +
            (accountName != null ? "accountName=" + accountName + ", " : "") +
            (parentAccountId != null ? "parentAccountId=" + parentAccountId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
