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

/**
 * Criteria class for the {@link io.github.erp.domain.TransactionAccountPostingProcessType} entity. This class is used
 * in {@link io.github.erp.web.rest.TransactionAccountPostingProcessTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-account-posting-process-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionAccountPostingProcessTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter debitAccountTypeId;

    private LongFilter creditAccountTypeId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public TransactionAccountPostingProcessTypeCriteria() {}

    public TransactionAccountPostingProcessTypeCriteria(TransactionAccountPostingProcessTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.debitAccountTypeId = other.debitAccountTypeId == null ? null : other.debitAccountTypeId.copy();
        this.creditAccountTypeId = other.creditAccountTypeId == null ? null : other.creditAccountTypeId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionAccountPostingProcessTypeCriteria copy() {
        return new TransactionAccountPostingProcessTypeCriteria(this);
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
        final TransactionAccountPostingProcessTypeCriteria that = (TransactionAccountPostingProcessTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(debitAccountTypeId, that.debitAccountTypeId) &&
            Objects.equals(creditAccountTypeId, that.creditAccountTypeId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, debitAccountTypeId, creditAccountTypeId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountPostingProcessTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (debitAccountTypeId != null ? "debitAccountTypeId=" + debitAccountTypeId + ", " : "") +
            (creditAccountTypeId != null ? "creditAccountTypeId=" + creditAccountTypeId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
