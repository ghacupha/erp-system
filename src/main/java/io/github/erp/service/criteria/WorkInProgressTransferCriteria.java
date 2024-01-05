package io.github.erp.service.criteria;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.WorkInProgressTransferType;
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
 * Criteria class for the {@link io.github.erp.domain.WorkInProgressTransfer} entity. This class is used
 * in {@link io.github.erp.web.rest.WorkInProgressTransferResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /work-in-progress-transfers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WorkInProgressTransferCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WorkInProgressTransferType
     */
    public static class WorkInProgressTransferTypeFilter extends Filter<WorkInProgressTransferType> {

        public WorkInProgressTransferTypeFilter() {}

        public WorkInProgressTransferTypeFilter(WorkInProgressTransferTypeFilter filter) {
            super(filter);
        }

        @Override
        public WorkInProgressTransferTypeFilter copy() {
            return new WorkInProgressTransferTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter targetAssetNumber;

    private BigDecimalFilter transferAmount;

    private LocalDateFilter transferDate;

    private WorkInProgressTransferTypeFilter transferType;

    private LongFilter placeholderId;

    private LongFilter businessDocumentId;

    private LongFilter assetCategoryId;

    private LongFilter workInProgressRegistrationId;

    private LongFilter serviceOutletId;

    private LongFilter settlementId;

    private LongFilter workProjectRegisterId;

    private Boolean distinct;

    public WorkInProgressTransferCriteria() {}

    public WorkInProgressTransferCriteria(WorkInProgressTransferCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.targetAssetNumber = other.targetAssetNumber == null ? null : other.targetAssetNumber.copy();
        this.transferAmount = other.transferAmount == null ? null : other.transferAmount.copy();
        this.transferDate = other.transferDate == null ? null : other.transferDate.copy();
        this.transferType = other.transferType == null ? null : other.transferType.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.workInProgressRegistrationId = other.workInProgressRegistrationId == null ? null : other.workInProgressRegistrationId.copy();
        this.serviceOutletId = other.serviceOutletId == null ? null : other.serviceOutletId.copy();
        this.settlementId = other.settlementId == null ? null : other.settlementId.copy();
        this.workProjectRegisterId = other.workProjectRegisterId == null ? null : other.workProjectRegisterId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public WorkInProgressTransferCriteria copy() {
        return new WorkInProgressTransferCriteria(this);
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

    public StringFilter getTargetAssetNumber() {
        return targetAssetNumber;
    }

    public StringFilter targetAssetNumber() {
        if (targetAssetNumber == null) {
            targetAssetNumber = new StringFilter();
        }
        return targetAssetNumber;
    }

    public void setTargetAssetNumber(StringFilter targetAssetNumber) {
        this.targetAssetNumber = targetAssetNumber;
    }

    public BigDecimalFilter getTransferAmount() {
        return transferAmount;
    }

    public BigDecimalFilter transferAmount() {
        if (transferAmount == null) {
            transferAmount = new BigDecimalFilter();
        }
        return transferAmount;
    }

    public void setTransferAmount(BigDecimalFilter transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDateFilter getTransferDate() {
        return transferDate;
    }

    public LocalDateFilter transferDate() {
        if (transferDate == null) {
            transferDate = new LocalDateFilter();
        }
        return transferDate;
    }

    public void setTransferDate(LocalDateFilter transferDate) {
        this.transferDate = transferDate;
    }

    public WorkInProgressTransferTypeFilter getTransferType() {
        return transferType;
    }

    public WorkInProgressTransferTypeFilter transferType() {
        if (transferType == null) {
            transferType = new WorkInProgressTransferTypeFilter();
        }
        return transferType;
    }

    public void setTransferType(WorkInProgressTransferTypeFilter transferType) {
        this.transferType = transferType;
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

    public LongFilter getBusinessDocumentId() {
        return businessDocumentId;
    }

    public LongFilter businessDocumentId() {
        if (businessDocumentId == null) {
            businessDocumentId = new LongFilter();
        }
        return businessDocumentId;
    }

    public void setBusinessDocumentId(LongFilter businessDocumentId) {
        this.businessDocumentId = businessDocumentId;
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

    public LongFilter getWorkInProgressRegistrationId() {
        return workInProgressRegistrationId;
    }

    public LongFilter workInProgressRegistrationId() {
        if (workInProgressRegistrationId == null) {
            workInProgressRegistrationId = new LongFilter();
        }
        return workInProgressRegistrationId;
    }

    public void setWorkInProgressRegistrationId(LongFilter workInProgressRegistrationId) {
        this.workInProgressRegistrationId = workInProgressRegistrationId;
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

    public LongFilter getSettlementId() {
        return settlementId;
    }

    public LongFilter settlementId() {
        if (settlementId == null) {
            settlementId = new LongFilter();
        }
        return settlementId;
    }

    public void setSettlementId(LongFilter settlementId) {
        this.settlementId = settlementId;
    }

    public LongFilter getWorkProjectRegisterId() {
        return workProjectRegisterId;
    }

    public LongFilter workProjectRegisterId() {
        if (workProjectRegisterId == null) {
            workProjectRegisterId = new LongFilter();
        }
        return workProjectRegisterId;
    }

    public void setWorkProjectRegisterId(LongFilter workProjectRegisterId) {
        this.workProjectRegisterId = workProjectRegisterId;
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
        final WorkInProgressTransferCriteria that = (WorkInProgressTransferCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(targetAssetNumber, that.targetAssetNumber) &&
            Objects.equals(transferAmount, that.transferAmount) &&
            Objects.equals(transferDate, that.transferDate) &&
            Objects.equals(transferType, that.transferType) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(workInProgressRegistrationId, that.workInProgressRegistrationId) &&
            Objects.equals(serviceOutletId, that.serviceOutletId) &&
            Objects.equals(settlementId, that.settlementId) &&
            Objects.equals(workProjectRegisterId, that.workProjectRegisterId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            targetAssetNumber,
            transferAmount,
            transferDate,
            transferType,
            placeholderId,
            businessDocumentId,
            assetCategoryId,
            workInProgressRegistrationId,
            serviceOutletId,
            settlementId,
            workProjectRegisterId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressTransferCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (targetAssetNumber != null ? "targetAssetNumber=" + targetAssetNumber + ", " : "") +
            (transferAmount != null ? "transferAmount=" + transferAmount + ", " : "") +
            (transferDate != null ? "transferDate=" + transferDate + ", " : "") +
            (transferType != null ? "transferType=" + transferType + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (workInProgressRegistrationId != null ? "workInProgressRegistrationId=" + workInProgressRegistrationId + ", " : "") +
            (serviceOutletId != null ? "serviceOutletId=" + serviceOutletId + ", " : "") +
            (settlementId != null ? "settlementId=" + settlementId + ", " : "") +
            (workProjectRegisterId != null ? "workProjectRegisterId=" + workProjectRegisterId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
