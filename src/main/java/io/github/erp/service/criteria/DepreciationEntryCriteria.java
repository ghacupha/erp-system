package io.github.erp.service.criteria;

/*-
 * Erp System - Mark III No 16 (Caleb Series) Server ver 1.2.7
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
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.DepreciationEntry} entity. This class is used
 * in {@link io.github.erp.web.rest.DepreciationEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /depreciation-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepreciationEntryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter postedAt;

    private BigDecimalFilter depreciationAmount;

    private LongFilter assetNumber;

    private LongFilter serviceOutletId;

    private LongFilter assetCategoryId;

    private LongFilter depreciationMethodId;

    private LongFilter assetRegistrationId;

    private LongFilter depreciationPeriodId;

    private Boolean distinct;

    public DepreciationEntryCriteria() {}

    public DepreciationEntryCriteria(DepreciationEntryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.postedAt = other.postedAt == null ? null : other.postedAt.copy();
        this.depreciationAmount = other.depreciationAmount == null ? null : other.depreciationAmount.copy();
        this.assetNumber = other.assetNumber == null ? null : other.assetNumber.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.depreciationMethodId = other.depreciationMethodId == null ? null : other.depreciationMethodId.copy();
        this.assetRegistrationId = other.assetRegistrationId == null ? null : other.assetRegistrationId.copy();
        this.depreciationPeriodId = other.depreciationPeriodId == null ? null : other.depreciationPeriodId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepreciationEntryCriteria copy() {
        return new DepreciationEntryCriteria(this);
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

    public ZonedDateTimeFilter getPostedAt() {
        return postedAt;
    }

    public ZonedDateTimeFilter postedAt() {
        if (postedAt == null) {
            postedAt = new ZonedDateTimeFilter();
        }
        return postedAt;
    }

    public void setPostedAt(ZonedDateTimeFilter postedAt) {
        this.postedAt = postedAt;
    }

    public BigDecimalFilter getDepreciationAmount() {
        return depreciationAmount;
    }

    public BigDecimalFilter depreciationAmount() {
        if (depreciationAmount == null) {
            depreciationAmount = new BigDecimalFilter();
        }
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimalFilter depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public LongFilter getAssetNumber() {
        return assetNumber;
    }

    public LongFilter assetNumber() {
        if (assetNumber == null) {
            assetNumber = new LongFilter();
        }
        return assetNumber;
    }

    public void setAssetNumber(LongFilter assetNumber) {
        this.assetNumber = assetNumber;
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
        final DepreciationEntryCriteria that = (DepreciationEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(postedAt, that.postedAt) &&
            Objects.equals(depreciationAmount, that.depreciationAmount) &&
            Objects.equals(assetNumber, that.assetNumber) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(depreciationMethodId, that.depreciationMethodId) &&
            Objects.equals(assetRegistrationId, that.assetRegistrationId) &&
            Objects.equals(depreciationPeriodId, that.depreciationPeriodId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            postedAt,
            depreciationAmount,
            assetNumber,
            serviceOutletId,
            assetCategoryId,
            depreciationMethodId,
            assetRegistrationId,
            depreciationPeriodId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationEntryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (postedAt != null ? "postedAt=" + postedAt + ", " : "") +
            (depreciationAmount != null ? "depreciationAmount=" + depreciationAmount + ", " : "") +
            (assetNumber != null ? "assetNumber=" + assetNumber + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (depreciationMethodId != null ? "depreciationMethodId=" + depreciationMethodId + ", " : "") +
            (assetRegistrationId != null ? "assetRegistrationId=" + assetRegistrationId + ", " : "") +
            (depreciationPeriodId != null ? "depreciationPeriodId=" + depreciationPeriodId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
