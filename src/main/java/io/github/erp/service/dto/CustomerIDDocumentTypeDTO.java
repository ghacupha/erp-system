package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CustomerIDDocumentType} entity.
 */
public class CustomerIDDocumentTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String documentCode;

    @NotNull
    private String documentType;

    private String documentTypeDescription;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTypeDescription() {
        return documentTypeDescription;
    }

    public void setDocumentTypeDescription(String documentTypeDescription) {
        this.documentTypeDescription = documentTypeDescription;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerIDDocumentTypeDTO)) {
            return false;
        }

        CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO = (CustomerIDDocumentTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerIDDocumentTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerIDDocumentTypeDTO{" +
            "id=" + getId() +
            ", documentCode='" + getDocumentCode() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", documentTypeDescription='" + getDocumentTypeDescription() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
