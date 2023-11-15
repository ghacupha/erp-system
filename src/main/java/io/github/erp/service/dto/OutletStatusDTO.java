package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
