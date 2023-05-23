package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.3
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * Criteria class for the {@link io.github.erp.domain.AssetWarranty} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetWarrantyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-warranties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetWarrantyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetTag;

    private StringFilter description;

    private StringFilter modelNumber;

    private StringFilter serialNumber;

    private LocalDateFilter expiryDate;

    private LongFilter placeholderId;

    private LongFilter universallyUniqueMappingId;

    private LongFilter dealerId;

    private LongFilter warrantyAttachmentId;

    private Boolean distinct;

    public AssetWarrantyCriteria() {}

    public AssetWarrantyCriteria(AssetWarrantyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetTag = other.assetTag == null ? null : other.assetTag.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.modelNumber = other.modelNumber == null ? null : other.modelNumber.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.expiryDate = other.expiryDate == null ? null : other.expiryDate.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.universallyUniqueMappingId = other.universallyUniqueMappingId == null ? null : other.universallyUniqueMappingId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.warrantyAttachmentId = other.warrantyAttachmentId == null ? null : other.warrantyAttachmentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetWarrantyCriteria copy() {
        return new AssetWarrantyCriteria(this);
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

    public StringFilter getAssetTag() {
        return assetTag;
    }

    public StringFilter assetTag() {
        if (assetTag == null) {
            assetTag = new StringFilter();
        }
        return assetTag;
    }

    public void setAssetTag(StringFilter assetTag) {
        this.assetTag = assetTag;
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

    public StringFilter getModelNumber() {
        return modelNumber;
    }

    public StringFilter modelNumber() {
        if (modelNumber == null) {
            modelNumber = new StringFilter();
        }
        return modelNumber;
    }

    public void setModelNumber(StringFilter modelNumber) {
        this.modelNumber = modelNumber;
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

    public LocalDateFilter getExpiryDate() {
        return expiryDate;
    }

    public LocalDateFilter expiryDate() {
        if (expiryDate == null) {
            expiryDate = new LocalDateFilter();
        }
        return expiryDate;
    }

    public void setExpiryDate(LocalDateFilter expiryDate) {
        this.expiryDate = expiryDate;
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

    public LongFilter getUniversallyUniqueMappingId() {
        return universallyUniqueMappingId;
    }

    public LongFilter universallyUniqueMappingId() {
        if (universallyUniqueMappingId == null) {
            universallyUniqueMappingId = new LongFilter();
        }
        return universallyUniqueMappingId;
    }

    public void setUniversallyUniqueMappingId(LongFilter universallyUniqueMappingId) {
        this.universallyUniqueMappingId = universallyUniqueMappingId;
    }

    public LongFilter getDealerId() {
        return dealerId;
    }

    public LongFilter dealerId() {
        if (dealerId == null) {
            dealerId = new LongFilter();
        }
        return dealerId;
    }

    public void setDealerId(LongFilter dealerId) {
        this.dealerId = dealerId;
    }

    public LongFilter getWarrantyAttachmentId() {
        return warrantyAttachmentId;
    }

    public LongFilter warrantyAttachmentId() {
        if (warrantyAttachmentId == null) {
            warrantyAttachmentId = new LongFilter();
        }
        return warrantyAttachmentId;
    }

    public void setWarrantyAttachmentId(LongFilter warrantyAttachmentId) {
        this.warrantyAttachmentId = warrantyAttachmentId;
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
        final AssetWarrantyCriteria that = (AssetWarrantyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetTag, that.assetTag) &&
            Objects.equals(description, that.description) &&
            Objects.equals(modelNumber, that.modelNumber) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(universallyUniqueMappingId, that.universallyUniqueMappingId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(warrantyAttachmentId, that.warrantyAttachmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetTag,
            description,
            modelNumber,
            serialNumber,
            expiryDate,
            placeholderId,
            universallyUniqueMappingId,
            dealerId,
            warrantyAttachmentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetWarrantyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetTag != null ? "assetTag=" + assetTag + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (modelNumber != null ? "modelNumber=" + modelNumber + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (expiryDate != null ? "expiryDate=" + expiryDate + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (universallyUniqueMappingId != null ? "universallyUniqueMappingId=" + universallyUniqueMappingId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (warrantyAttachmentId != null ? "warrantyAttachmentId=" + warrantyAttachmentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
