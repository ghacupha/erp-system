package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
 * Criteria class for the {@link io.github.erp.domain.TransactionAccountLedger} entity. This class is used
 * in {@link io.github.erp.web.rest.TransactionAccountLedgerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-account-ledgers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionAccountLedgerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ledgerCode;

    private StringFilter ledgerName;

    private LongFilter placeholderId;

    private Boolean distinct;

    public TransactionAccountLedgerCriteria() {}

    public TransactionAccountLedgerCriteria(TransactionAccountLedgerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ledgerCode = other.ledgerCode == null ? null : other.ledgerCode.copy();
        this.ledgerName = other.ledgerName == null ? null : other.ledgerName.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionAccountLedgerCriteria copy() {
        return new TransactionAccountLedgerCriteria(this);
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

    public StringFilter getLedgerCode() {
        return ledgerCode;
    }

    public StringFilter ledgerCode() {
        if (ledgerCode == null) {
            ledgerCode = new StringFilter();
        }
        return ledgerCode;
    }

    public void setLedgerCode(StringFilter ledgerCode) {
        this.ledgerCode = ledgerCode;
    }

    public StringFilter getLedgerName() {
        return ledgerName;
    }

    public StringFilter ledgerName() {
        if (ledgerName == null) {
            ledgerName = new StringFilter();
        }
        return ledgerName;
    }

    public void setLedgerName(StringFilter ledgerName) {
        this.ledgerName = ledgerName;
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
        final TransactionAccountLedgerCriteria that = (TransactionAccountLedgerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ledgerCode, that.ledgerCode) &&
            Objects.equals(ledgerName, that.ledgerName) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ledgerCode, ledgerName, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountLedgerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ledgerCode != null ? "ledgerCode=" + ledgerCode + ", " : "") +
            (ledgerName != null ? "ledgerName=" + ledgerName + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
