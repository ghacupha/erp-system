package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.depreciationProcessStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.RouDepreciationRequest} entity.
 */
public class RouDepreciationRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID requisitionId;

    private ZonedDateTime timeOfRequest;

    private depreciationProcessStatusTypes depreciationProcessStatus;

    private Integer numberOfEnumeratedItems;

    private ApplicationUserDTO initiatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(UUID requisitionId) {
        this.requisitionId = requisitionId;
    }

    public ZonedDateTime getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public depreciationProcessStatusTypes getDepreciationProcessStatus() {
        return depreciationProcessStatus;
    }

    public void setDepreciationProcessStatus(depreciationProcessStatusTypes depreciationProcessStatus) {
        this.depreciationProcessStatus = depreciationProcessStatus;
    }

    public Integer getNumberOfEnumeratedItems() {
        return numberOfEnumeratedItems;
    }

    public void setNumberOfEnumeratedItems(Integer numberOfEnumeratedItems) {
        this.numberOfEnumeratedItems = numberOfEnumeratedItems;
    }

    public ApplicationUserDTO getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(ApplicationUserDTO initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationRequestDTO)) {
            return false;
        }

        RouDepreciationRequestDTO rouDepreciationRequestDTO = (RouDepreciationRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rouDepreciationRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationRequestDTO{" +
            "id=" + getId() +
            ", requisitionId='" + getRequisitionId() + "'" +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", depreciationProcessStatus='" + getDepreciationProcessStatus() + "'" +
            ", numberOfEnumeratedItems=" + getNumberOfEnumeratedItems() +
            ", initiatedBy=" + getInitiatedBy() +
            "}";
    }
}
