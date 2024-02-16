package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaseContract.
 */
@Entity
@Table(name = "lease_contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leasecontract")
public class LeaseContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "booking_id", nullable = false, unique = true)
    private String bookingId;

    @NotNull
    @Column(name = "lease_title", nullable = false, unique = true)
    private String leaseTitle;

    @NotNull
    @Column(name = "identifier", nullable = false, unique = true)
    private UUID identifier;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "commencement_date", nullable = false)
    private LocalDate commencementDate;

    @NotNull
    @Column(name = "terminal_date", nullable = false)
    private LocalDate terminalDate;

    @ManyToMany
    @JoinTable(
        name = "rel_lease_contract__placeholder",
        joinColumns = @JoinColumn(name = "lease_contract_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_lease_contract__system_mappings",
        joinColumns = @JoinColumn(name = "lease_contract_id"),
        inverseJoinColumns = @JoinColumn(name = "system_mappings_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> systemMappings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_lease_contract__business_document",
        joinColumns = @JoinColumn(name = "lease_contract_id"),
        inverseJoinColumns = @JoinColumn(name = "business_document_id")
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
    private Set<BusinessDocument> businessDocuments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_lease_contract__contract_metadata",
        joinColumns = @JoinColumn(name = "lease_contract_id"),
        inverseJoinColumns = @JoinColumn(name = "contract_metadata_id")
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
    private Set<ContractMetadata> contractMetadata = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseContract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public LeaseContract bookingId(String bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return this.leaseTitle;
    }

    public LeaseContract leaseTitle(String leaseTitle) {
        this.setLeaseTitle(leaseTitle);
        return this;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public UUID getIdentifier() {
        return this.identifier;
    }

    public LeaseContract identifier(UUID identifier) {
        this.setIdentifier(identifier);
        return this;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return this.description;
    }

    public LeaseContract description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCommencementDate() {
        return this.commencementDate;
    }

    public LeaseContract commencementDate(LocalDate commencementDate) {
        this.setCommencementDate(commencementDate);
        return this;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getTerminalDate() {
        return this.terminalDate;
    }

    public LeaseContract terminalDate(LocalDate terminalDate) {
        this.setTerminalDate(terminalDate);
        return this;
    }

    public void setTerminalDate(LocalDate terminalDate) {
        this.terminalDate = terminalDate;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public LeaseContract placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public LeaseContract addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public LeaseContract removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getSystemMappings() {
        return this.systemMappings;
    }

    public void setSystemMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.systemMappings = universallyUniqueMappings;
    }

    public LeaseContract systemMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setSystemMappings(universallyUniqueMappings);
        return this;
    }

    public LeaseContract addSystemMappings(UniversallyUniqueMapping universallyUniqueMapping) {
        this.systemMappings.add(universallyUniqueMapping);
        return this;
    }

    public LeaseContract removeSystemMappings(UniversallyUniqueMapping universallyUniqueMapping) {
        this.systemMappings.remove(universallyUniqueMapping);
        return this;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return this.businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public LeaseContract businessDocuments(Set<BusinessDocument> businessDocuments) {
        this.setBusinessDocuments(businessDocuments);
        return this;
    }

    public LeaseContract addBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.add(businessDocument);
        return this;
    }

    public LeaseContract removeBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.remove(businessDocument);
        return this;
    }

    public Set<ContractMetadata> getContractMetadata() {
        return this.contractMetadata;
    }

    public void setContractMetadata(Set<ContractMetadata> contractMetadata) {
        this.contractMetadata = contractMetadata;
    }

    public LeaseContract contractMetadata(Set<ContractMetadata> contractMetadata) {
        this.setContractMetadata(contractMetadata);
        return this;
    }

    public LeaseContract addContractMetadata(ContractMetadata contractMetadata) {
        this.contractMetadata.add(contractMetadata);
        return this;
    }

    public LeaseContract removeContractMetadata(ContractMetadata contractMetadata) {
        this.contractMetadata.remove(contractMetadata);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseContract)) {
            return false;
        }
        return id != null && id.equals(((LeaseContract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseContract{" +
            "id=" + getId() +
            ", bookingId='" + getBookingId() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", description='" + getDescription() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", terminalDate='" + getTerminalDate() + "'" +
            "}";
    }
}
