package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.InternalMemo} entity. This class is used
 * in {@link io.github.erp.web.rest.InternalMemoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /internal-memos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InternalMemoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter catalogueNumber;

    private StringFilter referenceNumber;

    private LocalDateFilter memoDate;

    private StringFilter purposeDescription;

    private LongFilter actionRequiredId;

    private LongFilter addressedToId;

    private LongFilter fromId;

    private LongFilter preparedById;

    private LongFilter reviewedById;

    private LongFilter approvedById;

    private LongFilter memoDocumentId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public InternalMemoCriteria() {}

    public InternalMemoCriteria(InternalMemoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.catalogueNumber = other.catalogueNumber == null ? null : other.catalogueNumber.copy();
        this.referenceNumber = other.referenceNumber == null ? null : other.referenceNumber.copy();
        this.memoDate = other.memoDate == null ? null : other.memoDate.copy();
        this.purposeDescription = other.purposeDescription == null ? null : other.purposeDescription.copy();
        this.actionRequiredId = other.actionRequiredId == null ? null : other.actionRequiredId.copy();
        this.addressedToId = other.addressedToId == null ? null : other.addressedToId.copy();
        this.fromId = other.fromId == null ? null : other.fromId.copy();
        this.preparedById = other.preparedById == null ? null : other.preparedById.copy();
        this.reviewedById = other.reviewedById == null ? null : other.reviewedById.copy();
        this.approvedById = other.approvedById == null ? null : other.approvedById.copy();
        this.memoDocumentId = other.memoDocumentId == null ? null : other.memoDocumentId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InternalMemoCriteria copy() {
        return new InternalMemoCriteria(this);
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

    public StringFilter getCatalogueNumber() {
        return catalogueNumber;
    }

    public StringFilter catalogueNumber() {
        if (catalogueNumber == null) {
            catalogueNumber = new StringFilter();
        }
        return catalogueNumber;
    }

    public void setCatalogueNumber(StringFilter catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public StringFilter getReferenceNumber() {
        return referenceNumber;
    }

    public StringFilter referenceNumber() {
        if (referenceNumber == null) {
            referenceNumber = new StringFilter();
        }
        return referenceNumber;
    }

    public void setReferenceNumber(StringFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LocalDateFilter getMemoDate() {
        return memoDate;
    }

    public LocalDateFilter memoDate() {
        if (memoDate == null) {
            memoDate = new LocalDateFilter();
        }
        return memoDate;
    }

    public void setMemoDate(LocalDateFilter memoDate) {
        this.memoDate = memoDate;
    }

    public StringFilter getPurposeDescription() {
        return purposeDescription;
    }

    public StringFilter purposeDescription() {
        if (purposeDescription == null) {
            purposeDescription = new StringFilter();
        }
        return purposeDescription;
    }

    public void setPurposeDescription(StringFilter purposeDescription) {
        this.purposeDescription = purposeDescription;
    }

    public LongFilter getActionRequiredId() {
        return actionRequiredId;
    }

    public LongFilter actionRequiredId() {
        if (actionRequiredId == null) {
            actionRequiredId = new LongFilter();
        }
        return actionRequiredId;
    }

    public void setActionRequiredId(LongFilter actionRequiredId) {
        this.actionRequiredId = actionRequiredId;
    }

    public LongFilter getAddressedToId() {
        return addressedToId;
    }

    public LongFilter addressedToId() {
        if (addressedToId == null) {
            addressedToId = new LongFilter();
        }
        return addressedToId;
    }

    public void setAddressedToId(LongFilter addressedToId) {
        this.addressedToId = addressedToId;
    }

    public LongFilter getFromId() {
        return fromId;
    }

    public LongFilter fromId() {
        if (fromId == null) {
            fromId = new LongFilter();
        }
        return fromId;
    }

    public void setFromId(LongFilter fromId) {
        this.fromId = fromId;
    }

    public LongFilter getPreparedById() {
        return preparedById;
    }

    public LongFilter preparedById() {
        if (preparedById == null) {
            preparedById = new LongFilter();
        }
        return preparedById;
    }

    public void setPreparedById(LongFilter preparedById) {
        this.preparedById = preparedById;
    }

    public LongFilter getReviewedById() {
        return reviewedById;
    }

    public LongFilter reviewedById() {
        if (reviewedById == null) {
            reviewedById = new LongFilter();
        }
        return reviewedById;
    }

    public void setReviewedById(LongFilter reviewedById) {
        this.reviewedById = reviewedById;
    }

    public LongFilter getApprovedById() {
        return approvedById;
    }

    public LongFilter approvedById() {
        if (approvedById == null) {
            approvedById = new LongFilter();
        }
        return approvedById;
    }

    public void setApprovedById(LongFilter approvedById) {
        this.approvedById = approvedById;
    }

    public LongFilter getMemoDocumentId() {
        return memoDocumentId;
    }

    public LongFilter memoDocumentId() {
        if (memoDocumentId == null) {
            memoDocumentId = new LongFilter();
        }
        return memoDocumentId;
    }

    public void setMemoDocumentId(LongFilter memoDocumentId) {
        this.memoDocumentId = memoDocumentId;
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
        final InternalMemoCriteria that = (InternalMemoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(catalogueNumber, that.catalogueNumber) &&
            Objects.equals(referenceNumber, that.referenceNumber) &&
            Objects.equals(memoDate, that.memoDate) &&
            Objects.equals(purposeDescription, that.purposeDescription) &&
            Objects.equals(actionRequiredId, that.actionRequiredId) &&
            Objects.equals(addressedToId, that.addressedToId) &&
            Objects.equals(fromId, that.fromId) &&
            Objects.equals(preparedById, that.preparedById) &&
            Objects.equals(reviewedById, that.reviewedById) &&
            Objects.equals(approvedById, that.approvedById) &&
            Objects.equals(memoDocumentId, that.memoDocumentId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            catalogueNumber,
            referenceNumber,
            memoDate,
            purposeDescription,
            actionRequiredId,
            addressedToId,
            fromId,
            preparedById,
            reviewedById,
            approvedById,
            memoDocumentId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InternalMemoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (catalogueNumber != null ? "catalogueNumber=" + catalogueNumber + ", " : "") +
            (referenceNumber != null ? "referenceNumber=" + referenceNumber + ", " : "") +
            (memoDate != null ? "memoDate=" + memoDate + ", " : "") +
            (purposeDescription != null ? "purposeDescription=" + purposeDescription + ", " : "") +
            (actionRequiredId != null ? "actionRequiredId=" + actionRequiredId + ", " : "") +
            (addressedToId != null ? "addressedToId=" + addressedToId + ", " : "") +
            (fromId != null ? "fromId=" + fromId + ", " : "") +
            (preparedById != null ? "preparedById=" + preparedById + ", " : "") +
            (reviewedById != null ? "reviewedById=" + reviewedById + ", " : "") +
            (approvedById != null ? "approvedById=" + approvedById + ", " : "") +
            (memoDocumentId != null ? "memoDocumentId=" + memoDocumentId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
