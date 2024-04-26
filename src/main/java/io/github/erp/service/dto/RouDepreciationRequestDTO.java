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
