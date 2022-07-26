package io.github.erp.service.dto;

import io.github.erp.domain.enumeration.BranchStatusType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.OutletStatus} entity.
 */
public class OutletStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private String branchStatusTypeCode;

    @NotNull
    private BranchStatusType branchStatusType;

    private String branchStatusTypeDescription;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchStatusTypeCode() {
        return branchStatusTypeCode;
    }

    public void setBranchStatusTypeCode(String branchStatusTypeCode) {
        this.branchStatusTypeCode = branchStatusTypeCode;
    }

    public BranchStatusType getBranchStatusType() {
        return branchStatusType;
    }

    public void setBranchStatusType(BranchStatusType branchStatusType) {
        this.branchStatusType = branchStatusType;
    }

    public String getBranchStatusTypeDescription() {
        return branchStatusTypeDescription;
    }

    public void setBranchStatusTypeDescription(String branchStatusTypeDescription) {
        this.branchStatusTypeDescription = branchStatusTypeDescription;
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
        if (!(o instanceof OutletStatusDTO)) {
            return false;
        }

        OutletStatusDTO outletStatusDTO = (OutletStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outletStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutletStatusDTO{" +
            "id=" + getId() +
            ", branchStatusTypeCode='" + getBranchStatusTypeCode() + "'" +
            ", branchStatusType='" + getBranchStatusType() + "'" +
            ", branchStatusTypeDescription='" + getBranchStatusTypeDescription() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
