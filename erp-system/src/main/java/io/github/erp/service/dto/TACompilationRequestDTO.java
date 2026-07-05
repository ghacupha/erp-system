package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.enumeration.compilationProcessStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.TACompilationRequest} entity.
 */
public class TACompilationRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID requisitionId;

    private ZonedDateTime timeOfRequest;

    private compilationProcessStatusTypes compilationProcessStatus;

    private Integer numberOfEnumeratedItems;

    @NotNull
    private UUID batchJobIdentifier;

    private ZonedDateTime compilationTime;

    private Boolean invalidated;

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

    public compilationProcessStatusTypes getCompilationProcessStatus() {
        return compilationProcessStatus;
    }

    public void setCompilationProcessStatus(compilationProcessStatusTypes compilationProcessStatus) {
        this.compilationProcessStatus = compilationProcessStatus;
    }

    public Integer getNumberOfEnumeratedItems() {
        return numberOfEnumeratedItems;
    }

    public void setNumberOfEnumeratedItems(Integer numberOfEnumeratedItems) {
        this.numberOfEnumeratedItems = numberOfEnumeratedItems;
    }

    public UUID getBatchJobIdentifier() {
        return batchJobIdentifier;
    }

    public void setBatchJobIdentifier(UUID batchJobIdentifier) {
        this.batchJobIdentifier = batchJobIdentifier;
    }

    public ZonedDateTime getCompilationTime() {
        return compilationTime;
    }

    public void setCompilationTime(ZonedDateTime compilationTime) {
        this.compilationTime = compilationTime;
    }

    public Boolean getInvalidated() {
        return invalidated;
    }

    public void setInvalidated(Boolean invalidated) {
        this.invalidated = invalidated;
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
        if (!(o instanceof TACompilationRequestDTO)) {
            return false;
        }

        TACompilationRequestDTO tACompilationRequestDTO = (TACompilationRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tACompilationRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TACompilationRequestDTO{" +
            "id=" + getId() +
            ", requisitionId='" + getRequisitionId() + "'" +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", compilationProcessStatus='" + getCompilationProcessStatus() + "'" +
            ", numberOfEnumeratedItems=" + getNumberOfEnumeratedItems() +
            ", batchJobIdentifier='" + getBatchJobIdentifier() + "'" +
            ", compilationTime='" + getCompilationTime() + "'" +
            ", invalidated='" + getInvalidated() + "'" +
            ", initiatedBy=" + getInitiatedBy() +
            "}";
    }
}
