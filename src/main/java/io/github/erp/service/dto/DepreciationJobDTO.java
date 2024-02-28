package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
