package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.RouDepreciationEntry} entity. This class is used
 * in {@link io.github.erp.web.rest.RouDepreciationEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-depreciation-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouDepreciationEntryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private BigDecimalFilter depreciationAmount;

    private BigDecimalFilter outstandingAmount;

    private UUIDFilter rouAssetIdentifier;

    private UUIDFilter rouDepreciationIdentifier;

    private Boolean distinct;

    public RouDepreciationEntryCriteria() {}

    public RouDepreciationEntryCriteria(RouDepreciationEntryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.depreciationAmount = other.depreciationAmount == null ? null : other.depreciationAmount.copy();
        this.outstandingAmount = other.outstandingAmount == null ? null : other.outstandingAmount.copy();
        this.rouAssetIdentifier = other.rouAssetIdentifier == null ? null : other.rouAssetIdentifier.copy();
        this.rouDepreciationIdentifier = other.rouDepreciationIdentifier == null ? null : other.rouDepreciationIdentifier.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouDepreciationEntryCriteria copy() {
        return new RouDepreciationEntryCriteria(this);
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

    public BigDecimalFilter getOutstandingAmount() {
        return outstandingAmount;
    }

    public BigDecimalFilter outstandingAmount() {
        if (outstandingAmount == null) {
            outstandingAmount = new BigDecimalFilter();
        }
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimalFilter outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public UUIDFilter getRouAssetIdentifier() {
        return rouAssetIdentifier;
    }

    public UUIDFilter rouAssetIdentifier() {
        if (rouAssetIdentifier == null) {
            rouAssetIdentifier = new UUIDFilter();
        }
        return rouAssetIdentifier;
    }

    public void setRouAssetIdentifier(UUIDFilter rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public UUIDFilter getRouDepreciationIdentifier() {
        return rouDepreciationIdentifier;
    }

    public UUIDFilter rouDepreciationIdentifier() {
        if (rouDepreciationIdentifier == null) {
            rouDepreciationIdentifier = new UUIDFilter();
        }
        return rouDepreciationIdentifier;
    }

    public void setRouDepreciationIdentifier(UUIDFilter rouDepreciationIdentifier) {
        this.rouDepreciationIdentifier = rouDepreciationIdentifier;
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
        final RouDepreciationEntryCriteria that = (RouDepreciationEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(depreciationAmount, that.depreciationAmount) &&
            Objects.equals(outstandingAmount, that.outstandingAmount) &&
            Objects.equals(rouAssetIdentifier, that.rouAssetIdentifier) &&
            Objects.equals(rouDepreciationIdentifier, that.rouDepreciationIdentifier) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            depreciationAmount,
            outstandingAmount,
            rouAssetIdentifier,
            rouDepreciationIdentifier,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationEntryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (depreciationAmount != null ? "depreciationAmount=" + depreciationAmount + ", " : "") +
            (outstandingAmount != null ? "outstandingAmount=" + outstandingAmount + ", " : "") +
            (rouAssetIdentifier != null ? "rouAssetIdentifier=" + rouAssetIdentifier + ", " : "") +
            (rouDepreciationIdentifier != null ? "rouDepreciationIdentifier=" + rouDepreciationIdentifier + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
