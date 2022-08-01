package io.github.erp.service.dto;

import io.github.erp.domain.enumeration.DepreciationTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationMethod} entity.
 */
public class DepreciationMethodDTO implements Serializable {

    private Long id;

    @NotNull
    private String depreciationMethodName;

    private String description;

    @NotNull
    private DepreciationTypes depreciationType;

    @Lob
    private String remarks;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepreciationMethodName() {
        return depreciationMethodName;
    }

    public void setDepreciationMethodName(String depreciationMethodName) {
        this.depreciationMethodName = depreciationMethodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DepreciationTypes getDepreciationType() {
        return depreciationType;
    }

    public void setDepreciationType(DepreciationTypes depreciationType) {
        this.depreciationType = depreciationType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        if (!(o instanceof DepreciationMethodDTO)) {
            return false;
        }

        DepreciationMethodDTO depreciationMethodDTO = (DepreciationMethodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationMethodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationMethodDTO{" +
            "id=" + getId() +
            ", depreciationMethodName='" + getDepreciationMethodName() + "'" +
            ", description='" + getDescription() + "'" +
            ", depreciationType='" + getDepreciationType() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
