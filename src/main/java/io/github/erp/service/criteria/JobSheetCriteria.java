package io.github.erp.service.criteria;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.JobSheet} entity. This class is used
 * in {@link io.github.erp.web.rest.JobSheetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /job-sheets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobSheetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serialNumber;

    private LocalDateFilter jobSheetDate;

    private StringFilter details;

    private LongFilter billerId;

    private LongFilter signatoriesId;

    private LongFilter contactPersonId;

    private LongFilter businessStampsId;

    private LongFilter placeholderId;

    private LongFilter paymentLabelId;

    private LongFilter businessDocumentId;

    private Boolean distinct;

    public JobSheetCriteria() {}

    public JobSheetCriteria(JobSheetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.jobSheetDate = other.jobSheetDate == null ? null : other.jobSheetDate.copy();
        this.details = other.details == null ? null : other.details.copy();
        this.billerId = other.billerId == null ? null : other.billerId.copy();
        this.signatoriesId = other.signatoriesId == null ? null : other.signatoriesId.copy();
        this.contactPersonId = other.contactPersonId == null ? null : other.contactPersonId.copy();
        this.businessStampsId = other.businessStampsId == null ? null : other.businessStampsId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.paymentLabelId = other.paymentLabelId == null ? null : other.paymentLabelId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public JobSheetCriteria copy() {
        return new JobSheetCriteria(this);
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

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateFilter getJobSheetDate() {
        return jobSheetDate;
    }

    public LocalDateFilter jobSheetDate() {
        if (jobSheetDate == null) {
            jobSheetDate = new LocalDateFilter();
        }
        return jobSheetDate;
    }

    public void setJobSheetDate(LocalDateFilter jobSheetDate) {
        this.jobSheetDate = jobSheetDate;
    }

    public StringFilter getDetails() {
        return details;
    }

    public StringFilter details() {
        if (details == null) {
            details = new StringFilter();
        }
        return details;
    }

    public void setDetails(StringFilter details) {
        this.details = details;
    }

    public LongFilter getBillerId() {
        return billerId;
    }

    public LongFilter billerId() {
        if (billerId == null) {
            billerId = new LongFilter();
        }
        return billerId;
    }

    public void setBillerId(LongFilter billerId) {
        this.billerId = billerId;
    }

    public LongFilter getSignatoriesId() {
        return signatoriesId;
    }

    public LongFilter signatoriesId() {
        if (signatoriesId == null) {
            signatoriesId = new LongFilter();
        }
        return signatoriesId;
    }

    public void setSignatoriesId(LongFilter signatoriesId) {
        this.signatoriesId = signatoriesId;
    }

    public LongFilter getContactPersonId() {
        return contactPersonId;
    }

    public LongFilter contactPersonId() {
        if (contactPersonId == null) {
            contactPersonId = new LongFilter();
        }
        return contactPersonId;
    }

    public void setContactPersonId(LongFilter contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public LongFilter getBusinessStampsId() {
        return businessStampsId;
    }

    public LongFilter businessStampsId() {
        if (businessStampsId == null) {
            businessStampsId = new LongFilter();
        }
        return businessStampsId;
    }

    public void setBusinessStampsId(LongFilter businessStampsId) {
        this.businessStampsId = businessStampsId;
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

    public LongFilter getPaymentLabelId() {
        return paymentLabelId;
    }

    public LongFilter paymentLabelId() {
        if (paymentLabelId == null) {
            paymentLabelId = new LongFilter();
        }
        return paymentLabelId;
    }

    public void setPaymentLabelId(LongFilter paymentLabelId) {
        this.paymentLabelId = paymentLabelId;
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
        final JobSheetCriteria that = (JobSheetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(jobSheetDate, that.jobSheetDate) &&
            Objects.equals(details, that.details) &&
            Objects.equals(billerId, that.billerId) &&
            Objects.equals(signatoriesId, that.signatoriesId) &&
            Objects.equals(contactPersonId, that.contactPersonId) &&
            Objects.equals(businessStampsId, that.businessStampsId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(paymentLabelId, that.paymentLabelId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            serialNumber,
            jobSheetDate,
            details,
            billerId,
            signatoriesId,
            contactPersonId,
            businessStampsId,
            placeholderId,
            paymentLabelId,
            businessDocumentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobSheetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (jobSheetDate != null ? "jobSheetDate=" + jobSheetDate + ", " : "") +
            (details != null ? "details=" + details + ", " : "") +
            (billerId != null ? "billerId=" + billerId + ", " : "") +
            (signatoriesId != null ? "signatoriesId=" + signatoriesId + ", " : "") +
            (contactPersonId != null ? "contactPersonId=" + contactPersonId + ", " : "") +
            (businessStampsId != null ? "businessStampsId=" + businessStampsId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (paymentLabelId != null ? "paymentLabelId=" + paymentLabelId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
