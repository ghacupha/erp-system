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
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.TransactionAccountPostingRule} entity. This class is used
 * in {@link io.github.erp.web.rest.TransactionAccountPostingRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-account-posting-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionAccountPostingRuleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private UUIDFilter identifier;

    private LongFilter debitAccountTypeId;

    private LongFilter creditAccountTypeId;

    private LongFilter transactionContextId;

    private Boolean distinct;

    public TransactionAccountPostingRuleCriteria() {}

    public TransactionAccountPostingRuleCriteria(TransactionAccountPostingRuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.identifier = other.identifier == null ? null : other.identifier.copy();
        this.debitAccountTypeId = other.debitAccountTypeId == null ? null : other.debitAccountTypeId.copy();
        this.creditAccountTypeId = other.creditAccountTypeId == null ? null : other.creditAccountTypeId.copy();
        this.transactionContextId = other.transactionContextId == null ? null : other.transactionContextId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionAccountPostingRuleCriteria copy() {
        return new TransactionAccountPostingRuleCriteria(this);
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

    public UUIDFilter getIdentifier() {
        return identifier;
    }

    public UUIDFilter identifier() {
        if (identifier == null) {
            identifier = new UUIDFilter();
        }
        return identifier;
    }

    public void setIdentifier(UUIDFilter identifier) {
        this.identifier = identifier;
    }

    public LongFilter getDebitAccountTypeId() {
        return debitAccountTypeId;
    }

    public LongFilter debitAccountTypeId() {
        if (debitAccountTypeId == null) {
            debitAccountTypeId = new LongFilter();
        }
        return debitAccountTypeId;
    }

    public void setDebitAccountTypeId(LongFilter debitAccountTypeId) {
        this.debitAccountTypeId = debitAccountTypeId;
    }

    public LongFilter getCreditAccountTypeId() {
        return creditAccountTypeId;
    }

    public LongFilter creditAccountTypeId() {
        if (creditAccountTypeId == null) {
            creditAccountTypeId = new LongFilter();
        }
        return creditAccountTypeId;
    }

    public void setCreditAccountTypeId(LongFilter creditAccountTypeId) {
        this.creditAccountTypeId = creditAccountTypeId;
    }

    public LongFilter getTransactionContextId() {
        return transactionContextId;
    }

    public LongFilter transactionContextId() {
        if (transactionContextId == null) {
            transactionContextId = new LongFilter();
        }
        return transactionContextId;
    }

    public void setTransactionContextId(LongFilter transactionContextId) {
        this.transactionContextId = transactionContextId;
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
        final TransactionAccountPostingRuleCriteria that = (TransactionAccountPostingRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(debitAccountTypeId, that.debitAccountTypeId) &&
            Objects.equals(creditAccountTypeId, that.creditAccountTypeId) &&
            Objects.equals(transactionContextId, that.transactionContextId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, identifier, debitAccountTypeId, creditAccountTypeId, transactionContextId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingRuleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (identifier != null ? "identifier=" + identifier + ", " : "") +
            (debitAccountTypeId != null ? "debitAccountTypeId=" + debitAccountTypeId + ", " : "") +
            (creditAccountTypeId != null ? "creditAccountTypeId=" + creditAccountTypeId + ", " : "") +
            (transactionContextId != null ? "transactionContextId=" + transactionContextId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
