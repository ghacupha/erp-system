package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
