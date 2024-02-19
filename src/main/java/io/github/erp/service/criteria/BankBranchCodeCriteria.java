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
 * Criteria class for the {@link io.github.erp.domain.BankBranchCode} entity. This class is used
 * in {@link io.github.erp.web.rest.BankBranchCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bank-branch-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BankBranchCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bankCode;

    private StringFilter bankName;

    private StringFilter branchCode;

    private StringFilter branchName;

    private StringFilter notes;

    private LongFilter placeholderId;

    private Boolean distinct;

    public BankBranchCodeCriteria() {}

    public BankBranchCodeCriteria(BankBranchCodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bankCode = other.bankCode == null ? null : other.bankCode.copy();
        this.bankName = other.bankName == null ? null : other.bankName.copy();
        this.branchCode = other.branchCode == null ? null : other.branchCode.copy();
        this.branchName = other.branchName == null ? null : other.branchName.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BankBranchCodeCriteria copy() {
        return new BankBranchCodeCriteria(this);
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

    public StringFilter getBankCode() {
        return bankCode;
    }

    public StringFilter bankCode() {
        if (bankCode == null) {
            bankCode = new StringFilter();
        }
        return bankCode;
    }

    public void setBankCode(StringFilter bankCode) {
        this.bankCode = bankCode;
    }

    public StringFilter getBankName() {
        return bankName;
    }

    public StringFilter bankName() {
        if (bankName == null) {
            bankName = new StringFilter();
        }
        return bankName;
    }

    public void setBankName(StringFilter bankName) {
        this.bankName = bankName;
    }

    public StringFilter getBranchCode() {
        return branchCode;
    }

    public StringFilter branchCode() {
        if (branchCode == null) {
            branchCode = new StringFilter();
        }
        return branchCode;
    }

    public void setBranchCode(StringFilter branchCode) {
        this.branchCode = branchCode;
    }

    public StringFilter getBranchName() {
        return branchName;
    }

    public StringFilter branchName() {
        if (branchName == null) {
            branchName = new StringFilter();
        }
        return branchName;
    }

    public void setBranchName(StringFilter branchName) {
        this.branchName = branchName;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
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
        final BankBranchCodeCriteria that = (BankBranchCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bankCode, that.bankCode) &&
            Objects.equals(bankName, that.bankName) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchName, that.branchName) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bankCode, bankName, branchCode, branchName, notes, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankBranchCodeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bankCode != null ? "bankCode=" + bankCode + ", " : "") +
            (bankName != null ? "bankName=" + bankName + ", " : "") +
            (branchCode != null ? "branchCode=" + branchCode + ", " : "") +
            (branchName != null ? "branchName=" + branchName + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
