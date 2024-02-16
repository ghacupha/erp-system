package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
 * Criteria class for the {@link io.github.erp.domain.BankTransactionType} entity. This class is used
 * in {@link io.github.erp.web.rest.BankTransactionTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bank-transaction-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BankTransactionTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter transactionTypeCode;

    private StringFilter transactionTypeDetails;

    private Boolean distinct;

    public BankTransactionTypeCriteria() {}

    public BankTransactionTypeCriteria(BankTransactionTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.transactionTypeCode = other.transactionTypeCode == null ? null : other.transactionTypeCode.copy();
        this.transactionTypeDetails = other.transactionTypeDetails == null ? null : other.transactionTypeDetails.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BankTransactionTypeCriteria copy() {
        return new BankTransactionTypeCriteria(this);
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

    public StringFilter getTransactionTypeCode() {
        return transactionTypeCode;
    }

    public StringFilter transactionTypeCode() {
        if (transactionTypeCode == null) {
            transactionTypeCode = new StringFilter();
        }
        return transactionTypeCode;
    }

    public void setTransactionTypeCode(StringFilter transactionTypeCode) {
        this.transactionTypeCode = transactionTypeCode;
    }

    public StringFilter getTransactionTypeDetails() {
        return transactionTypeDetails;
    }

    public StringFilter transactionTypeDetails() {
        if (transactionTypeDetails == null) {
            transactionTypeDetails = new StringFilter();
        }
        return transactionTypeDetails;
    }

    public void setTransactionTypeDetails(StringFilter transactionTypeDetails) {
        this.transactionTypeDetails = transactionTypeDetails;
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
        final BankTransactionTypeCriteria that = (BankTransactionTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(transactionTypeCode, that.transactionTypeCode) &&
            Objects.equals(transactionTypeDetails, that.transactionTypeDetails) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionTypeCode, transactionTypeDetails, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankTransactionTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (transactionTypeCode != null ? "transactionTypeCode=" + transactionTypeCode + ", " : "") +
            (transactionTypeDetails != null ? "transactionTypeDetails=" + transactionTypeDetails + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
