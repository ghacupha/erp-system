package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
 * Criteria class for the {@link io.github.erp.domain.AssetDisposal} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetDisposalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-disposals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetDisposalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter assetDisposalReference;

    private StringFilter description;

    private BigDecimalFilter assetCost;

    private BigDecimalFilter historicalCost;

    private BigDecimalFilter accruedDepreciation;

    private BigDecimalFilter netBookValue;

    private LocalDateFilter decommissioningDate;

    private LocalDateFilter disposalDate;

    private BooleanFilter dormant;

    private LongFilter createdById;

    private LongFilter modifiedById;

    private LongFilter lastAccessedById;

    private LongFilter effectivePeriodId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public AssetDisposalCriteria() {}

    public AssetDisposalCriteria(AssetDisposalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetDisposalReference = other.assetDisposalReference == null ? null : other.assetDisposalReference.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.assetCost = other.assetCost == null ? null : other.assetCost.copy();
        this.historicalCost = other.historicalCost == null ? null : other.historicalCost.copy();
        this.accruedDepreciation = other.accruedDepreciation == null ? null : other.accruedDepreciation.copy();
        this.netBookValue = other.netBookValue == null ? null : other.netBookValue.copy();
        this.decommissioningDate = other.decommissioningDate == null ? null : other.decommissioningDate.copy();
        this.disposalDate = other.disposalDate == null ? null : other.disposalDate.copy();
        this.dormant = other.dormant == null ? null : other.dormant.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.modifiedById = other.modifiedById == null ? null : other.modifiedById.copy();
        this.lastAccessedById = other.lastAccessedById == null ? null : other.lastAccessedById.copy();
        this.effectivePeriodId = other.effectivePeriodId == null ? null : other.effectivePeriodId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetDisposalCriteria copy() {
        return new AssetDisposalCriteria(this);
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

    public UUIDFilter getAssetDisposalReference() {
        return assetDisposalReference;
    }

    public UUIDFilter assetDisposalReference() {
        if (assetDisposalReference == null) {
            assetDisposalReference = new UUIDFilter();
        }
        return assetDisposalReference;
    }

    public void setAssetDisposalReference(UUIDFilter assetDisposalReference) {
        this.assetDisposalReference = assetDisposalReference;
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

    public BigDecimalFilter getAssetCost() {
        return assetCost;
    }

    public BigDecimalFilter assetCost() {
        if (assetCost == null) {
            assetCost = new BigDecimalFilter();
        }
        return assetCost;
    }

    public void setAssetCost(BigDecimalFilter assetCost) {
        this.assetCost = assetCost;
    }

    public BigDecimalFilter getHistoricalCost() {
        return historicalCost;
    }

    public BigDecimalFilter historicalCost() {
        if (historicalCost == null) {
            historicalCost = new BigDecimalFilter();
        }
        return historicalCost;
    }

    public void setHistoricalCost(BigDecimalFilter historicalCost) {
        this.historicalCost = historicalCost;
    }

    public BigDecimalFilter getAccruedDepreciation() {
        return accruedDepreciation;
    }

    public BigDecimalFilter accruedDepreciation() {
        if (accruedDepreciation == null) {
            accruedDepreciation = new BigDecimalFilter();
        }
        return accruedDepreciation;
    }

    public void setAccruedDepreciation(BigDecimalFilter accruedDepreciation) {
        this.accruedDepreciation = accruedDepreciation;
    }

    public BigDecimalFilter getNetBookValue() {
        return netBookValue;
    }

    public BigDecimalFilter netBookValue() {
        if (netBookValue == null) {
            netBookValue = new BigDecimalFilter();
        }
        return netBookValue;
    }

    public void setNetBookValue(BigDecimalFilter netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDateFilter getDecommissioningDate() {
        return decommissioningDate;
    }

    public LocalDateFilter decommissioningDate() {
        if (decommissioningDate == null) {
            decommissioningDate = new LocalDateFilter();
        }
        return decommissioningDate;
    }

    public void setDecommissioningDate(LocalDateFilter decommissioningDate) {
        this.decommissioningDate = decommissioningDate;
    }

    public LocalDateFilter getDisposalDate() {
        return disposalDate;
    }

    public LocalDateFilter disposalDate() {
        if (disposalDate == null) {
            disposalDate = new LocalDateFilter();
        }
        return disposalDate;
    }

    public void setDisposalDate(LocalDateFilter disposalDate) {
        this.disposalDate = disposalDate;
    }

    public BooleanFilter getDormant() {
        return dormant;
    }

    public BooleanFilter dormant() {
        if (dormant == null) {
            dormant = new BooleanFilter();
        }
        return dormant;
    }

    public void setDormant(BooleanFilter dormant) {
        this.dormant = dormant;
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
        final AssetDisposalCriteria that = (AssetDisposalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetDisposalReference, that.assetDisposalReference) &&
            Objects.equals(description, that.description) &&
            Objects.equals(assetCost, that.assetCost) &&
            Objects.equals(historicalCost, that.historicalCost) &&
            Objects.equals(accruedDepreciation, that.accruedDepreciation) &&
            Objects.equals(netBookValue, that.netBookValue) &&
            Objects.equals(decommissioningDate, that.decommissioningDate) &&
            Objects.equals(disposalDate, that.disposalDate) &&
            Objects.equals(dormant, that.dormant) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(modifiedById, that.modifiedById) &&
            Objects.equals(lastAccessedById, that.lastAccessedById) &&
            Objects.equals(effectivePeriodId, that.effectivePeriodId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetDisposalReference,
            description,
            assetCost,
            historicalCost,
            accruedDepreciation,
            netBookValue,
            decommissioningDate,
            disposalDate,
            dormant,
            createdById,
            modifiedById,
            lastAccessedById,
            effectivePeriodId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDisposalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetDisposalReference != null ? "assetDisposalReference=" + assetDisposalReference + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (assetCost != null ? "assetCost=" + assetCost + ", " : "") +
            (historicalCost != null ? "historicalCost=" + historicalCost + ", " : "") +
            (accruedDepreciation != null ? "accruedDepreciation=" + accruedDepreciation + ", " : "") +
            (netBookValue != null ? "netBookValue=" + netBookValue + ", " : "") +
            (decommissioningDate != null ? "decommissioningDate=" + decommissioningDate + ", " : "") +
            (disposalDate != null ? "disposalDate=" + disposalDate + ", " : "") +
            (dormant != null ? "dormant=" + dormant + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (modifiedById != null ? "modifiedById=" + modifiedById + ", " : "") +
            (lastAccessedById != null ? "lastAccessedById=" + lastAccessedById + ", " : "") +
            (effectivePeriodId != null ? "effectivePeriodId=" + effectivePeriodId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
