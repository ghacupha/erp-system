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
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.TransactionDetails} entity. This class is used
 * in {@link io.github.erp.web.rest.TransactionDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter entryId;

    private LocalDateFilter transactionDate;

    private StringFilter description;

    private BigDecimalFilter amount;

    private BooleanFilter isDeleted;

    private UUIDFilter postingId;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter modifiedAt;

    private StringFilter transactionType;

    private LongFilter debitAccountId;

    private LongFilter creditAccountId;

    private LongFilter placeholderId;

    private LongFilter postedById;

    private Boolean distinct;

    public TransactionDetailsCriteria() {}

    public TransactionDetailsCriteria(TransactionDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entryId = other.entryId == null ? null : other.entryId.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.postingId = other.postingId == null ? null : other.postingId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.modifiedAt = other.modifiedAt == null ? null : other.modifiedAt.copy();
        this.transactionType = other.transactionType == null ? null : other.transactionType.copy();
        this.debitAccountId = other.debitAccountId == null ? null : other.debitAccountId.copy();
        this.creditAccountId = other.creditAccountId == null ? null : other.creditAccountId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.postedById = other.postedById == null ? null : other.postedById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionDetailsCriteria copy() {
        return new TransactionDetailsCriteria(this);
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

    public LongFilter getEntryId() {
        return entryId;
    }

    public LongFilter entryId() {
        if (entryId == null) {
            entryId = new LongFilter();
        }
        return entryId;
    }

    public void setEntryId(LongFilter entryId) {
        this.entryId = entryId;
    }

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public LocalDateFilter transactionDate() {
        if (transactionDate == null) {
            transactionDate = new LocalDateFilter();
        }
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            amount = new BigDecimalFilter();
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UUIDFilter getPostingId() {
        return postingId;
    }

    public UUIDFilter postingId() {
        if (postingId == null) {
            postingId = new UUIDFilter();
        }
        return postingId;
    }

    public void setPostingId(UUIDFilter postingId) {
        this.postingId = postingId;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getModifiedAt() {
        return modifiedAt;
    }

    public ZonedDateTimeFilter modifiedAt() {
        if (modifiedAt == null) {
            modifiedAt = new ZonedDateTimeFilter();
        }
        return modifiedAt;
    }

    public void setModifiedAt(ZonedDateTimeFilter modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public StringFilter getTransactionType() {
        return transactionType;
    }

    public StringFilter transactionType() {
        if (transactionType == null) {
            transactionType = new StringFilter();
        }
        return transactionType;
    }

    public void setTransactionType(StringFilter transactionType) {
        this.transactionType = transactionType;
    }

    public LongFilter getDebitAccountId() {
        return debitAccountId;
    }

    public LongFilter debitAccountId() {
        if (debitAccountId == null) {
            debitAccountId = new LongFilter();
        }
        return debitAccountId;
    }

    public void setDebitAccountId(LongFilter debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public LongFilter getCreditAccountId() {
        return creditAccountId;
    }

    public LongFilter creditAccountId() {
        if (creditAccountId == null) {
            creditAccountId = new LongFilter();
        }
        return creditAccountId;
    }

    public void setCreditAccountId(LongFilter creditAccountId) {
        this.creditAccountId = creditAccountId;
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

    public LongFilter getPostedById() {
        return postedById;
    }

    public LongFilter postedById() {
        if (postedById == null) {
            postedById = new LongFilter();
        }
        return postedById;
    }

    public void setPostedById(LongFilter postedById) {
        this.postedById = postedById;
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
        final TransactionDetailsCriteria that = (TransactionDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entryId, that.entryId) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(postingId, that.postingId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(modifiedAt, that.modifiedAt) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(debitAccountId, that.debitAccountId) &&
            Objects.equals(creditAccountId, that.creditAccountId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(postedById, that.postedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            entryId,
            transactionDate,
            description,
            amount,
            isDeleted,
            postingId,
            createdAt,
            modifiedAt,
            transactionType,
            debitAccountId,
            creditAccountId,
            placeholderId,
            postedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entryId != null ? "entryId=" + entryId + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (postingId != null ? "postingId=" + postingId + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (modifiedAt != null ? "modifiedAt=" + modifiedAt + ", " : "") +
            (transactionType != null ? "transactionType=" + transactionType + ", " : "") +
            (debitAccountId != null ? "debitAccountId=" + debitAccountId + ", " : "") +
            (creditAccountId != null ? "creditAccountId=" + creditAccountId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (postedById != null ? "postedById=" + postedById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
