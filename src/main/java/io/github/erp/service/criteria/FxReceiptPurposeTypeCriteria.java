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
 * Criteria class for the {@link io.github.erp.domain.FxReceiptPurposeType} entity. This class is used
 * in {@link io.github.erp.web.rest.FxReceiptPurposeTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fx-receipt-purpose-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FxReceiptPurposeTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter itemCode;

    private StringFilter attribute1ReceiptPaymentPurposeCode;

    private StringFilter attribute1ReceiptPaymentPurposeType;

    private StringFilter attribute2ReceiptPaymentPurposeCode;

    private StringFilter attribute2ReceiptPaymentPurposeDescription;

    private StringFilter attribute3ReceiptPaymentPurposeCode;

    private StringFilter attribute3ReceiptPaymentPurposeDescription;

    private StringFilter attribute4ReceiptPaymentPurposeCode;

    private StringFilter attribute4ReceiptPaymentPurposeDescription;

    private StringFilter attribute5ReceiptPaymentPurposeCode;

    private StringFilter attribute5ReceiptPaymentPurposeDescription;

    private StringFilter lastChild;

    private Boolean distinct;

    public FxReceiptPurposeTypeCriteria() {}

    public FxReceiptPurposeTypeCriteria(FxReceiptPurposeTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.itemCode = other.itemCode == null ? null : other.itemCode.copy();
        this.attribute1ReceiptPaymentPurposeCode =
            other.attribute1ReceiptPaymentPurposeCode == null ? null : other.attribute1ReceiptPaymentPurposeCode.copy();
        this.attribute1ReceiptPaymentPurposeType =
            other.attribute1ReceiptPaymentPurposeType == null ? null : other.attribute1ReceiptPaymentPurposeType.copy();
        this.attribute2ReceiptPaymentPurposeCode =
            other.attribute2ReceiptPaymentPurposeCode == null ? null : other.attribute2ReceiptPaymentPurposeCode.copy();
        this.attribute2ReceiptPaymentPurposeDescription =
            other.attribute2ReceiptPaymentPurposeDescription == null ? null : other.attribute2ReceiptPaymentPurposeDescription.copy();
        this.attribute3ReceiptPaymentPurposeCode =
            other.attribute3ReceiptPaymentPurposeCode == null ? null : other.attribute3ReceiptPaymentPurposeCode.copy();
        this.attribute3ReceiptPaymentPurposeDescription =
            other.attribute3ReceiptPaymentPurposeDescription == null ? null : other.attribute3ReceiptPaymentPurposeDescription.copy();
        this.attribute4ReceiptPaymentPurposeCode =
            other.attribute4ReceiptPaymentPurposeCode == null ? null : other.attribute4ReceiptPaymentPurposeCode.copy();
        this.attribute4ReceiptPaymentPurposeDescription =
            other.attribute4ReceiptPaymentPurposeDescription == null ? null : other.attribute4ReceiptPaymentPurposeDescription.copy();
        this.attribute5ReceiptPaymentPurposeCode =
            other.attribute5ReceiptPaymentPurposeCode == null ? null : other.attribute5ReceiptPaymentPurposeCode.copy();
        this.attribute5ReceiptPaymentPurposeDescription =
            other.attribute5ReceiptPaymentPurposeDescription == null ? null : other.attribute5ReceiptPaymentPurposeDescription.copy();
        this.lastChild = other.lastChild == null ? null : other.lastChild.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FxReceiptPurposeTypeCriteria copy() {
        return new FxReceiptPurposeTypeCriteria(this);
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

    public StringFilter getItemCode() {
        return itemCode;
    }

    public StringFilter itemCode() {
        if (itemCode == null) {
            itemCode = new StringFilter();
        }
        return itemCode;
    }

    public void setItemCode(StringFilter itemCode) {
        this.itemCode = itemCode;
    }

    public StringFilter getAttribute1ReceiptPaymentPurposeCode() {
        return attribute1ReceiptPaymentPurposeCode;
    }

    public StringFilter attribute1ReceiptPaymentPurposeCode() {
        if (attribute1ReceiptPaymentPurposeCode == null) {
            attribute1ReceiptPaymentPurposeCode = new StringFilter();
        }
        return attribute1ReceiptPaymentPurposeCode;
    }

    public void setAttribute1ReceiptPaymentPurposeCode(StringFilter attribute1ReceiptPaymentPurposeCode) {
        this.attribute1ReceiptPaymentPurposeCode = attribute1ReceiptPaymentPurposeCode;
    }

    public StringFilter getAttribute1ReceiptPaymentPurposeType() {
        return attribute1ReceiptPaymentPurposeType;
    }

    public StringFilter attribute1ReceiptPaymentPurposeType() {
        if (attribute1ReceiptPaymentPurposeType == null) {
            attribute1ReceiptPaymentPurposeType = new StringFilter();
        }
        return attribute1ReceiptPaymentPurposeType;
    }

    public void setAttribute1ReceiptPaymentPurposeType(StringFilter attribute1ReceiptPaymentPurposeType) {
        this.attribute1ReceiptPaymentPurposeType = attribute1ReceiptPaymentPurposeType;
    }

    public StringFilter getAttribute2ReceiptPaymentPurposeCode() {
        return attribute2ReceiptPaymentPurposeCode;
    }

    public StringFilter attribute2ReceiptPaymentPurposeCode() {
        if (attribute2ReceiptPaymentPurposeCode == null) {
            attribute2ReceiptPaymentPurposeCode = new StringFilter();
        }
        return attribute2ReceiptPaymentPurposeCode;
    }

    public void setAttribute2ReceiptPaymentPurposeCode(StringFilter attribute2ReceiptPaymentPurposeCode) {
        this.attribute2ReceiptPaymentPurposeCode = attribute2ReceiptPaymentPurposeCode;
    }

    public StringFilter getAttribute2ReceiptPaymentPurposeDescription() {
        return attribute2ReceiptPaymentPurposeDescription;
    }

    public StringFilter attribute2ReceiptPaymentPurposeDescription() {
        if (attribute2ReceiptPaymentPurposeDescription == null) {
            attribute2ReceiptPaymentPurposeDescription = new StringFilter();
        }
        return attribute2ReceiptPaymentPurposeDescription;
    }

    public void setAttribute2ReceiptPaymentPurposeDescription(StringFilter attribute2ReceiptPaymentPurposeDescription) {
        this.attribute2ReceiptPaymentPurposeDescription = attribute2ReceiptPaymentPurposeDescription;
    }

    public StringFilter getAttribute3ReceiptPaymentPurposeCode() {
        return attribute3ReceiptPaymentPurposeCode;
    }

    public StringFilter attribute3ReceiptPaymentPurposeCode() {
        if (attribute3ReceiptPaymentPurposeCode == null) {
            attribute3ReceiptPaymentPurposeCode = new StringFilter();
        }
        return attribute3ReceiptPaymentPurposeCode;
    }

    public void setAttribute3ReceiptPaymentPurposeCode(StringFilter attribute3ReceiptPaymentPurposeCode) {
        this.attribute3ReceiptPaymentPurposeCode = attribute3ReceiptPaymentPurposeCode;
    }

    public StringFilter getAttribute3ReceiptPaymentPurposeDescription() {
        return attribute3ReceiptPaymentPurposeDescription;
    }

    public StringFilter attribute3ReceiptPaymentPurposeDescription() {
        if (attribute3ReceiptPaymentPurposeDescription == null) {
            attribute3ReceiptPaymentPurposeDescription = new StringFilter();
        }
        return attribute3ReceiptPaymentPurposeDescription;
    }

    public void setAttribute3ReceiptPaymentPurposeDescription(StringFilter attribute3ReceiptPaymentPurposeDescription) {
        this.attribute3ReceiptPaymentPurposeDescription = attribute3ReceiptPaymentPurposeDescription;
    }

    public StringFilter getAttribute4ReceiptPaymentPurposeCode() {
        return attribute4ReceiptPaymentPurposeCode;
    }

    public StringFilter attribute4ReceiptPaymentPurposeCode() {
        if (attribute4ReceiptPaymentPurposeCode == null) {
            attribute4ReceiptPaymentPurposeCode = new StringFilter();
        }
        return attribute4ReceiptPaymentPurposeCode;
    }

    public void setAttribute4ReceiptPaymentPurposeCode(StringFilter attribute4ReceiptPaymentPurposeCode) {
        this.attribute4ReceiptPaymentPurposeCode = attribute4ReceiptPaymentPurposeCode;
    }

    public StringFilter getAttribute4ReceiptPaymentPurposeDescription() {
        return attribute4ReceiptPaymentPurposeDescription;
    }

    public StringFilter attribute4ReceiptPaymentPurposeDescription() {
        if (attribute4ReceiptPaymentPurposeDescription == null) {
            attribute4ReceiptPaymentPurposeDescription = new StringFilter();
        }
        return attribute4ReceiptPaymentPurposeDescription;
    }

    public void setAttribute4ReceiptPaymentPurposeDescription(StringFilter attribute4ReceiptPaymentPurposeDescription) {
        this.attribute4ReceiptPaymentPurposeDescription = attribute4ReceiptPaymentPurposeDescription;
    }

    public StringFilter getAttribute5ReceiptPaymentPurposeCode() {
        return attribute5ReceiptPaymentPurposeCode;
    }

    public StringFilter attribute5ReceiptPaymentPurposeCode() {
        if (attribute5ReceiptPaymentPurposeCode == null) {
            attribute5ReceiptPaymentPurposeCode = new StringFilter();
        }
        return attribute5ReceiptPaymentPurposeCode;
    }

    public void setAttribute5ReceiptPaymentPurposeCode(StringFilter attribute5ReceiptPaymentPurposeCode) {
        this.attribute5ReceiptPaymentPurposeCode = attribute5ReceiptPaymentPurposeCode;
    }

    public StringFilter getAttribute5ReceiptPaymentPurposeDescription() {
        return attribute5ReceiptPaymentPurposeDescription;
    }

    public StringFilter attribute5ReceiptPaymentPurposeDescription() {
        if (attribute5ReceiptPaymentPurposeDescription == null) {
            attribute5ReceiptPaymentPurposeDescription = new StringFilter();
        }
        return attribute5ReceiptPaymentPurposeDescription;
    }

    public void setAttribute5ReceiptPaymentPurposeDescription(StringFilter attribute5ReceiptPaymentPurposeDescription) {
        this.attribute5ReceiptPaymentPurposeDescription = attribute5ReceiptPaymentPurposeDescription;
    }

    public StringFilter getLastChild() {
        return lastChild;
    }

    public StringFilter lastChild() {
        if (lastChild == null) {
            lastChild = new StringFilter();
        }
        return lastChild;
    }

    public void setLastChild(StringFilter lastChild) {
        this.lastChild = lastChild;
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
        final FxReceiptPurposeTypeCriteria that = (FxReceiptPurposeTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(itemCode, that.itemCode) &&
            Objects.equals(attribute1ReceiptPaymentPurposeCode, that.attribute1ReceiptPaymentPurposeCode) &&
            Objects.equals(attribute1ReceiptPaymentPurposeType, that.attribute1ReceiptPaymentPurposeType) &&
            Objects.equals(attribute2ReceiptPaymentPurposeCode, that.attribute2ReceiptPaymentPurposeCode) &&
            Objects.equals(attribute2ReceiptPaymentPurposeDescription, that.attribute2ReceiptPaymentPurposeDescription) &&
            Objects.equals(attribute3ReceiptPaymentPurposeCode, that.attribute3ReceiptPaymentPurposeCode) &&
            Objects.equals(attribute3ReceiptPaymentPurposeDescription, that.attribute3ReceiptPaymentPurposeDescription) &&
            Objects.equals(attribute4ReceiptPaymentPurposeCode, that.attribute4ReceiptPaymentPurposeCode) &&
            Objects.equals(attribute4ReceiptPaymentPurposeDescription, that.attribute4ReceiptPaymentPurposeDescription) &&
            Objects.equals(attribute5ReceiptPaymentPurposeCode, that.attribute5ReceiptPaymentPurposeCode) &&
            Objects.equals(attribute5ReceiptPaymentPurposeDescription, that.attribute5ReceiptPaymentPurposeDescription) &&
            Objects.equals(lastChild, that.lastChild) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            itemCode,
            attribute1ReceiptPaymentPurposeCode,
            attribute1ReceiptPaymentPurposeType,
            attribute2ReceiptPaymentPurposeCode,
            attribute2ReceiptPaymentPurposeDescription,
            attribute3ReceiptPaymentPurposeCode,
            attribute3ReceiptPaymentPurposeDescription,
            attribute4ReceiptPaymentPurposeCode,
            attribute4ReceiptPaymentPurposeDescription,
            attribute5ReceiptPaymentPurposeCode,
            attribute5ReceiptPaymentPurposeDescription,
            lastChild,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FxReceiptPurposeTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (itemCode != null ? "itemCode=" + itemCode + ", " : "") +
            (attribute1ReceiptPaymentPurposeCode != null ? "attribute1ReceiptPaymentPurposeCode=" + attribute1ReceiptPaymentPurposeCode + ", " : "") +
            (attribute1ReceiptPaymentPurposeType != null ? "attribute1ReceiptPaymentPurposeType=" + attribute1ReceiptPaymentPurposeType + ", " : "") +
            (attribute2ReceiptPaymentPurposeCode != null ? "attribute2ReceiptPaymentPurposeCode=" + attribute2ReceiptPaymentPurposeCode + ", " : "") +
            (attribute2ReceiptPaymentPurposeDescription != null ? "attribute2ReceiptPaymentPurposeDescription=" + attribute2ReceiptPaymentPurposeDescription + ", " : "") +
            (attribute3ReceiptPaymentPurposeCode != null ? "attribute3ReceiptPaymentPurposeCode=" + attribute3ReceiptPaymentPurposeCode + ", " : "") +
            (attribute3ReceiptPaymentPurposeDescription != null ? "attribute3ReceiptPaymentPurposeDescription=" + attribute3ReceiptPaymentPurposeDescription + ", " : "") +
            (attribute4ReceiptPaymentPurposeCode != null ? "attribute4ReceiptPaymentPurposeCode=" + attribute4ReceiptPaymentPurposeCode + ", " : "") +
            (attribute4ReceiptPaymentPurposeDescription != null ? "attribute4ReceiptPaymentPurposeDescription=" + attribute4ReceiptPaymentPurposeDescription + ", " : "") +
            (attribute5ReceiptPaymentPurposeCode != null ? "attribute5ReceiptPaymentPurposeCode=" + attribute5ReceiptPaymentPurposeCode + ", " : "") +
            (attribute5ReceiptPaymentPurposeDescription != null ? "attribute5ReceiptPaymentPurposeDescription=" + attribute5ReceiptPaymentPurposeDescription + ", " : "") +
            (lastChild != null ? "lastChild=" + lastChild + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
