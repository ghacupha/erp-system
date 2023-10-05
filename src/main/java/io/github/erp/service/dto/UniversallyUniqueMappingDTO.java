package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
 * A DTO for the {@link io.github.erp.domain.UniversallyUniqueMapping} entity.
 */
public class UniversallyUniqueMappingDTO implements Serializable {

    private Long id;

    @NotNull
    private String universalKey;

    private String mappedValue;

    private UniversallyUniqueMappingDTO parentMapping;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversalKey() {
        return universalKey;
    }

    public void setUniversalKey(String universalKey) {
        this.universalKey = universalKey;
    }

    public String getMappedValue() {
        return mappedValue;
    }

    public void setMappedValue(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public UniversallyUniqueMappingDTO getParentMapping() {
        return parentMapping;
    }

    public void setParentMapping(UniversallyUniqueMappingDTO parentMapping) {
        this.parentMapping = parentMapping;
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
        if (!(o instanceof UniversallyUniqueMappingDTO)) {
            return false;
        }

        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = (UniversallyUniqueMappingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, universallyUniqueMappingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniversallyUniqueMappingDTO{" +
            "id=" + getId() +
            ", universalKey='" + getUniversalKey() + "'" +
            ", mappedValue='" + getMappedValue() + "'" +
            ", parentMapping=" + getParentMapping() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
