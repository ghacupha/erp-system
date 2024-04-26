package io.github.erp.service.dto;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
