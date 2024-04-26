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
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationJob} entity.
 */
public class DepreciationJobDTO implements Serializable {

    private Long id;

    private ZonedDateTime timeOfCommencement;

    private DepreciationJobStatusType depreciationJobStatus;

    @NotNull
    private String description;

    private Integer numberOfBatches;

    private Integer processedBatches;

    private Integer lastBatchSize;

    private Integer processedItems;

    private Duration processingTime;

    private Integer totalItems;

    private ApplicationUserDTO createdBy;

    private DepreciationPeriodDTO depreciationPeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeOfCommencement() {
        return timeOfCommencement;
    }

    public void setTimeOfCommencement(ZonedDateTime timeOfCommencement) {
        this.timeOfCommencement = timeOfCommencement;
    }

    public DepreciationJobStatusType getDepreciationJobStatus() {
        return depreciationJobStatus;
    }

    public void setDepreciationJobStatus(DepreciationJobStatusType depreciationJobStatus) {
        this.depreciationJobStatus = depreciationJobStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfBatches() {
        return numberOfBatches;
    }

    public void setNumberOfBatches(Integer numberOfBatches) {
        this.numberOfBatches = numberOfBatches;
    }

    public Integer getProcessedBatches() {
        return processedBatches;
    }

    public void setProcessedBatches(Integer processedBatches) {
        this.processedBatches = processedBatches;
    }

    public Integer getLastBatchSize() {
        return lastBatchSize;
    }

    public void setLastBatchSize(Integer lastBatchSize) {
        this.lastBatchSize = lastBatchSize;
    }

    public Integer getProcessedItems() {
        return processedItems;
    }

    public void setProcessedItems(Integer processedItems) {
        this.processedItems = processedItems;
    }

    public Duration getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Duration processingTime) {
        this.processingTime = processingTime;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public ApplicationUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public DepreciationPeriodDTO getDepreciationPeriod() {
        return depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriodDTO depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationJobDTO)) {
            return false;
        }

        DepreciationJobDTO depreciationJobDTO = (DepreciationJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationJobDTO{" +
            "id=" + getId() +
            ", timeOfCommencement='" + getTimeOfCommencement() + "'" +
            ", depreciationJobStatus='" + getDepreciationJobStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", numberOfBatches=" + getNumberOfBatches() +
            ", processedBatches=" + getProcessedBatches() +
            ", lastBatchSize=" + getLastBatchSize() +
            ", processedItems=" + getProcessedItems() +
            ", processingTime='" + getProcessingTime() + "'" +
            ", totalItems=" + getTotalItems() +
            ", createdBy=" + getCreatedBy() +
            ", depreciationPeriod=" + getDepreciationPeriod() +
            "}";
    }
}
