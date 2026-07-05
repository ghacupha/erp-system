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

/**
 * Criteria class for the {@link io.github.erp.domain.RouInitialDirectCost} entity. This class is used
 * in {@link io.github.erp.web.rest.RouInitialDirectCostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rou-initial-direct-costs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RouInitialDirectCostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter transactionDate;

    private StringFilter description;

    private BigDecimalFilter cost;

    private LongFilter referenceNumber;

    private LongFilter leaseContractId;

    private LongFilter settlementDetailsId;

    private LongFilter targetROUAccountId;

    private LongFilter transferAccountId;

    private LongFilter placeholderId;

    private Boolean distinct;

    public RouInitialDirectCostCriteria() {}

    public RouInitialDirectCostCriteria(RouInitialDirectCostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.cost = other.cost == null ? null : other.cost.copy();
        this.referenceNumber = other.referenceNumber == null ? null : other.referenceNumber.copy();
        this.leaseContractId = other.leaseContractId == null ? null : other.leaseContractId.copy();
        this.settlementDetailsId = other.settlementDetailsId == null ? null : other.settlementDetailsId.copy();
        this.targetROUAccountId = other.targetROUAccountId == null ? null : other.targetROUAccountId.copy();
        this.transferAccountId = other.transferAccountId == null ? null : other.transferAccountId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RouInitialDirectCostCriteria copy() {
        return new RouInitialDirectCostCriteria(this);
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

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public LocalDateFilter transactionDate() {
        if (transactionDate == null) {
            transactionDate = new LocalDateFilter();
        }
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
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

    public BigDecimalFilter getCost() {
        return cost;
    }

    public BigDecimalFilter cost() {
        if (cost == null) {
            cost = new BigDecimalFilter();
        }
        return cost;
    }

    public void setCost(BigDecimalFilter cost) {
        this.cost = cost;
    }

    public LongFilter getReferenceNumber() {
        return referenceNumber;
    }

    public LongFilter referenceNumber() {
        if (referenceNumber == null) {
            referenceNumber = new LongFilter();
        }
        return referenceNumber;
    }

    public void setReferenceNumber(LongFilter referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LongFilter getLeaseContractId() {
        return leaseContractId;
    }

    public LongFilter leaseContractId() {
        if (leaseContractId == null) {
            leaseContractId = new LongFilter();
        }
        return leaseContractId;
    }

    public void setLeaseContractId(LongFilter leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public LongFilter getSettlementDetailsId() {
        return settlementDetailsId;
    }

    public LongFilter settlementDetailsId() {
        if (settlementDetailsId == null) {
            settlementDetailsId = new LongFilter();
        }
        return settlementDetailsId;
    }

    public void setSettlementDetailsId(LongFilter settlementDetailsId) {
        this.settlementDetailsId = settlementDetailsId;
    }

    public LongFilter getTargetROUAccountId() {
        return targetROUAccountId;
    }

    public LongFilter targetROUAccountId() {
        if (targetROUAccountId == null) {
            targetROUAccountId = new LongFilter();
        }
        return targetROUAccountId;
    }

    public void setTargetROUAccountId(LongFilter targetROUAccountId) {
        this.targetROUAccountId = targetROUAccountId;
    }

    public LongFilter getTransferAccountId() {
        return transferAccountId;
    }

    public LongFilter transferAccountId() {
        if (transferAccountId == null) {
            transferAccountId = new LongFilter();
        }
        return transferAccountId;
    }

    public void setTransferAccountId(LongFilter transferAccountId) {
        this.transferAccountId = transferAccountId;
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
        final RouInitialDirectCostCriteria that = (RouInitialDirectCostCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(referenceNumber, that.referenceNumber) &&
            Objects.equals(leaseContractId, that.leaseContractId) &&
            Objects.equals(settlementDetailsId, that.settlementDetailsId) &&
            Objects.equals(targetROUAccountId, that.targetROUAccountId) &&
            Objects.equals(transferAccountId, that.transferAccountId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            transactionDate,
            description,
            cost,
            referenceNumber,
            leaseContractId,
            settlementDetailsId,
            targetROUAccountId,
            transferAccountId,
            placeholderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouInitialDirectCostCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (cost != null ? "cost=" + cost + ", " : "") +
            (referenceNumber != null ? "referenceNumber=" + referenceNumber + ", " : "") +
            (leaseContractId != null ? "leaseContractId=" + leaseContractId + ", " : "") +
            (settlementDetailsId != null ? "settlementDetailsId=" + settlementDetailsId + ", " : "") +
            (targetROUAccountId != null ? "targetROUAccountId=" + targetROUAccountId + ", " : "") +
            (transferAccountId != null ? "transferAccountId=" + transferAccountId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
