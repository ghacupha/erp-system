package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
 * Criteria class for the {@link io.github.erp.domain.DeliveryNote} entity. This class is used
 * in {@link io.github.erp.web.rest.DeliveryNoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /delivery-notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DeliveryNoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deliveryNoteNumber;

    private LocalDateFilter documentDate;

    private StringFilter description;

    private StringFilter serialNumber;

    private IntegerFilter quantity;

    private LongFilter placeholderId;

    private LongFilter receivedById;

    private LongFilter deliveryStampsId;

    private LongFilter purchaseOrderId;

    private LongFilter supplierId;

    private LongFilter signatoriesId;

    private LongFilter otherPurchaseOrdersId;

    private LongFilter businessDocumentId;

    private Boolean distinct;

    public DeliveryNoteCriteria() {}

    public DeliveryNoteCriteria(DeliveryNoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deliveryNoteNumber = other.deliveryNoteNumber == null ? null : other.deliveryNoteNumber.copy();
        this.documentDate = other.documentDate == null ? null : other.documentDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.receivedById = other.receivedById == null ? null : other.receivedById.copy();
        this.deliveryStampsId = other.deliveryStampsId == null ? null : other.deliveryStampsId.copy();
        this.purchaseOrderId = other.purchaseOrderId == null ? null : other.purchaseOrderId.copy();
        this.supplierId = other.supplierId == null ? null : other.supplierId.copy();
        this.signatoriesId = other.signatoriesId == null ? null : other.signatoriesId.copy();
        this.otherPurchaseOrdersId = other.otherPurchaseOrdersId == null ? null : other.otherPurchaseOrdersId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DeliveryNoteCriteria copy() {
        return new DeliveryNoteCriteria(this);
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

    public StringFilter getDeliveryNoteNumber() {
        return deliveryNoteNumber;
    }

    public StringFilter deliveryNoteNumber() {
        if (deliveryNoteNumber == null) {
            deliveryNoteNumber = new StringFilter();
        }
        return deliveryNoteNumber;
    }

    public void setDeliveryNoteNumber(StringFilter deliveryNoteNumber) {
        this.deliveryNoteNumber = deliveryNoteNumber;
    }

    public LocalDateFilter getDocumentDate() {
        return documentDate;
    }

    public LocalDateFilter documentDate() {
        if (documentDate == null) {
            documentDate = new LocalDateFilter();
        }
        return documentDate;
    }

    public void setDocumentDate(LocalDateFilter documentDate) {
        this.documentDate = documentDate;
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

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
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

    public LongFilter getReceivedById() {
        return receivedById;
    }

    public LongFilter receivedById() {
        if (receivedById == null) {
            receivedById = new LongFilter();
        }
        return receivedById;
    }

    public void setReceivedById(LongFilter receivedById) {
        this.receivedById = receivedById;
    }

    public LongFilter getDeliveryStampsId() {
        return deliveryStampsId;
    }

    public LongFilter deliveryStampsId() {
        if (deliveryStampsId == null) {
            deliveryStampsId = new LongFilter();
        }
        return deliveryStampsId;
    }

    public void setDeliveryStampsId(LongFilter deliveryStampsId) {
        this.deliveryStampsId = deliveryStampsId;
    }

    public LongFilter getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public LongFilter purchaseOrderId() {
        if (purchaseOrderId == null) {
            purchaseOrderId = new LongFilter();
        }
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(LongFilter purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            supplierId = new LongFilter();
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
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

    public LongFilter getOtherPurchaseOrdersId() {
        return otherPurchaseOrdersId;
    }

    public LongFilter otherPurchaseOrdersId() {
        if (otherPurchaseOrdersId == null) {
            otherPurchaseOrdersId = new LongFilter();
        }
        return otherPurchaseOrdersId;
    }

    public void setOtherPurchaseOrdersId(LongFilter otherPurchaseOrdersId) {
        this.otherPurchaseOrdersId = otherPurchaseOrdersId;
    }

    public LongFilter getBusinessDocumentId() {
        return businessDocumentId;
    }

    public LongFilter businessDocumentId() {
        if (businessDocumentId == null) {
            businessDocumentId = new LongFilter();
        }
        return businessDocumentId;
    }

    public void setBusinessDocumentId(LongFilter businessDocumentId) {
        this.businessDocumentId = businessDocumentId;
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
        final DeliveryNoteCriteria that = (DeliveryNoteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deliveryNoteNumber, that.deliveryNoteNumber) &&
            Objects.equals(documentDate, that.documentDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(receivedById, that.receivedById) &&
            Objects.equals(deliveryStampsId, that.deliveryStampsId) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(signatoriesId, that.signatoriesId) &&
            Objects.equals(otherPurchaseOrdersId, that.otherPurchaseOrdersId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            deliveryNoteNumber,
            documentDate,
            description,
            serialNumber,
            quantity,
            placeholderId,
            receivedById,
            deliveryStampsId,
            purchaseOrderId,
            supplierId,
            signatoriesId,
            otherPurchaseOrdersId,
            businessDocumentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryNoteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (deliveryNoteNumber != null ? "deliveryNoteNumber=" + deliveryNoteNumber + ", " : "") +
            (documentDate != null ? "documentDate=" + documentDate + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (receivedById != null ? "receivedById=" + receivedById + ", " : "") +
            (deliveryStampsId != null ? "deliveryStampsId=" + deliveryStampsId + ", " : "") +
            (purchaseOrderId != null ? "purchaseOrderId=" + purchaseOrderId + ", " : "") +
            (supplierId != null ? "supplierId=" + supplierId + ", " : "") +
            (signatoriesId != null ? "signatoriesId=" + signatoriesId + ", " : "") +
            (otherPurchaseOrdersId != null ? "otherPurchaseOrdersId=" + otherPurchaseOrdersId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
