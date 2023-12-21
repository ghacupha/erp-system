package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
 * Criteria class for the {@link io.github.erp.domain.RelatedPartyRelationship} entity. This class is used
 * in {@link io.github.erp.web.rest.RelatedPartyRelationshipResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /related-party-relationships?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RelatedPartyRelationshipCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter reportingDate;

    private StringFilter customerId;

    private StringFilter relatedPartyId;

    private LongFilter bankCodeId;

    private LongFilter branchIdId;

    private LongFilter relationshipTypeId;

    private Boolean distinct;

    public RelatedPartyRelationshipCriteria() {}

    public RelatedPartyRelationshipCriteria(RelatedPartyRelationshipCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reportingDate = other.reportingDate == null ? null : other.reportingDate.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.relatedPartyId = other.relatedPartyId == null ? null : other.relatedPartyId.copy();
        this.bankCodeId = other.bankCodeId == null ? null : other.bankCodeId.copy();
        this.branchIdId = other.branchIdId == null ? null : other.branchIdId.copy();
        this.relationshipTypeId = other.relationshipTypeId == null ? null : other.relationshipTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RelatedPartyRelationshipCriteria copy() {
        return new RelatedPartyRelationshipCriteria(this);
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

    public LocalDateFilter getReportingDate() {
        return reportingDate;
    }

    public LocalDateFilter reportingDate() {
        if (reportingDate == null) {
            reportingDate = new LocalDateFilter();
        }
        return reportingDate;
    }

    public void setReportingDate(LocalDateFilter reportingDate) {
        this.reportingDate = reportingDate;
    }

    public StringFilter getCustomerId() {
        return customerId;
    }

    public StringFilter customerId() {
        if (customerId == null) {
            customerId = new StringFilter();
        }
        return customerId;
    }

    public void setCustomerId(StringFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getRelatedPartyId() {
        return relatedPartyId;
    }

    public StringFilter relatedPartyId() {
        if (relatedPartyId == null) {
            relatedPartyId = new StringFilter();
        }
        return relatedPartyId;
    }

    public void setRelatedPartyId(StringFilter relatedPartyId) {
        this.relatedPartyId = relatedPartyId;
    }

    public LongFilter getBankCodeId() {
        return bankCodeId;
    }

    public LongFilter bankCodeId() {
        if (bankCodeId == null) {
            bankCodeId = new LongFilter();
        }
        return bankCodeId;
    }

    public void setBankCodeId(LongFilter bankCodeId) {
        this.bankCodeId = bankCodeId;
    }

    public LongFilter getBranchIdId() {
        return branchIdId;
    }

    public LongFilter branchIdId() {
        if (branchIdId == null) {
            branchIdId = new LongFilter();
        }
        return branchIdId;
    }

    public void setBranchIdId(LongFilter branchIdId) {
        this.branchIdId = branchIdId;
    }

    public LongFilter getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public LongFilter relationshipTypeId() {
        if (relationshipTypeId == null) {
            relationshipTypeId = new LongFilter();
        }
        return relationshipTypeId;
    }

    public void setRelationshipTypeId(LongFilter relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
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
        final RelatedPartyRelationshipCriteria that = (RelatedPartyRelationshipCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reportingDate, that.reportingDate) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(relatedPartyId, that.relatedPartyId) &&
            Objects.equals(bankCodeId, that.bankCodeId) &&
            Objects.equals(branchIdId, that.branchIdId) &&
            Objects.equals(relationshipTypeId, that.relationshipTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportingDate, customerId, relatedPartyId, bankCodeId, branchIdId, relationshipTypeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelatedPartyRelationshipCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reportingDate != null ? "reportingDate=" + reportingDate + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (relatedPartyId != null ? "relatedPartyId=" + relatedPartyId + ", " : "") +
            (bankCodeId != null ? "bankCodeId=" + bankCodeId + ", " : "") +
            (branchIdId != null ? "branchIdId=" + branchIdId + ", " : "") +
            (relationshipTypeId != null ? "relationshipTypeId=" + relationshipTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
