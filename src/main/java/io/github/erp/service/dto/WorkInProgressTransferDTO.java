package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.2-SNAPSHOT
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

/**
 * A DTO for the {@link io.github.erp.domain.WorkInProgressTransfer} entity.
 */
public class WorkInProgressTransferDTO implements Serializable {

    private Long id;

    private String description;

    private String targetAssetNumber;

    private Set<WorkInProgressRegistrationDTO> workInProgressRegistrations = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetAssetNumber() {
        return targetAssetNumber;
    }

    public void setTargetAssetNumber(String targetAssetNumber) {
        this.targetAssetNumber = targetAssetNumber;
    }

    public Set<WorkInProgressRegistrationDTO> getWorkInProgressRegistrations() {
        return workInProgressRegistrations;
    }

    public void setWorkInProgressRegistrations(Set<WorkInProgressRegistrationDTO> workInProgressRegistrations) {
        this.workInProgressRegistrations = workInProgressRegistrations;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressTransferDTO)) {
            return false;
        }

        WorkInProgressTransferDTO workInProgressTransferDTO = (WorkInProgressTransferDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workInProgressTransferDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressTransferDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", targetAssetNumber='" + getTargetAssetNumber() + "'" +
            ", workInProgressRegistrations=" + getWorkInProgressRegistrations() +
            ", placeholders=" + getPlaceholders() +
            ", businessDocuments=" + getBusinessDocuments() +
            "}";
    }
}
