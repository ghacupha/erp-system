package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 14 (Caleb Series) Server ver 1.1.4-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

    @NotNull
    private String applicationIdentity;

    private DealerDTO organization;

    private DealerDTO department;

    private SecurityClearanceDTO securityClearance;

    private UserDTO systemIdentity;

    private Set<UniversallyUniqueMappingDTO> userProperties = new HashSet<>();

    private DealerDTO dealerIdentity;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

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

    public String getApplicationIdentity() {
        return applicationIdentity;
    }

    public void setApplicationIdentity(String applicationIdentity) {
        this.applicationIdentity = applicationIdentity;
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

    public DealerDTO getDealerIdentity() {
        return dealerIdentity;
    }

    public void setDealerIdentity(DealerDTO dealerIdentity) {
        this.dealerIdentity = dealerIdentity;
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
            ", applicationIdentity='" + getApplicationIdentity() + "'" +
            ", organization=" + getOrganization() +
            ", department=" + getDepartment() +
            ", securityClearance=" + getSecurityClearance() +
            ", systemIdentity=" + getSystemIdentity() +
            ", userProperties=" + getUserProperties() +
            ", dealerIdentity=" + getDealerIdentity() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
