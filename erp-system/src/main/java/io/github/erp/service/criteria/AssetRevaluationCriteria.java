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
 * Criteria class for the {@link io.github.erp.domain.AssetRevaluation} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetRevaluationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-revaluations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetRevaluationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private BigDecimalFilter devaluationAmount;

    private LocalDateFilter revaluationDate;

    private UUIDFilter revaluationReferenceId;

    private ZonedDateTimeFilter timeOfCreation;

    private LongFilter revaluerId;

    private LongFilter createdById;

    private LongFilter lastModifiedById;

    private LongFilter lastAccessedById;

    private LongFilter effectivePeriodId;

    private LongFilter revaluedAssetId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public AssetRevaluationCriteria() {}

    public AssetRevaluationCriteria(AssetRevaluationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.devaluationAmount = other.devaluationAmount == null ? null : other.devaluationAmount.copy();
        this.revaluationDate = other.revaluationDate == null ? null : other.revaluationDate.copy();
        this.revaluationReferenceId = other.revaluationReferenceId == null ? null : other.revaluationReferenceId.copy();
        this.timeOfCreation = other.timeOfCreation == null ? null : other.timeOfCreation.copy();
        this.revaluerId = other.revaluerId == null ? null : other.revaluerId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.lastModifiedById = other.lastModifiedById == null ? null : other.lastModifiedById.copy();
        this.lastAccessedById = other.lastAccessedById == null ? null : other.lastAccessedById.copy();
        this.effectivePeriodId = other.effectivePeriodId == null ? null : other.effectivePeriodId.copy();
        this.revaluedAssetId = other.revaluedAssetId == null ? null : other.revaluedAssetId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetRevaluationCriteria copy() {
        return new AssetRevaluationCriteria(this);
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

    public LocalDateFilter getRevaluationDate() {
        return revaluationDate;
    }

    public LocalDateFilter revaluationDate() {
        if (revaluationDate == null) {
            revaluationDate = new LocalDateFilter();
        }
        return revaluationDate;
    }

    public void setRevaluationDate(LocalDateFilter revaluationDate) {
        this.revaluationDate = revaluationDate;
    }

    public UUIDFilter getRevaluationReferenceId() {
        return revaluationReferenceId;
    }

    public UUIDFilter revaluationReferenceId() {
        if (revaluationReferenceId == null) {
            revaluationReferenceId = new UUIDFilter();
        }
        return revaluationReferenceId;
    }

    public void setRevaluationReferenceId(UUIDFilter revaluationReferenceId) {
        this.revaluationReferenceId = revaluationReferenceId;
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

    public LongFilter getRevaluerId() {
        return revaluerId;
    }

    public LongFilter revaluerId() {
        if (revaluerId == null) {
            revaluerId = new LongFilter();
        }
        return revaluerId;
    }

    public void setRevaluerId(LongFilter revaluerId) {
        this.revaluerId = revaluerId;
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

    public LongFilter getRevaluedAssetId() {
        return revaluedAssetId;
    }

    public LongFilter revaluedAssetId() {
        if (revaluedAssetId == null) {
            revaluedAssetId = new LongFilter();
        }
        return revaluedAssetId;
    }

    public void setRevaluedAssetId(LongFilter revaluedAssetId) {
        this.revaluedAssetId = revaluedAssetId;
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
        final AssetRevaluationCriteria that = (AssetRevaluationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(devaluationAmount, that.devaluationAmount) &&
            Objects.equals(revaluationDate, that.revaluationDate) &&
            Objects.equals(revaluationReferenceId, that.revaluationReferenceId) &&
            Objects.equals(timeOfCreation, that.timeOfCreation) &&
            Objects.equals(revaluerId, that.revaluerId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(lastModifiedById, that.lastModifiedById) &&
            Objects.equals(lastAccessedById, that.lastAccessedById) &&
            Objects.equals(effectivePeriodId, that.effectivePeriodId) &&
            Objects.equals(revaluedAssetId, that.revaluedAssetId) &&
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
            revaluationDate,
            revaluationReferenceId,
            timeOfCreation,
            revaluerId,
            createdById,
            lastModifiedById,
            lastAccessedById,
            effectivePeriodId,
            revaluedAssetId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRevaluationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (devaluationAmount != null ? "devaluationAmount=" + devaluationAmount + ", " : "") +
            (revaluationDate != null ? "revaluationDate=" + revaluationDate + ", " : "") +
            (revaluationReferenceId != null ? "revaluationReferenceId=" + revaluationReferenceId + ", " : "") +
            (timeOfCreation != null ? "timeOfCreation=" + timeOfCreation + ", " : "") +
            (revaluerId != null ? "revaluerId=" + revaluerId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (lastModifiedById != null ? "lastModifiedById=" + lastModifiedById + ", " : "") +
            (lastAccessedById != null ? "lastAccessedById=" + lastAccessedById + ", " : "") +
            (effectivePeriodId != null ? "effectivePeriodId=" + effectivePeriodId + ", " : "") +
            (revaluedAssetId != null ? "revaluedAssetId=" + revaluedAssetId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
