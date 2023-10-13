package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private StringFilter targetAssetNumber;

    private LongFilter workInProgressRegistrationId;

    private LongFilter placeholderId;

    private LongFilter businessDocumentId;

    private Boolean distinct;

    public WorkInProgressTransferCriteria() {}

    public WorkInProgressTransferCriteria(WorkInProgressTransferCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.targetAssetNumber = other.targetAssetNumber == null ? null : other.targetAssetNumber.copy();
        this.workInProgressRegistrationId = other.workInProgressRegistrationId == null ? null : other.workInProgressRegistrationId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
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
            Objects.equals(workInProgressRegistrationId, that.workInProgressRegistrationId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, targetAssetNumber, workInProgressRegistrationId, placeholderId, businessDocumentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressTransferCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (targetAssetNumber != null ? "targetAssetNumber=" + targetAssetNumber + ", " : "") +
            (workInProgressRegistrationId != null ? "workInProgressRegistrationId=" + workInProgressRegistrationId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
