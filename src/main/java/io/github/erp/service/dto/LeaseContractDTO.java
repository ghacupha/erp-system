package io.github.erp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LeaseContract} entity.
 */
public class LeaseContractDTO implements Serializable {

    private Long id;

    @NotNull
    private String bookingId;

    @NotNull
    private String leaseTitle;

    @NotNull
    private UUID identifier;

    private String description;

    @NotNull
    private LocalDate commencementDate;

    @NotNull
    private LocalDate terminalDate;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> systemMappings = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    private Set<ContractMetadataDTO> contractMetadata = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return leaseTitle;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getTerminalDate() {
        return terminalDate;
    }

    public void setTerminalDate(LocalDate terminalDate) {
        this.terminalDate = terminalDate;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getSystemMappings() {
        return systemMappings;
    }

    public void setSystemMappings(Set<UniversallyUniqueMappingDTO> systemMappings) {
        this.systemMappings = systemMappings;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public Set<ContractMetadataDTO> getContractMetadata() {
        return contractMetadata;
    }

    public void setContractMetadata(Set<ContractMetadataDTO> contractMetadata) {
        this.contractMetadata = contractMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseContractDTO)) {
            return false;
        }

        LeaseContractDTO leaseContractDTO = (LeaseContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaseContractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseContractDTO{" +
            "id=" + getId() +
            ", bookingId='" + getBookingId() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", description='" + getDescription() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", terminalDate='" + getTerminalDate() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", systemMappings=" + getSystemMappings() +
            ", businessDocuments=" + getBusinessDocuments() +
            ", contractMetadata=" + getContractMetadata() +
            "}";
    }
}
