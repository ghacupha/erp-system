package io.github.erp.service.dto;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
