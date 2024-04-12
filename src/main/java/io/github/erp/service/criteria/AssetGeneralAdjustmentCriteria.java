package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
 * Criteria class for the {@link io.github.erp.domain.AssetGeneralAdjustment} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetGeneralAdjustmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-general-adjustments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetGeneralAdjustmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private BigDecimalFilter devaluationAmount;

    private LocalDateFilter adjustmentDate;

    private ZonedDateTimeFilter timeOfCreation;

    private UUIDFilter adjustmentReferenceId;

    private LongFilter effectivePeriodId;

    private LongFilter assetRegistrationId;

    private LongFilter createdById;

    private LongFilter lastModifiedById;

    private LongFilter lastAccessedById;

    private LongFilter placeholderId;

    private Boolean distinct;

    public AssetGeneralAdjustmentCriteria() {}

    public AssetGeneralAdjustmentCriteria(AssetGeneralAdjustmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.devaluationAmount = other.devaluationAmount == null ? null : other.devaluationAmount.copy();
        this.adjustmentDate = other.adjustmentDate == null ? null : other.adjustmentDate.copy();
        this.timeOfCreation = other.timeOfCreation == null ? null : other.timeOfCreation.copy();
        this.adjustmentReferenceId = other.adjustmentReferenceId == null ? null : other.adjustmentReferenceId.copy();
        this.effectivePeriodId = other.effectivePeriodId == null ? null : other.effectivePeriodId.copy();
        this.assetRegistrationId = other.assetRegistrationId == null ? null : other.assetRegistrationId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.lastModifiedById = other.lastModifiedById == null ? null : other.lastModifiedById.copy();
        this.lastAccessedById = other.lastAccessedById == null ? null : other.lastAccessedById.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetGeneralAdjustmentCriteria copy() {
        return new AssetGeneralAdjustmentCriteria(this);
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

    public BigDecimalFilter getDevaluationAmount() {
        return devaluationAmount;
    }

    public BigDecimalFilter devaluationAmount() {
        if (devaluationAmount == null) {
            devaluationAmount = new BigDecimalFilter();
        }
        return devaluationAmount;
    }

    public void setDevaluationAmount(BigDecimalFilter devaluationAmount) {
        this.devaluationAmount = devaluationAmount;
    }

    public LocalDateFilter getAdjustmentDate() {
        return adjustmentDate;
    }

    public LocalDateFilter adjustmentDate() {
        if (adjustmentDate == null) {
            adjustmentDate = new LocalDateFilter();
        }
        return adjustmentDate;
    }

    public void setAdjustmentDate(LocalDateFilter adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public ZonedDateTimeFilter getTimeOfCreation() {
        return timeOfCreation;
    }

    public ZonedDateTimeFilter timeOfCreation() {
        if (timeOfCreation == null) {
            timeOfCreation = new ZonedDateTimeFilter();
        }
        return timeOfCreation;
    }

    public void setTimeOfCreation(ZonedDateTimeFilter timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public UUIDFilter getAdjustmentReferenceId() {
        return adjustmentReferenceId;
    }

    public UUIDFilter adjustmentReferenceId() {
        if (adjustmentReferenceId == null) {
            adjustmentReferenceId = new UUIDFilter();
        }
        return adjustmentReferenceId;
    }

    public void setAdjustmentReferenceId(UUIDFilter adjustmentReferenceId) {
        this.adjustmentReferenceId = adjustmentReferenceId;
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

    public LongFilter getAssetRegistrationId() {
        return assetRegistrationId;
    }

    public LongFilter assetRegistrationId() {
        if (assetRegistrationId == null) {
            assetRegistrationId = new LongFilter();
        }
        return assetRegistrationId;
    }

    public void setAssetRegistrationId(LongFilter assetRegistrationId) {
        this.assetRegistrationId = assetRegistrationId;
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

    public LongFilter getLastModifiedById() {
        return lastModifiedById;
    }

    public LongFilter lastModifiedById() {
        if (lastModifiedById == null) {
            lastModifiedById = new LongFilter();
        }
        return lastModifiedById;
    }

    public void setLastModifiedById(LongFilter lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
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
        final AssetGeneralAdjustmentCriteria that = (AssetGeneralAdjustmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(devaluationAmount, that.devaluationAmount) &&
            Objects.equals(adjustmentDate, that.adjustmentDate) &&
            Objects.equals(timeOfCreation, that.timeOfCreation) &&
            Objects.equals(adjustmentReferenceId, that.adjustmentReferenceId) &&
            Objects.equals(effectivePeriodId, that.effectivePeriodId) &&
            Objects.equals(assetRegistrationId, that.assetRegistrationId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(lastModifiedById, that.lastModifiedById) &&
            Objects.equals(lastAccessedById, that.lastAccessedById) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            devaluationAmount,
            adjustmentDate,
            timeOfCreation,
            adjustmentReferenceId,
            effectivePeriodId,
            assetRegistrationId,
            createdById,
            lastModifiedById,
            lastAccessedById,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetGeneralAdjustmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (devaluationAmount != null ? "devaluationAmount=" + devaluationAmount + ", " : "") +
            (adjustmentDate != null ? "adjustmentDate=" + adjustmentDate + ", " : "") +
            (timeOfCreation != null ? "timeOfCreation=" + timeOfCreation + ", " : "") +
            (adjustmentReferenceId != null ? "adjustmentReferenceId=" + adjustmentReferenceId + ", " : "") +
            (effectivePeriodId != null ? "effectivePeriodId=" + effectivePeriodId + ", " : "") +
            (assetRegistrationId != null ? "assetRegistrationId=" + assetRegistrationId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (lastModifiedById != null ? "lastModifiedById=" + lastModifiedById + ", " : "") +
            (lastAccessedById != null ? "lastAccessedById=" + lastAccessedById + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
