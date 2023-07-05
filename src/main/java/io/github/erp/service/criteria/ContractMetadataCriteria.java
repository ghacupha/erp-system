package io.github.erp.service.criteria;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.0
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
import io.github.erp.domain.enumeration.ContractStatus;
import io.github.erp.domain.enumeration.ContractType;
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
 * Criteria class for the {@link io.github.erp.domain.ContractMetadata} entity. This class is used
 * in {@link io.github.erp.web.rest.ContractMetadataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contract-metadata?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContractMetadataCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContractType
     */
    public static class ContractTypeFilter extends Filter<ContractType> {

        public ContractTypeFilter() {}

        public ContractTypeFilter(ContractTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContractTypeFilter copy() {
            return new ContractTypeFilter(this);
        }
    }

    /**
     * Class for filtering ContractStatus
     */
    public static class ContractStatusFilter extends Filter<ContractStatus> {

        public ContractStatusFilter() {}

        public ContractStatusFilter(ContractStatusFilter filter) {
            super(filter);
        }

        @Override
        public ContractStatusFilter copy() {
            return new ContractStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private ContractTypeFilter typeOfContract;

    private ContractStatusFilter contractStatus;

    private LocalDateFilter startDate;

    private LocalDateFilter terminationDate;

    private StringFilter contractTitle;

    private UUIDFilter contractIdentifier;

    private StringFilter contractIdentifierShort;

    private LongFilter relatedContractsId;

    private LongFilter departmentId;

    private LongFilter contractPartnerId;

    private LongFilter responsiblePersonId;

    private LongFilter signatoryId;

    private LongFilter securityClearanceId;

    private LongFilter placeholderId;

    private LongFilter contractDocumentFileId;

    private LongFilter contractMappingsId;

    private Boolean distinct;

    public ContractMetadataCriteria() {}

    public ContractMetadataCriteria(ContractMetadataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.typeOfContract = other.typeOfContract == null ? null : other.typeOfContract.copy();
        this.contractStatus = other.contractStatus == null ? null : other.contractStatus.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.terminationDate = other.terminationDate == null ? null : other.terminationDate.copy();
        this.contractTitle = other.contractTitle == null ? null : other.contractTitle.copy();
        this.contractIdentifier = other.contractIdentifier == null ? null : other.contractIdentifier.copy();
        this.contractIdentifierShort = other.contractIdentifierShort == null ? null : other.contractIdentifierShort.copy();
        this.relatedContractsId = other.relatedContractsId == null ? null : other.relatedContractsId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.contractPartnerId = other.contractPartnerId == null ? null : other.contractPartnerId.copy();
        this.responsiblePersonId = other.responsiblePersonId == null ? null : other.responsiblePersonId.copy();
        this.signatoryId = other.signatoryId == null ? null : other.signatoryId.copy();
        this.securityClearanceId = other.securityClearanceId == null ? null : other.securityClearanceId.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.contractDocumentFileId = other.contractDocumentFileId == null ? null : other.contractDocumentFileId.copy();
        this.contractMappingsId = other.contractMappingsId == null ? null : other.contractMappingsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContractMetadataCriteria copy() {
        return new ContractMetadataCriteria(this);
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

    public ContractTypeFilter getTypeOfContract() {
        return typeOfContract;
    }

    public ContractTypeFilter typeOfContract() {
        if (typeOfContract == null) {
            typeOfContract = new ContractTypeFilter();
        }
        return typeOfContract;
    }

    public void setTypeOfContract(ContractTypeFilter typeOfContract) {
        this.typeOfContract = typeOfContract;
    }

    public ContractStatusFilter getContractStatus() {
        return contractStatus;
    }

    public ContractStatusFilter contractStatus() {
        if (contractStatus == null) {
            contractStatus = new ContractStatusFilter();
        }
        return contractStatus;
    }

    public void setContractStatus(ContractStatusFilter contractStatus) {
        this.contractStatus = contractStatus;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getTerminationDate() {
        return terminationDate;
    }

    public LocalDateFilter terminationDate() {
        if (terminationDate == null) {
            terminationDate = new LocalDateFilter();
        }
        return terminationDate;
    }

    public void setTerminationDate(LocalDateFilter terminationDate) {
        this.terminationDate = terminationDate;
    }

    public StringFilter getContractTitle() {
        return contractTitle;
    }

    public StringFilter contractTitle() {
        if (contractTitle == null) {
            contractTitle = new StringFilter();
        }
        return contractTitle;
    }

    public void setContractTitle(StringFilter contractTitle) {
        this.contractTitle = contractTitle;
    }

    public UUIDFilter getContractIdentifier() {
        return contractIdentifier;
    }

    public UUIDFilter contractIdentifier() {
        if (contractIdentifier == null) {
            contractIdentifier = new UUIDFilter();
        }
        return contractIdentifier;
    }

    public void setContractIdentifier(UUIDFilter contractIdentifier) {
        this.contractIdentifier = contractIdentifier;
    }

    public StringFilter getContractIdentifierShort() {
        return contractIdentifierShort;
    }

    public StringFilter contractIdentifierShort() {
        if (contractIdentifierShort == null) {
            contractIdentifierShort = new StringFilter();
        }
        return contractIdentifierShort;
    }

    public void setContractIdentifierShort(StringFilter contractIdentifierShort) {
        this.contractIdentifierShort = contractIdentifierShort;
    }

    public LongFilter getRelatedContractsId() {
        return relatedContractsId;
    }

    public LongFilter relatedContractsId() {
        if (relatedContractsId == null) {
            relatedContractsId = new LongFilter();
        }
        return relatedContractsId;
    }

    public void setRelatedContractsId(LongFilter relatedContractsId) {
        this.relatedContractsId = relatedContractsId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getContractPartnerId() {
        return contractPartnerId;
    }

    public LongFilter contractPartnerId() {
        if (contractPartnerId == null) {
            contractPartnerId = new LongFilter();
        }
        return contractPartnerId;
    }

    public void setContractPartnerId(LongFilter contractPartnerId) {
        this.contractPartnerId = contractPartnerId;
    }

    public LongFilter getResponsiblePersonId() {
        return responsiblePersonId;
    }

    public LongFilter responsiblePersonId() {
        if (responsiblePersonId == null) {
            responsiblePersonId = new LongFilter();
        }
        return responsiblePersonId;
    }

    public void setResponsiblePersonId(LongFilter responsiblePersonId) {
        this.responsiblePersonId = responsiblePersonId;
    }

    public LongFilter getSignatoryId() {
        return signatoryId;
    }

    public LongFilter signatoryId() {
        if (signatoryId == null) {
            signatoryId = new LongFilter();
        }
        return signatoryId;
    }

    public void setSignatoryId(LongFilter signatoryId) {
        this.signatoryId = signatoryId;
    }

    public LongFilter getSecurityClearanceId() {
        return securityClearanceId;
    }

    public LongFilter securityClearanceId() {
        if (securityClearanceId == null) {
            securityClearanceId = new LongFilter();
        }
        return securityClearanceId;
    }

    public void setSecurityClearanceId(LongFilter securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
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

    public LongFilter getContractDocumentFileId() {
        return contractDocumentFileId;
    }

    public LongFilter contractDocumentFileId() {
        if (contractDocumentFileId == null) {
            contractDocumentFileId = new LongFilter();
        }
        return contractDocumentFileId;
    }

    public void setContractDocumentFileId(LongFilter contractDocumentFileId) {
        this.contractDocumentFileId = contractDocumentFileId;
    }

    public LongFilter getContractMappingsId() {
        return contractMappingsId;
    }

    public LongFilter contractMappingsId() {
        if (contractMappingsId == null) {
            contractMappingsId = new LongFilter();
        }
        return contractMappingsId;
    }

    public void setContractMappingsId(LongFilter contractMappingsId) {
        this.contractMappingsId = contractMappingsId;
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
        final ContractMetadataCriteria that = (ContractMetadataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(typeOfContract, that.typeOfContract) &&
            Objects.equals(contractStatus, that.contractStatus) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(terminationDate, that.terminationDate) &&
            Objects.equals(contractTitle, that.contractTitle) &&
            Objects.equals(contractIdentifier, that.contractIdentifier) &&
            Objects.equals(contractIdentifierShort, that.contractIdentifierShort) &&
            Objects.equals(relatedContractsId, that.relatedContractsId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(contractPartnerId, that.contractPartnerId) &&
            Objects.equals(responsiblePersonId, that.responsiblePersonId) &&
            Objects.equals(signatoryId, that.signatoryId) &&
            Objects.equals(securityClearanceId, that.securityClearanceId) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(contractDocumentFileId, that.contractDocumentFileId) &&
            Objects.equals(contractMappingsId, that.contractMappingsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            typeOfContract,
            contractStatus,
            startDate,
            terminationDate,
            contractTitle,
            contractIdentifier,
            contractIdentifierShort,
            relatedContractsId,
            departmentId,
            contractPartnerId,
            responsiblePersonId,
            signatoryId,
            securityClearanceId,
            placeholderId,
            contractDocumentFileId,
            contractMappingsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractMetadataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (typeOfContract != null ? "typeOfContract=" + typeOfContract + ", " : "") +
            (contractStatus != null ? "contractStatus=" + contractStatus + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (terminationDate != null ? "terminationDate=" + terminationDate + ", " : "") +
            (contractTitle != null ? "contractTitle=" + contractTitle + ", " : "") +
            (contractIdentifier != null ? "contractIdentifier=" + contractIdentifier + ", " : "") +
            (contractIdentifierShort != null ? "contractIdentifierShort=" + contractIdentifierShort + ", " : "") +
            (relatedContractsId != null ? "relatedContractsId=" + relatedContractsId + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (contractPartnerId != null ? "contractPartnerId=" + contractPartnerId + ", " : "") +
            (responsiblePersonId != null ? "responsiblePersonId=" + responsiblePersonId + ", " : "") +
            (signatoryId != null ? "signatoryId=" + signatoryId + ", " : "") +
            (securityClearanceId != null ? "securityClearanceId=" + securityClearanceId + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (contractDocumentFileId != null ? "contractDocumentFileId=" + contractDocumentFileId + ", " : "") +
            (contractMappingsId != null ? "contractMappingsId=" + contractMappingsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
