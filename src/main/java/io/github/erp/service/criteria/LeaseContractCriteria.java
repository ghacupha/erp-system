package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.LeaseContract} entity. This class is used
 * in {@link io.github.erp.web.rest.LeaseContractResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-contracts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseContractCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bookingId;

    private StringFilter leaseTitle;

    private UUIDFilter identifier;

    private StringFilter description;

    private LocalDateFilter commencementDate;

    private LocalDateFilter terminalDate;

    private LongFilter placeholderId;

    private LongFilter systemMappingsId;

    private LongFilter businessDocumentId;

    private LongFilter contractMetadataId;

    private Boolean distinct;

    public LeaseContractCriteria() {}

    public LeaseContractCriteria(LeaseContractCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.leaseTitle = other.leaseTitle == null ? null : other.leaseTitle.copy();
        this.identifier = other.identifier == null ? null : other.identifier.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.commencementDate = other.commencementDate == null ? null : other.commencementDate.copy();
        this.terminalDate = other.terminalDate == null ? null : other.terminalDate.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.systemMappingsId = other.systemMappingsId == null ? null : other.systemMappingsId.copy();
        this.businessDocumentId = other.businessDocumentId == null ? null : other.businessDocumentId.copy();
        this.contractMetadataId = other.contractMetadataId == null ? null : other.contractMetadataId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaseContractCriteria copy() {
        return new LeaseContractCriteria(this);
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

    public StringFilter getBookingId() {
        return bookingId;
    }

    public StringFilter bookingId() {
        if (bookingId == null) {
            bookingId = new StringFilter();
        }
        return bookingId;
    }

    public void setBookingId(StringFilter bookingId) {
        this.bookingId = bookingId;
    }

    public StringFilter getLeaseTitle() {
        return leaseTitle;
    }

    public StringFilter leaseTitle() {
        if (leaseTitle == null) {
            leaseTitle = new StringFilter();
        }
        return leaseTitle;
    }

    public void setLeaseTitle(StringFilter leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public UUIDFilter getIdentifier() {
        return identifier;
    }

    public UUIDFilter identifier() {
        if (identifier == null) {
            identifier = new UUIDFilter();
        }
        return identifier;
    }

    public void setIdentifier(UUIDFilter identifier) {
        this.identifier = identifier;
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

    public LocalDateFilter getCommencementDate() {
        return commencementDate;
    }

    public LocalDateFilter commencementDate() {
        if (commencementDate == null) {
            commencementDate = new LocalDateFilter();
        }
        return commencementDate;
    }

    public void setCommencementDate(LocalDateFilter commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDateFilter getTerminalDate() {
        return terminalDate;
    }

    public LocalDateFilter terminalDate() {
        if (terminalDate == null) {
            terminalDate = new LocalDateFilter();
        }
        return terminalDate;
    }

    public void setTerminalDate(LocalDateFilter terminalDate) {
        this.terminalDate = terminalDate;
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

    public LongFilter getSystemMappingsId() {
        return systemMappingsId;
    }

    public LongFilter systemMappingsId() {
        if (systemMappingsId == null) {
            systemMappingsId = new LongFilter();
        }
        return systemMappingsId;
    }

    public void setSystemMappingsId(LongFilter systemMappingsId) {
        this.systemMappingsId = systemMappingsId;
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

    public LongFilter getContractMetadataId() {
        return contractMetadataId;
    }

    public LongFilter contractMetadataId() {
        if (contractMetadataId == null) {
            contractMetadataId = new LongFilter();
        }
        return contractMetadataId;
    }

    public void setContractMetadataId(LongFilter contractMetadataId) {
        this.contractMetadataId = contractMetadataId;
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
        final LeaseContractCriteria that = (LeaseContractCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(leaseTitle, that.leaseTitle) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(description, that.description) &&
            Objects.equals(commencementDate, that.commencementDate) &&
            Objects.equals(terminalDate, that.terminalDate) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(systemMappingsId, that.systemMappingsId) &&
            Objects.equals(businessDocumentId, that.businessDocumentId) &&
            Objects.equals(contractMetadataId, that.contractMetadataId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            bookingId,
            leaseTitle,
            identifier,
            description,
            commencementDate,
            terminalDate,
            placeholderId,
            systemMappingsId,
            businessDocumentId,
            contractMetadataId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseContractCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (leaseTitle != null ? "leaseTitle=" + leaseTitle + ", " : "") +
            (identifier != null ? "identifier=" + identifier + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (commencementDate != null ? "commencementDate=" + commencementDate + ", " : "") +
            (terminalDate != null ? "terminalDate=" + terminalDate + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (systemMappingsId != null ? "systemMappingsId=" + systemMappingsId + ", " : "") +
            (businessDocumentId != null ? "businessDocumentId=" + businessDocumentId + ", " : "") +
            (contractMetadataId != null ? "contractMetadataId=" + contractMetadataId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
