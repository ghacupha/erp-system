package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.ContractStatus;
import io.github.erp.domain.enumeration.ContractType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ContractMetadata.
 */
@Entity
@Table(name = "contract_metadata")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contractmetadata")
public class ContractMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_contract", nullable = false)
    private ContractType typeOfContract;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status", nullable = false)
    private ContractStatus contractStatus;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "termination_date", nullable = false)
    private LocalDate terminationDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "comments_and_attachment")
    private String commentsAndAttachment;

    @NotNull
    @Column(name = "contract_title", nullable = false, unique = true)
    private String contractTitle;

    @NotNull
    @Column(name = "contract_identifier", nullable = false, unique = true)
    private UUID contractIdentifier;

    @NotNull
    @Size(min = 6)
    @Column(name = "contract_identifier_short", nullable = false, unique = true)
    private String contractIdentifierShort;

    @ManyToMany
    @JoinTable(
        name = "rel_contract_metadata__related_contracts",
        joinColumns = @JoinColumn(name = "contract_metadata_id"),
        inverseJoinColumns = @JoinColumn(name = "related_contracts_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "relatedContracts",
            "department",
            "contractPartner",
            "responsiblePerson",
            "signatories",
            "securityClearance",
            "placeholders",
            "contractDocumentFiles",
            "contractMappings",
        },
        allowSetters = true
    )
    private Set<ContractMetadata> relatedContracts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer department;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer contractPartner;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser responsiblePerson;

    @ManyToMany
    @JoinTable(
        name = "rel_contract_metadata__signatory",
        joinColumns = @JoinColumn(name = "contract_metadata_id"),
        inverseJoinColumns = @JoinColumn(name = "signatory_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private Set<ApplicationUser> signatories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "grantedClearances", "placeholders", "systemParameters" }, allowSetters = true)
    private SecurityClearance securityClearance;

    @ManyToMany
    @JoinTable(
        name = "rel_contract_metadata__placeholder",
        joinColumns = @JoinColumn(name = "contract_metadata_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_contract_metadata__contract_document_file",
        joinColumns = @JoinColumn(name = "contract_metadata_id"),
        inverseJoinColumns = @JoinColumn(name = "contract_document_file_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private Set<BusinessDocument> contractDocumentFiles = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_contract_metadata__contract_mappings",
        joinColumns = @JoinColumn(name = "contract_metadata_id"),
        inverseJoinColumns = @JoinColumn(name = "contract_mappings_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> contractMappings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContractMetadata id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public ContractMetadata description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContractType getTypeOfContract() {
        return this.typeOfContract;
    }

    public ContractMetadata typeOfContract(ContractType typeOfContract) {
        this.setTypeOfContract(typeOfContract);
        return this;
    }

    public void setTypeOfContract(ContractType typeOfContract) {
        this.typeOfContract = typeOfContract;
    }

    public ContractStatus getContractStatus() {
        return this.contractStatus;
    }

    public ContractMetadata contractStatus(ContractStatus contractStatus) {
        this.setContractStatus(contractStatus);
        return this;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public ContractMetadata startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTerminationDate() {
        return this.terminationDate;
    }

    public ContractMetadata terminationDate(LocalDate terminationDate) {
        this.setTerminationDate(terminationDate);
        return this;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getCommentsAndAttachment() {
        return this.commentsAndAttachment;
    }

    public ContractMetadata commentsAndAttachment(String commentsAndAttachment) {
        this.setCommentsAndAttachment(commentsAndAttachment);
        return this;
    }

    public void setCommentsAndAttachment(String commentsAndAttachment) {
        this.commentsAndAttachment = commentsAndAttachment;
    }

    public String getContractTitle() {
        return this.contractTitle;
    }

    public ContractMetadata contractTitle(String contractTitle) {
        this.setContractTitle(contractTitle);
        return this;
    }

    public void setContractTitle(String contractTitle) {
        this.contractTitle = contractTitle;
    }

    public UUID getContractIdentifier() {
        return this.contractIdentifier;
    }

    public ContractMetadata contractIdentifier(UUID contractIdentifier) {
        this.setContractIdentifier(contractIdentifier);
        return this;
    }

    public void setContractIdentifier(UUID contractIdentifier) {
        this.contractIdentifier = contractIdentifier;
    }

    public String getContractIdentifierShort() {
        return this.contractIdentifierShort;
    }

    public ContractMetadata contractIdentifierShort(String contractIdentifierShort) {
        this.setContractIdentifierShort(contractIdentifierShort);
        return this;
    }

    public void setContractIdentifierShort(String contractIdentifierShort) {
        this.contractIdentifierShort = contractIdentifierShort;
    }

    public Set<ContractMetadata> getRelatedContracts() {
        return this.relatedContracts;
    }

    public void setRelatedContracts(Set<ContractMetadata> contractMetadata) {
        this.relatedContracts = contractMetadata;
    }

    public ContractMetadata relatedContracts(Set<ContractMetadata> contractMetadata) {
        this.setRelatedContracts(contractMetadata);
        return this;
    }

    public ContractMetadata addRelatedContracts(ContractMetadata contractMetadata) {
        this.relatedContracts.add(contractMetadata);
        return this;
    }

    public ContractMetadata removeRelatedContracts(ContractMetadata contractMetadata) {
        this.relatedContracts.remove(contractMetadata);
        return this;
    }

    public Dealer getDepartment() {
        return this.department;
    }

    public void setDepartment(Dealer dealer) {
        this.department = dealer;
    }

    public ContractMetadata department(Dealer dealer) {
        this.setDepartment(dealer);
        return this;
    }

    public Dealer getContractPartner() {
        return this.contractPartner;
    }

    public void setContractPartner(Dealer dealer) {
        this.contractPartner = dealer;
    }

    public ContractMetadata contractPartner(Dealer dealer) {
        this.setContractPartner(dealer);
        return this;
    }

    public ApplicationUser getResponsiblePerson() {
        return this.responsiblePerson;
    }

    public void setResponsiblePerson(ApplicationUser applicationUser) {
        this.responsiblePerson = applicationUser;
    }

    public ContractMetadata responsiblePerson(ApplicationUser applicationUser) {
        this.setResponsiblePerson(applicationUser);
        return this;
    }

    public Set<ApplicationUser> getSignatories() {
        return this.signatories;
    }

    public void setSignatories(Set<ApplicationUser> applicationUsers) {
        this.signatories = applicationUsers;
    }

    public ContractMetadata signatories(Set<ApplicationUser> applicationUsers) {
        this.setSignatories(applicationUsers);
        return this;
    }

    public ContractMetadata addSignatory(ApplicationUser applicationUser) {
        this.signatories.add(applicationUser);
        return this;
    }

    public ContractMetadata removeSignatory(ApplicationUser applicationUser) {
        this.signatories.remove(applicationUser);
        return this;
    }

    public SecurityClearance getSecurityClearance() {
        return this.securityClearance;
    }

    public void setSecurityClearance(SecurityClearance securityClearance) {
        this.securityClearance = securityClearance;
    }

    public ContractMetadata securityClearance(SecurityClearance securityClearance) {
        this.setSecurityClearance(securityClearance);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public ContractMetadata placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public ContractMetadata addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public ContractMetadata removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<BusinessDocument> getContractDocumentFiles() {
        return this.contractDocumentFiles;
    }

    public void setContractDocumentFiles(Set<BusinessDocument> businessDocuments) {
        this.contractDocumentFiles = businessDocuments;
    }

    public ContractMetadata contractDocumentFiles(Set<BusinessDocument> businessDocuments) {
        this.setContractDocumentFiles(businessDocuments);
        return this;
    }

    public ContractMetadata addContractDocumentFile(BusinessDocument businessDocument) {
        this.contractDocumentFiles.add(businessDocument);
        return this;
    }

    public ContractMetadata removeContractDocumentFile(BusinessDocument businessDocument) {
        this.contractDocumentFiles.remove(businessDocument);
        return this;
    }

    public Set<UniversallyUniqueMapping> getContractMappings() {
        return this.contractMappings;
    }

    public void setContractMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.contractMappings = universallyUniqueMappings;
    }

    public ContractMetadata contractMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setContractMappings(universallyUniqueMappings);
        return this;
    }

    public ContractMetadata addContractMappings(UniversallyUniqueMapping universallyUniqueMapping) {
        this.contractMappings.add(universallyUniqueMapping);
        return this;
    }

    public ContractMetadata removeContractMappings(UniversallyUniqueMapping universallyUniqueMapping) {
        this.contractMappings.remove(universallyUniqueMapping);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractMetadata)) {
            return false;
        }
        return id != null && id.equals(((ContractMetadata) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractMetadata{" +
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
            "}";
    }
}
