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

import io.github.erp.domain.enumeration.transactionAccountPostingTypes;
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
 * Criteria class for the {@link io.github.erp.domain.TransactionAccountCategory} entity. This class is used
 * in {@link io.github.erp.web.rest.TransactionAccountCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-account-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionAccountCategoryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering transactionAccountPostingTypes
     */
    public static class transactionAccountPostingTypesFilter extends Filter<transactionAccountPostingTypes> {

        public transactionAccountPostingTypesFilter() {}

        public transactionAccountPostingTypesFilter(transactionAccountPostingTypesFilter filter) {
            super(filter);
        }

        @Override
        public transactionAccountPostingTypesFilter copy() {
            return new transactionAccountPostingTypesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private transactionAccountPostingTypesFilter transactionAccountPostingType;

    private LongFilter placeholderId;

    private LongFilter accountLedgerId;

    private Boolean distinct;

    public TransactionAccountCategoryCriteria() {}

    public TransactionAccountCategoryCriteria(TransactionAccountCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.transactionAccountPostingType =
            other.transactionAccountPostingType == null ? null : other.transactionAccountPostingType.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.accountLedgerId = other.accountLedgerId == null ? null : other.accountLedgerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionAccountCategoryCriteria copy() {
        return new TransactionAccountCategoryCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public transactionAccountPostingTypesFilter getTransactionAccountPostingType() {
        return transactionAccountPostingType;
    }

    public transactionAccountPostingTypesFilter transactionAccountPostingType() {
        if (transactionAccountPostingType == null) {
            transactionAccountPostingType = new transactionAccountPostingTypesFilter();
        }
        return transactionAccountPostingType;
    }

    public void setTransactionAccountPostingType(transactionAccountPostingTypesFilter transactionAccountPostingType) {
        this.transactionAccountPostingType = transactionAccountPostingType;
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
        final TransactionAccountCategoryCriteria that = (TransactionAccountCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(transactionAccountPostingType, that.transactionAccountPostingType) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(accountLedgerId, that.accountLedgerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transactionAccountPostingType, placeholderId, accountLedgerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (transactionAccountPostingType != null ? "transactionAccountPostingType=" + transactionAccountPostingType + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (accountLedgerId != null ? "accountLedgerId=" + accountLedgerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
