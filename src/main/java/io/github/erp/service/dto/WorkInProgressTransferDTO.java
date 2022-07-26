package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.github.erp.domain.WorkInProgressTransfer} entity.
 */
public class WorkInProgressTransferDTO implements Serializable {

    private Long id;

    private String description;

    private String targetAssetNumber;

    private Set<WorkInProgressRegistrationDTO> workInProgressRegistrations = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

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

    public String getTargetAssetNumber() {
        return targetAssetNumber;
    }

    public void setTargetAssetNumber(String targetAssetNumber) {
        this.targetAssetNumber = targetAssetNumber;
    }

    public Set<WorkInProgressRegistrationDTO> getWorkInProgressRegistrations() {
        return workInProgressRegistrations;
    }

    public void setWorkInProgressRegistrations(Set<WorkInProgressRegistrationDTO> workInProgressRegistrations) {
        this.workInProgressRegistrations = workInProgressRegistrations;
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
        if (!(o instanceof WorkInProgressTransferDTO)) {
            return false;
        }

        WorkInProgressTransferDTO workInProgressTransferDTO = (WorkInProgressTransferDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workInProgressTransferDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressTransferDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", targetAssetNumber='" + getTargetAssetNumber() + "'" +
            ", workInProgressRegistrations=" + getWorkInProgressRegistrations() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
