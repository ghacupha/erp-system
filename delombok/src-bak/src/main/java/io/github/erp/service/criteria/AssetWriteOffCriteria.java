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

/**
 * Criteria class for the {@link io.github.erp.domain.AssetWriteOff} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetWriteOffResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-write-offs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetWriteOffCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private BigDecimalFilter writeOffAmount;

    private LocalDateFilter writeOffDate;

    private UUIDFilter writeOffReferenceId;

    private LongFilter createdById;

    private LongFilter modifiedById;

    private LongFilter lastAccessedById;

    private LongFilter effectivePeriodId;

    private LongFilter placeholderId;

    private LongFilter assetWrittenOffId;

    private Boolean distinct;

    public AssetWriteOffCriteria() {}

    public AssetWriteOffCriteria(AssetWriteOffCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.writeOffAmount = other.writeOffAmount == null ? null : other.writeOffAmount.copy();
        this.writeOffDate = other.writeOffDate == null ? null : other.writeOffDate.copy();
        this.writeOffReferenceId = other.writeOffReferenceId == null ? null : other.writeOffReferenceId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.modifiedById = other.modifiedById == null ? null : other.modifiedById.copy();
        this.lastAccessedById = other.lastAccessedById == null ? null : other.lastAccessedById.copy();
        this.effectivePeriodId = other.effectivePeriodId == null ? null : other.effectivePeriodId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.assetWrittenOffId = other.assetWrittenOffId == null ? null : other.assetWrittenOffId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetWriteOffCriteria copy() {
        return new AssetWriteOffCriteria(this);
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

    public BigDecimalFilter getWriteOffAmount() {
        return writeOffAmount;
    }

    public BigDecimalFilter writeOffAmount() {
        if (writeOffAmount == null) {
            writeOffAmount = new BigDecimalFilter();
        }
        return writeOffAmount;
    }

    public void setWriteOffAmount(BigDecimalFilter writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }

    public LocalDateFilter getWriteOffDate() {
        return writeOffDate;
    }

    public LocalDateFilter writeOffDate() {
        if (writeOffDate == null) {
            writeOffDate = new LocalDateFilter();
        }
        return writeOffDate;
    }

    public void setWriteOffDate(LocalDateFilter writeOffDate) {
        this.writeOffDate = writeOffDate;
    }

    public UUIDFilter getWriteOffReferenceId() {
        return writeOffReferenceId;
    }

    public UUIDFilter writeOffReferenceId() {
        if (writeOffReferenceId == null) {
            writeOffReferenceId = new UUIDFilter();
        }
        return writeOffReferenceId;
    }

    public void setWriteOffReferenceId(UUIDFilter writeOffReferenceId) {
        this.writeOffReferenceId = writeOffReferenceId;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getModifiedById() {
        return modifiedById;
    }

    public LongFilter modifiedById() {
        if (modifiedById == null) {
            modifiedById = new LongFilter();
        }
        return modifiedById;
    }

    public void setModifiedById(LongFilter modifiedById) {
        this.modifiedById = modifiedById;
    }

    public LongFilter getLastAccessedById() {
        return lastAccessedById;
    }

    public LongFilter lastAccessedById() {
        if (lastAccessedById == null) {
            lastAccessedById = new LongFilter();
        }
        return lastAccessedById;
    }

    public void setLastAccessedById(LongFilter lastAccessedById) {
        this.lastAccessedById = lastAccessedById;
    }

    public LongFilter getEffectivePeriodId() {
        return effectivePeriodId;
    }

    public LongFilter effectivePeriodId() {
        if (effectivePeriodId == null) {
            effectivePeriodId = new LongFilter();
        }
        return effectivePeriodId;
    }

    public void setEffectivePeriodId(LongFilter effectivePeriodId) {
        this.effectivePeriodId = effectivePeriodId;
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

    public LongFilter getAssetWrittenOffId() {
        return assetWrittenOffId;
    }

    public LongFilter assetWrittenOffId() {
        if (assetWrittenOffId == null) {
            assetWrittenOffId = new LongFilter();
        }
        return assetWrittenOffId;
    }

    public void setAssetWrittenOffId(LongFilter assetWrittenOffId) {
        this.assetWrittenOffId = assetWrittenOffId;
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
        final AssetWriteOffCriteria that = (AssetWriteOffCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(writeOffAmount, that.writeOffAmount) &&
            Objects.equals(writeOffDate, that.writeOffDate) &&
            Objects.equals(writeOffReferenceId, that.writeOffReferenceId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(modifiedById, that.modifiedById) &&
            Objects.equals(lastAccessedById, that.lastAccessedById) &&
            Objects.equals(effectivePeriodId, that.effectivePeriodId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(assetWrittenOffId, that.assetWrittenOffId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            writeOffAmount,
            writeOffDate,
            writeOffReferenceId,
            createdById,
            modifiedById,
            lastAccessedById,
            effectivePeriodId,
            placeholderId,
            assetWrittenOffId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetWriteOffCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (writeOffAmount != null ? "writeOffAmount=" + writeOffAmount + ", " : "") +
            (writeOffDate != null ? "writeOffDate=" + writeOffDate + ", " : "") +
            (writeOffReferenceId != null ? "writeOffReferenceId=" + writeOffReferenceId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (modifiedById != null ? "modifiedById=" + modifiedById + ", " : "") +
            (lastAccessedById != null ? "lastAccessedById=" + lastAccessedById + ", " : "") +
            (effectivePeriodId != null ? "effectivePeriodId=" + effectivePeriodId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (assetWrittenOffId != null ? "assetWrittenOffId=" + assetWrittenOffId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
