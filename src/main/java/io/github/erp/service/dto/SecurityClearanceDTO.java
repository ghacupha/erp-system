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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SecurityClearance} entity.
 */
public class SecurityClearanceDTO implements Serializable {

    private Long id;

    @NotNull
    private String clearanceLevel;

    private Set<SecurityClearanceDTO> grantedClearances = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> systemParameters = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClearanceLevel() {
        return clearanceLevel;
    }

    public void setClearanceLevel(String clearanceLevel) {
        this.clearanceLevel = clearanceLevel;
    }

    public Set<SecurityClearanceDTO> getGrantedClearances() {
        return grantedClearances;
    }

    public void setGrantedClearances(Set<SecurityClearanceDTO> grantedClearances) {
        this.grantedClearances = grantedClearances;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getSystemParameters() {
        return systemParameters;
    }

    public void setSystemParameters(Set<UniversallyUniqueMappingDTO> systemParameters) {
        this.systemParameters = systemParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityClearanceDTO)) {
            return false;
        }

        SecurityClearanceDTO securityClearanceDTO = (SecurityClearanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, securityClearanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityClearanceDTO{" +
            "id=" + getId() +
            ", clearanceLevel='" + getClearanceLevel() + "'" +
            ", grantedClearances=" + getGrantedClearances() +
            ", placeholders=" + getPlaceholders() +
            ", systemParameters=" + getSystemParameters() +
            "}";
    }
}
