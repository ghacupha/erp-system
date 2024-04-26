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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationBatchSequence} entity.
 */
public class DepreciationBatchSequenceDTO implements Serializable {

    private Long id;

    private Integer startIndex;

    private Integer endIndex;

    private DepreciationBatchStatusType depreciationBatchStatus;

    private UUID depreciationPeriodIdentifier;

    private UUID depreciationJobIdentifier;

    private UUID fiscalMonthIdentifier;

    private UUID fiscalQuarterIdentifier;

    private Integer batchSize;

    private Integer processedItems;

    private Integer sequenceNumber;

    private Boolean isLastBatch;

    private Duration processingTime;

    private Integer totalItems;

    private DepreciationJobDTO depreciationJob;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public DepreciationBatchStatusType getDepreciationBatchStatus() {
        return depreciationBatchStatus;
    }

    public void setDepreciationBatchStatus(DepreciationBatchStatusType depreciationBatchStatus) {
        this.depreciationBatchStatus = depreciationBatchStatus;
    }

    public UUID getDepreciationPeriodIdentifier() {
        return depreciationPeriodIdentifier;
    }

    public void setDepreciationPeriodIdentifier(UUID depreciationPeriodIdentifier) {
        this.depreciationPeriodIdentifier = depreciationPeriodIdentifier;
    }

    public UUID getDepreciationJobIdentifier() {
        return depreciationJobIdentifier;
    }

    public void setDepreciationJobIdentifier(UUID depreciationJobIdentifier) {
        this.depreciationJobIdentifier = depreciationJobIdentifier;
    }

    public UUID getFiscalMonthIdentifier() {
        return fiscalMonthIdentifier;
    }

    public void setFiscalMonthIdentifier(UUID fiscalMonthIdentifier) {
        this.fiscalMonthIdentifier = fiscalMonthIdentifier;
    }

    public UUID getFiscalQuarterIdentifier() {
        return fiscalQuarterIdentifier;
    }

    public void setFiscalQuarterIdentifier(UUID fiscalQuarterIdentifier) {
        this.fiscalQuarterIdentifier = fiscalQuarterIdentifier;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getProcessedItems() {
        return processedItems;
    }

    public void setProcessedItems(Integer processedItems) {
        this.processedItems = processedItems;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Boolean getIsLastBatch() {
        return isLastBatch;
    }

    public void setIsLastBatch(Boolean isLastBatch) {
        this.isLastBatch = isLastBatch;
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

    public DepreciationJobDTO getDepreciationJob() {
        return depreciationJob;
    }

    public void setDepreciationJob(DepreciationJobDTO depreciationJob) {
        this.depreciationJob = depreciationJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationBatchSequenceDTO)) {
            return false;
        }

        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = (DepreciationBatchSequenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationBatchSequenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationBatchSequenceDTO{" +
            "id=" + getId() +
            ", startIndex=" + getStartIndex() +
            ", endIndex=" + getEndIndex() +
            ", depreciationBatchStatus='" + getDepreciationBatchStatus() + "'" +
            ", depreciationPeriodIdentifier='" + getDepreciationPeriodIdentifier() + "'" +
            ", depreciationJobIdentifier='" + getDepreciationJobIdentifier() + "'" +
            ", fiscalMonthIdentifier='" + getFiscalMonthIdentifier() + "'" +
            ", fiscalQuarterIdentifier='" + getFiscalQuarterIdentifier() + "'" +
            ", batchSize=" + getBatchSize() +
            ", processedItems=" + getProcessedItems() +
            ", sequenceNumber=" + getSequenceNumber() +
            ", isLastBatch='" + getIsLastBatch() + "'" +
            ", processingTime='" + getProcessingTime() + "'" +
            ", totalItems=" + getTotalItems() +
            ", depreciationJob=" + getDepreciationJob() +
            "}";
    }
}
