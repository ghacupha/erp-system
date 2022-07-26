package io.github.erp.service.criteria;

/*-
 * Erp System - Mark II No 21 (Baruch Series)
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.WorkProjectRegister} entity. This class is used
 * in {@link io.github.erp.web.rest.WorkProjectRegisterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /work-project-registers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkProjectRegisterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter catalogueNumber;

    private StringFilter description;

    private BigDecimalFilter totalProjectCost;

    private LongFilter dealersId;

    private LongFilter settlementCurrencyId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public WorkProjectRegisterCriteria() {}

    public WorkProjectRegisterCriteria(WorkProjectRegisterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.catalogueNumber = other.catalogueNumber == null ? null : other.catalogueNumber.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.totalProjectCost = other.totalProjectCost == null ? null : other.totalProjectCost.copy();
        this.dealersId = other.dealersId == null ? null : other.dealersId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WorkProjectRegisterCriteria copy() {
        return new WorkProjectRegisterCriteria(this);
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

    public StringFilter getCatalogueNumber() {
        return catalogueNumber;
    }

    public StringFilter catalogueNumber() {
        if (catalogueNumber == null) {
            catalogueNumber = new StringFilter();
        }
        return catalogueNumber;
    }

    public void setCatalogueNumber(StringFilter catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
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

    public BigDecimalFilter getTotalProjectCost() {
        return totalProjectCost;
    }

    public BigDecimalFilter totalProjectCost() {
        if (totalProjectCost == null) {
            totalProjectCost = new BigDecimalFilter();
        }
        return totalProjectCost;
    }

    public void setTotalProjectCost(BigDecimalFilter totalProjectCost) {
        this.totalProjectCost = totalProjectCost;
    }

    public LongFilter getDealersId() {
        return dealersId;
    }

    public LongFilter dealersId() {
        if (dealersId == null) {
            dealersId = new LongFilter();
        }
        return dealersId;
    }

    public void setDealersId(LongFilter dealersId) {
        this.dealersId = dealersId;
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
        final WorkProjectRegisterCriteria that = (WorkProjectRegisterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(catalogueNumber, that.catalogueNumber) &&
            Objects.equals(description, that.description) &&
            Objects.equals(totalProjectCost, that.totalProjectCost) &&
            Objects.equals(dealersId, that.dealersId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, catalogueNumber, description, totalProjectCost, dealersId, settlementCurrencyId, placeholderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkProjectRegisterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (catalogueNumber != null ? "catalogueNumber=" + catalogueNumber + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (totalProjectCost != null ? "totalProjectCost=" + totalProjectCost + ", " : "") +
            (dealersId != null ? "dealersId=" + dealersId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
