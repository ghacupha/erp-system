package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.enumeration.SystemContentTypeAvailability;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.SystemContentType} entity.
 */
public class SystemContentTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String contentTypeName;

    @NotNull
    private String contentTypeHeader;

    @Lob
    private String comments;

    @NotNull
    private SystemContentTypeAvailability availability;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> sysMaps = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentTypeName() {
        return contentTypeName;
    }

    public void setContentTypeName(String contentTypeName) {
        this.contentTypeName = contentTypeName;
    }

    public String getContentTypeHeader() {
        return contentTypeHeader;
    }

    public void setContentTypeHeader(String contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public SystemContentTypeAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(SystemContentTypeAvailability availability) {
        this.availability = availability;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getSysMaps() {
        return sysMaps;
    }

    public void setSysMaps(Set<UniversallyUniqueMappingDTO> sysMaps) {
        this.sysMaps = sysMaps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemContentTypeDTO)) {
            return false;
        }

        SystemContentTypeDTO systemContentTypeDTO = (SystemContentTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemContentTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemContentTypeDTO{" +
            "id=" + getId() +
            ", contentTypeName='" + getContentTypeName() + "'" +
            ", contentTypeHeader='" + getContentTypeHeader() + "'" +
            ", comments='" + getComments() + "'" +
            ", availability='" + getAvailability() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", sysMaps=" + getSysMaps() +
            "}";
    }
}
