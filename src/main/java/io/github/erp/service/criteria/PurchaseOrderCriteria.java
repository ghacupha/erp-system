package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 2 (Caleb Series) Server ver 0.1.2-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

/**
 * Criteria class for the {@link io.github.erp.domain.PurchaseOrder} entity. This class is used
 * in {@link io.github.erp.web.rest.PurchaseOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /purchase-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PurchaseOrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter purchaseOrderNumber;

    private LocalDateFilter purchaseOrderDate;

    private BigDecimalFilter purchaseOrderAmount;

    private StringFilter description;

    private StringFilter notes;

    private StringFilter fileUploadToken;

    private StringFilter compilationToken;

    private LongFilter settlementCurrencyId;

    private LongFilter placeholderId;

    private LongFilter signatoriesId;

    private LongFilter vendorId;

    private Boolean distinct;

    public PurchaseOrderCriteria() {}

    public PurchaseOrderCriteria(PurchaseOrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.purchaseOrderNumber = other.purchaseOrderNumber == null ? null : other.purchaseOrderNumber.copy();
        this.purchaseOrderDate = other.purchaseOrderDate == null ? null : other.purchaseOrderDate.copy();
        this.purchaseOrderAmount = other.purchaseOrderAmount == null ? null : other.purchaseOrderAmount.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.fileUploadToken = other.fileUploadToken == null ? null : other.fileUploadToken.copy();
        this.compilationToken = other.compilationToken == null ? null : other.compilationToken.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.signatoriesId = other.signatoriesId == null ? null : other.signatoriesId.copy();
        this.vendorId = other.vendorId == null ? null : other.vendorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PurchaseOrderCriteria copy() {
        return new PurchaseOrderCriteria(this);
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

    public StringFilter getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public StringFilter purchaseOrderNumber() {
        if (purchaseOrderNumber == null) {
            purchaseOrderNumber = new StringFilter();
        }
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(StringFilter purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public LocalDateFilter getPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    public LocalDateFilter purchaseOrderDate() {
        if (purchaseOrderDate == null) {
            purchaseOrderDate = new LocalDateFilter();
        }
        return purchaseOrderDate;
    }

    public void setPurchaseOrderDate(LocalDateFilter purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }

    public BigDecimalFilter getPurchaseOrderAmount() {
        return purchaseOrderAmount;
    }

    public BigDecimalFilter purchaseOrderAmount() {
        if (purchaseOrderAmount == null) {
            purchaseOrderAmount = new BigDecimalFilter();
        }
        return purchaseOrderAmount;
    }

    public void setPurchaseOrderAmount(BigDecimalFilter purchaseOrderAmount) {
        this.purchaseOrderAmount = purchaseOrderAmount;
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

    public StringFilter getFileUploadToken() {
        return fileUploadToken;
    }

    public StringFilter fileUploadToken() {
        if (fileUploadToken == null) {
            fileUploadToken = new StringFilter();
        }
        return fileUploadToken;
    }

    public void setFileUploadToken(StringFilter fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public StringFilter getCompilationToken() {
        return compilationToken;
    }

    public StringFilter compilationToken() {
        if (compilationToken == null) {
            compilationToken = new StringFilter();
        }
        return compilationToken;
    }

    public void setCompilationToken(StringFilter compilationToken) {
        this.compilationToken = compilationToken;
    }

    public LongFilter getSettlementCurrencyId() {
        return settlementCurrencyId;
    }

    public LongFilter settlementCurrencyId() {
        if (settlementCurrencyId == null) {
            settlementCurrencyId = new LongFilter();
        }
        return settlementCurrencyId;
    }

    public void setSettlementCurrencyId(LongFilter settlementCurrencyId) {
        this.settlementCurrencyId = settlementCurrencyId;
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

    public LongFilter getSignatoriesId() {
        return signatoriesId;
    }

    public LongFilter signatoriesId() {
        if (signatoriesId == null) {
            signatoriesId = new LongFilter();
        }
        return signatoriesId;
    }

    public void setSignatoriesId(LongFilter signatoriesId) {
        this.signatoriesId = signatoriesId;
    }

    public LongFilter getVendorId() {
        return vendorId;
    }

    public LongFilter vendorId() {
        if (vendorId == null) {
            vendorId = new LongFilter();
        }
        return vendorId;
    }

    public void setVendorId(LongFilter vendorId) {
        this.vendorId = vendorId;
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
        final PurchaseOrderCriteria that = (PurchaseOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(purchaseOrderNumber, that.purchaseOrderNumber) &&
            Objects.equals(purchaseOrderDate, that.purchaseOrderDate) &&
            Objects.equals(purchaseOrderAmount, that.purchaseOrderAmount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(fileUploadToken, that.fileUploadToken) &&
            Objects.equals(compilationToken, that.compilationToken) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(signatoriesId, that.signatoriesId) &&
            Objects.equals(vendorId, that.vendorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            purchaseOrderNumber,
            purchaseOrderDate,
            purchaseOrderAmount,
            description,
            notes,
            fileUploadToken,
            compilationToken,
            settlementCurrencyId,
            placeholderId,
            signatoriesId,
            vendorId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (purchaseOrderNumber != null ? "purchaseOrderNumber=" + purchaseOrderNumber + ", " : "") +
            (purchaseOrderDate != null ? "purchaseOrderDate=" + purchaseOrderDate + ", " : "") +
            (purchaseOrderAmount != null ? "purchaseOrderAmount=" + purchaseOrderAmount + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (fileUploadToken != null ? "fileUploadToken=" + fileUploadToken + ", " : "") +
            (compilationToken != null ? "compilationToken=" + compilationToken + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (signatoriesId != null ? "signatoriesId=" + signatoriesId + ", " : "") +
            (vendorId != null ? "vendorId=" + vendorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
