package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.enumeration.ContractStatus;
import io.github.erp.domain.enumeration.ContractType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ContractMetadata} entity.
 */
public class ContractMetadataDTO implements Serializable {

    private Long id;

    private String description;

    @NotNull
    private ContractType typeOfContract;

    @NotNull
    private ContractStatus contractStatus;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate terminationDate;

    @Lob
    private String commentsAndAttachment;

    @NotNull
    private String contractTitle;

    @NotNull
    private UUID contractIdentifier;

    @NotNull
    @Size(min = 6)
    private String contractIdentifierShort;

    private Set<ContractMetadataDTO> relatedContracts = new HashSet<>();

    private DealerDTO department;

    private DealerDTO contractPartner;

    private ApplicationUserDTO responsiblePerson;

    private Set<ApplicationUserDTO> signatories = new HashSet<>();

    private SecurityClearanceDTO securityClearance;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<BusinessDocumentDTO> contractDocumentFiles = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> contractMappings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContractType getTypeOfContract() {
        return typeOfContract;
    }

    public void setTypeOfContract(ContractType typeOfContract) {
        this.typeOfContract = typeOfContract;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getCommentsAndAttachment() {
        return commentsAndAttachment;
    }

    public void setCommentsAndAttachment(String commentsAndAttachment) {
        this.commentsAndAttachment = commentsAndAttachment;
    }

    public String getContractTitle() {
        return contractTitle;
    }

    public void setContractTitle(String contractTitle) {
        this.contractTitle = contractTitle;
    }

    public UUID getContractIdentifier() {
        return contractIdentifier;
    }

    public void setContractIdentifier(UUID contractIdentifier) {
        this.contractIdentifier = contractIdentifier;
    }

    public String getContractIdentifierShort() {
        return contractIdentifierShort;
    }

    public void setContractIdentifierShort(String contractIdentifierShort) {
        this.contractIdentifierShort = contractIdentifierShort;
    }

    public Set<ContractMetadataDTO> getRelatedContracts() {
        return relatedContracts;
    }

    public void setRelatedContracts(Set<ContractMetadataDTO> relatedContracts) {
        this.relatedContracts = relatedContracts;
    }

    public DealerDTO getDepartment() {
        return department;
    }

    public void setDepartment(DealerDTO department) {
        this.department = department;
    }

    public DealerDTO getContractPartner() {
        return contractPartner;
    }

    public void setContractPartner(DealerDTO contractPartner) {
        this.contractPartner = contractPartner;
    }

    public ApplicationUserDTO getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(ApplicationUserDTO responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public Set<ApplicationUserDTO> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<ApplicationUserDTO> signatories) {
        this.signatories = signatories;
    }

    public SecurityClearanceDTO getSecurityClearance() {
        return securityClearance;
    }

    public void setSecurityClearance(SecurityClearanceDTO securityClearance) {
        this.securityClearance = securityClearance;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<BusinessDocumentDTO> getContractDocumentFiles() {
        return contractDocumentFiles;
    }

    public void setContractDocumentFiles(Set<BusinessDocumentDTO> contractDocumentFiles) {
        this.contractDocumentFiles = contractDocumentFiles;
    }

    public Set<UniversallyUniqueMappingDTO> getContractMappings() {
        return contractMappings;
    }

    public void setContractMappings(Set<UniversallyUniqueMappingDTO> contractMappings) {
        this.contractMappings = contractMappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractMetadataDTO)) {
            return false;
        }

        ContractMetadataDTO contractMetadataDTO = (ContractMetadataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractMetadataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractMetadataDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", typeOfContract='" + getTypeOfContract() + "'" +
            ", contractStatus='" + getContractStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", terminationDate='" + getTerminationDate() + "'" +
            ", commentsAndAttachment='" + getCommentsAndAttachment() + "'" +
            ", contractTitle='" + getContractTitle() + "'" +
            ", contractIdentifier='" + getContractIdentifier() + "'" +
            ", contractIdentifierShort='" + getContractIdentifierShort() + "'" +
            ", relatedContracts=" + getRelatedContracts() +
            ", department=" + getDepartment() +
            ", contractPartner=" + getContractPartner() +
            ", responsiblePerson=" + getResponsiblePerson() +
            ", signatories=" + getSignatories() +
            ", securityClearance=" + getSecurityClearance() +
            ", placeholders=" + getPlaceholders() +
            ", contractDocumentFiles=" + getContractDocumentFiles() +
            ", contractMappings=" + getContractMappings() +
            "}";
    }
}
