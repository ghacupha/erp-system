package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ApplicationUser} entity.
 */
public class ApplicationUserDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID designation;

    private DealerDTO organization;

    private DealerDTO department;

    private SecurityClearanceDTO securityClearance;

    private UserDTO systemIdentity;

    private Set<UniversallyUniqueMappingDTO> userProperties = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getDesignation() {
        return designation;
    }

    public void setDesignation(UUID designation) {
        this.designation = designation;
    }

    public DealerDTO getOrganization() {
        return organization;
    }

    public void setOrganization(DealerDTO organization) {
        this.organization = organization;
    }

    public DealerDTO getDepartment() {
        return department;
    }

    public void setDepartment(DealerDTO department) {
        this.department = department;
    }

    public SecurityClearanceDTO getSecurityClearance() {
        return securityClearance;
    }

    public void setSecurityClearance(SecurityClearanceDTO securityClearance) {
        this.securityClearance = securityClearance;
    }

    public UserDTO getSystemIdentity() {
        return systemIdentity;
    }

    public void setSystemIdentity(UserDTO systemIdentity) {
        this.systemIdentity = systemIdentity;
    }

    public Set<UniversallyUniqueMappingDTO> getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(Set<UniversallyUniqueMappingDTO> userProperties) {
        this.userProperties = userProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUserDTO)) {
            return false;
        }

        ApplicationUserDTO applicationUserDTO = (ApplicationUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserDTO{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", organization=" + getOrganization() +
            ", department=" + getDepartment() +
            ", securityClearance=" + getSecurityClearance() +
            ", systemIdentity=" + getSystemIdentity() +
            ", userProperties=" + getUserProperties() +
            "}";
    }
}
