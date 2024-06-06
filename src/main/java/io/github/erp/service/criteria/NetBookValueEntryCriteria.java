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
 * Criteria class for the {@link io.github.erp.domain.NetBookValueEntry} entity. This class is used
 * in {@link io.github.erp.web.rest.NetBookValueEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /net-book-value-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NetBookValueEntryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetNumber;

    private StringFilter assetTag;

    private StringFilter assetDescription;

    private UUIDFilter nbvIdentifier;

    private UUIDFilter compilationJobIdentifier;

    private UUIDFilter compilationBatchIdentifier;

    private IntegerFilter elapsedMonths;

    private IntegerFilter priorMonths;

    private DoubleFilter usefulLifeYears;

    private BigDecimalFilter netBookValueAmount;

    private BigDecimalFilter previousNetBookValueAmount;

    private BigDecimalFilter historicalCost;

    private LocalDateFilter capitalizationDate;

    private LongFilter serviceOutletId;

    private LongFilter depreciationPeriodId;

    private LongFilter fiscalMonthId;

    private LongFilter depreciationMethodId;

    private LongFilter assetRegistrationId;

    private LongFilter assetCategoryId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public NetBookValueEntryCriteria() {}

    public NetBookValueEntryCriteria(NetBookValueEntryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetNumber = other.assetNumber == null ? null : other.assetNumber.copy();
        this.assetTag = other.assetTag == null ? null : other.assetTag.copy();
        this.assetDescription = other.assetDescription == null ? null : other.assetDescription.copy();
        this.nbvIdentifier = other.nbvIdentifier == null ? null : other.nbvIdentifier.copy();
        this.compilationJobIdentifier = other.compilationJobIdentifier == null ? null : other.compilationJobIdentifier.copy();
        this.compilationBatchIdentifier = other.compilationBatchIdentifier == null ? null : other.compilationBatchIdentifier.copy();
        this.elapsedMonths = other.elapsedMonths == null ? null : other.elapsedMonths.copy();
        this.priorMonths = other.priorMonths == null ? null : other.priorMonths.copy();
        this.usefulLifeYears = other.usefulLifeYears == null ? null : other.usefulLifeYears.copy();
        this.netBookValueAmount = other.netBookValueAmount == null ? null : other.netBookValueAmount.copy();
        this.previousNetBookValueAmount = other.previousNetBookValueAmount == null ? null : other.previousNetBookValueAmount.copy();
        this.historicalCost = other.historicalCost == null ? null : other.historicalCost.copy();
        this.capitalizationDate = other.capitalizationDate == null ? null : other.capitalizationDate.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.depreciationPeriodId = other.depreciationPeriodId == null ? null : other.depreciationPeriodId.copy();
        this.fiscalMonthId = other.fiscalMonthId == null ? null : other.fiscalMonthId.copy();
        this.depreciationMethodId = other.depreciationMethodId == null ? null : other.depreciationMethodId.copy();
        this.assetRegistrationId = other.assetRegistrationId == null ? null : other.assetRegistrationId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NetBookValueEntryCriteria copy() {
        return new NetBookValueEntryCriteria(this);
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

    public StringFilter getAssetNumber() {
        return assetNumber;
    }

    public StringFilter assetNumber() {
        if (assetNumber == null) {
            assetNumber = new StringFilter();
        }
        return assetNumber;
    }

    public void setAssetNumber(StringFilter assetNumber) {
        this.assetNumber = assetNumber;
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

    public StringFilter getAssetDescription() {
        return assetDescription;
    }

    public StringFilter assetDescription() {
        if (assetDescription == null) {
            assetDescription = new StringFilter();
        }
        return assetDescription;
    }

    public void setAssetDescription(StringFilter assetDescription) {
        this.assetDescription = assetDescription;
    }

    public UUIDFilter getNbvIdentifier() {
        return nbvIdentifier;
    }

    public UUIDFilter nbvIdentifier() {
        if (nbvIdentifier == null) {
            nbvIdentifier = new UUIDFilter();
        }
        return nbvIdentifier;
    }

    public void setNbvIdentifier(UUIDFilter nbvIdentifier) {
        this.nbvIdentifier = nbvIdentifier;
    }

    public UUIDFilter getCompilationJobIdentifier() {
        return compilationJobIdentifier;
    }

    public UUIDFilter compilationJobIdentifier() {
        if (compilationJobIdentifier == null) {
            compilationJobIdentifier = new UUIDFilter();
        }
        return compilationJobIdentifier;
    }

    public void setCompilationJobIdentifier(UUIDFilter compilationJobIdentifier) {
        this.compilationJobIdentifier = compilationJobIdentifier;
    }

    public UUIDFilter getCompilationBatchIdentifier() {
        return compilationBatchIdentifier;
    }

    public UUIDFilter compilationBatchIdentifier() {
        if (compilationBatchIdentifier == null) {
            compilationBatchIdentifier = new UUIDFilter();
        }
        return compilationBatchIdentifier;
    }

    public void setCompilationBatchIdentifier(UUIDFilter compilationBatchIdentifier) {
        this.compilationBatchIdentifier = compilationBatchIdentifier;
    }

    public IntegerFilter getElapsedMonths() {
        return elapsedMonths;
    }

    public IntegerFilter elapsedMonths() {
        if (elapsedMonths == null) {
            elapsedMonths = new IntegerFilter();
        }
        return elapsedMonths;
    }

    public void setElapsedMonths(IntegerFilter elapsedMonths) {
        this.elapsedMonths = elapsedMonths;
    }

    public IntegerFilter getPriorMonths() {
        return priorMonths;
    }

    public IntegerFilter priorMonths() {
        if (priorMonths == null) {
            priorMonths = new IntegerFilter();
        }
        return priorMonths;
    }

    public void setPriorMonths(IntegerFilter priorMonths) {
        this.priorMonths = priorMonths;
    }

    public DoubleFilter getUsefulLifeYears() {
        return usefulLifeYears;
    }

    public DoubleFilter usefulLifeYears() {
        if (usefulLifeYears == null) {
            usefulLifeYears = new DoubleFilter();
        }
        return usefulLifeYears;
    }

    public void setUsefulLifeYears(DoubleFilter usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public BigDecimalFilter getNetBookValueAmount() {
        return netBookValueAmount;
    }

    public BigDecimalFilter netBookValueAmount() {
        if (netBookValueAmount == null) {
            netBookValueAmount = new BigDecimalFilter();
        }
        return netBookValueAmount;
    }

    public void setNetBookValueAmount(BigDecimalFilter netBookValueAmount) {
        this.netBookValueAmount = netBookValueAmount;
    }

    public BigDecimalFilter getPreviousNetBookValueAmount() {
        return previousNetBookValueAmount;
    }

    public BigDecimalFilter previousNetBookValueAmount() {
        if (previousNetBookValueAmount == null) {
            previousNetBookValueAmount = new BigDecimalFilter();
        }
        return previousNetBookValueAmount;
    }

    public void setPreviousNetBookValueAmount(BigDecimalFilter previousNetBookValueAmount) {
        this.previousNetBookValueAmount = previousNetBookValueAmount;
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

    public LocalDateFilter getCapitalizationDate() {
        return capitalizationDate;
    }

    public LocalDateFilter capitalizationDate() {
        if (capitalizationDate == null) {
            capitalizationDate = new LocalDateFilter();
        }
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDateFilter capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public LongFilter getServiceOutletId() {
        return serviceOutletId;
    }

    public LongFilter serviceOutletId() {
        if (serviceOutletId == null) {
            serviceOutletId = new LongFilter();
        }
        return serviceOutletId;
    }

    public void setServiceOutletId(LongFilter serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
    }

    public LongFilter getDepreciationPeriodId() {
        return depreciationPeriodId;
    }

    public LongFilter depreciationPeriodId() {
        if (depreciationPeriodId == null) {
            depreciationPeriodId = new LongFilter();
        }
        return depreciationPeriodId;
    }

    public void setDepreciationPeriodId(LongFilter depreciationPeriodId) {
        this.depreciationPeriodId = depreciationPeriodId;
    }

    public LongFilter getFiscalMonthId() {
        return fiscalMonthId;
    }

    public LongFilter fiscalMonthId() {
        if (fiscalMonthId == null) {
            fiscalMonthId = new LongFilter();
        }
        return fiscalMonthId;
    }

    public void setFiscalMonthId(LongFilter fiscalMonthId) {
        this.fiscalMonthId = fiscalMonthId;
    }

    public LongFilter getDepreciationMethodId() {
        return depreciationMethodId;
    }

    public LongFilter depreciationMethodId() {
        if (depreciationMethodId == null) {
            depreciationMethodId = new LongFilter();
        }
        return depreciationMethodId;
    }

    public void setDepreciationMethodId(LongFilter depreciationMethodId) {
        this.depreciationMethodId = depreciationMethodId;
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

    public LongFilter getAssetCategoryId() {
        return assetCategoryId;
    }

    public LongFilter assetCategoryId() {
        if (assetCategoryId == null) {
            assetCategoryId = new LongFilter();
        }
        return assetCategoryId;
    }

    public void setAssetCategoryId(LongFilter assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
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
        final NetBookValueEntryCriteria that = (NetBookValueEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetNumber, that.assetNumber) &&
            Objects.equals(assetTag, that.assetTag) &&
            Objects.equals(assetDescription, that.assetDescription) &&
            Objects.equals(nbvIdentifier, that.nbvIdentifier) &&
            Objects.equals(compilationJobIdentifier, that.compilationJobIdentifier) &&
            Objects.equals(compilationBatchIdentifier, that.compilationBatchIdentifier) &&
            Objects.equals(elapsedMonths, that.elapsedMonths) &&
            Objects.equals(priorMonths, that.priorMonths) &&
            Objects.equals(usefulLifeYears, that.usefulLifeYears) &&
            Objects.equals(netBookValueAmount, that.netBookValueAmount) &&
            Objects.equals(previousNetBookValueAmount, that.previousNetBookValueAmount) &&
            Objects.equals(historicalCost, that.historicalCost) &&
            Objects.equals(capitalizationDate, that.capitalizationDate) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(depreciationPeriodId, that.depreciationPeriodId) &&
            Objects.equals(fiscalMonthId, that.fiscalMonthId) &&
            Objects.equals(depreciationMethodId, that.depreciationMethodId) &&
            Objects.equals(assetRegistrationId, that.assetRegistrationId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetNumber,
            assetTag,
            assetDescription,
            nbvIdentifier,
            compilationJobIdentifier,
            compilationBatchIdentifier,
            elapsedMonths,
            priorMonths,
            usefulLifeYears,
            netBookValueAmount,
            previousNetBookValueAmount,
            historicalCost,
            capitalizationDate,
            serviceOutletId,
            depreciationPeriodId,
            fiscalMonthId,
            depreciationMethodId,
            assetRegistrationId,
            assetCategoryId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NetBookValueEntryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetNumber != null ? "assetNumber=" + assetNumber + ", " : "") +
            (assetTag != null ? "assetTag=" + assetTag + ", " : "") +
            (assetDescription != null ? "assetDescription=" + assetDescription + ", " : "") +
            (nbvIdentifier != null ? "nbvIdentifier=" + nbvIdentifier + ", " : "") +
            (compilationJobIdentifier != null ? "compilationJobIdentifier=" + compilationJobIdentifier + ", " : "") +
            (compilationBatchIdentifier != null ? "compilationBatchIdentifier=" + compilationBatchIdentifier + ", " : "") +
            (elapsedMonths != null ? "elapsedMonths=" + elapsedMonths + ", " : "") +
            (priorMonths != null ? "priorMonths=" + priorMonths + ", " : "") +
            (usefulLifeYears != null ? "usefulLifeYears=" + usefulLifeYears + ", " : "") +
            (netBookValueAmount != null ? "netBookValueAmount=" + netBookValueAmount + ", " : "") +
            (previousNetBookValueAmount != null ? "previousNetBookValueAmount=" + previousNetBookValueAmount + ", " : "") +
            (historicalCost != null ? "historicalCost=" + historicalCost + ", " : "") +
            (capitalizationDate != null ? "capitalizationDate=" + capitalizationDate + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (depreciationPeriodId != null ? "depreciationPeriodId=" + depreciationPeriodId + ", " : "") +
            (fiscalMonthId != null ? "fiscalMonthId=" + fiscalMonthId + ", " : "") +
            (depreciationMethodId != null ? "depreciationMethodId=" + depreciationMethodId + ", " : "") +
            (assetRegistrationId != null ? "assetRegistrationId=" + assetRegistrationId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
