package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.OutletType} entity.
 */
public class OutletTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String outletTypeCode;

    @NotNull
    private String outletType;

    private String outletTypeDetails;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutletTypeCode() {
        return outletTypeCode;
    }

    public void setOutletTypeCode(String outletTypeCode) {
        this.outletTypeCode = outletTypeCode;
    }

    public String getOutletType() {
        return outletType;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }

    public String getOutletTypeDetails() {
        return outletTypeDetails;
    }

    public void setOutletTypeDetails(String outletTypeDetails) {
        this.outletTypeDetails = outletTypeDetails;
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
        if (!(o instanceof OutletTypeDTO)) {
            return false;
        }

        OutletTypeDTO outletTypeDTO = (OutletTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outletTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutletTypeDTO{" +
            "id=" + getId() +
            ", outletTypeCode='" + getOutletTypeCode() + "'" +
            ", outletType='" + getOutletType() + "'" +
            ", outletTypeDetails='" + getOutletTypeDetails() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
