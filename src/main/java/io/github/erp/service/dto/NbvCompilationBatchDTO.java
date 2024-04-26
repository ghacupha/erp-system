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
import io.github.erp.domain.enumeration.CompilationBatchStatusTypes;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link io.github.erp.domain.NbvCompilationBatch} entity.
 */
public class NbvCompilationBatchDTO implements Serializable {

    private Long id;

    private Integer startIndex;

    private Integer endIndex;

    private CompilationBatchStatusTypes compilationBatchStatus;

    private UUID compilationBatchIdentifier;

    private UUID compilationJobidentifier;

    private UUID depreciationPeriodIdentifier;

    private UUID fiscalMonthIdentifier;

    private Integer batchSize;

    private Integer processedItems;

    private Integer sequenceNumber;

    private Boolean isLastBatch;

    private Duration processingTime;

    private Integer totalItems;

    private NbvCompilationJobDTO nbvCompilationJob;

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

    public CompilationBatchStatusTypes getCompilationBatchStatus() {
        return compilationBatchStatus;
    }

    public void setCompilationBatchStatus(CompilationBatchStatusTypes compilationBatchStatus) {
        this.compilationBatchStatus = compilationBatchStatus;
    }

    public UUID getCompilationBatchIdentifier() {
        return compilationBatchIdentifier;
    }

    public void setCompilationBatchIdentifier(UUID compilationBatchIdentifier) {
        this.compilationBatchIdentifier = compilationBatchIdentifier;
    }

    public UUID getCompilationJobidentifier() {
        return compilationJobidentifier;
    }

    public void setCompilationJobidentifier(UUID compilationJobidentifier) {
        this.compilationJobidentifier = compilationJobidentifier;
    }

    public UUID getDepreciationPeriodIdentifier() {
        return depreciationPeriodIdentifier;
    }

    public void setDepreciationPeriodIdentifier(UUID depreciationPeriodIdentifier) {
        this.depreciationPeriodIdentifier = depreciationPeriodIdentifier;
    }

    public UUID getFiscalMonthIdentifier() {
        return fiscalMonthIdentifier;
    }

    public void setFiscalMonthIdentifier(UUID fiscalMonthIdentifier) {
        this.fiscalMonthIdentifier = fiscalMonthIdentifier;
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

    public NbvCompilationJobDTO getNbvCompilationJob() {
        return nbvCompilationJob;
    }

    public void setNbvCompilationJob(NbvCompilationJobDTO nbvCompilationJob) {
        this.nbvCompilationJob = nbvCompilationJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NbvCompilationBatchDTO)) {
            return false;
        }

        NbvCompilationBatchDTO nbvCompilationBatchDTO = (NbvCompilationBatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nbvCompilationBatchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NbvCompilationBatchDTO{" +
            "id=" + getId() +
            ", startIndex=" + getStartIndex() +
            ", endIndex=" + getEndIndex() +
            ", compilationBatchStatus='" + getCompilationBatchStatus() + "'" +
            ", compilationBatchIdentifier='" + getCompilationBatchIdentifier() + "'" +
            ", compilationJobidentifier='" + getCompilationJobidentifier() + "'" +
            ", depreciationPeriodIdentifier='" + getDepreciationPeriodIdentifier() + "'" +
            ", fiscalMonthIdentifier='" + getFiscalMonthIdentifier() + "'" +
            ", batchSize=" + getBatchSize() +
            ", processedItems=" + getProcessedItems() +
            ", sequenceNumber=" + getSequenceNumber() +
            ", isLastBatch='" + getIsLastBatch() + "'" +
            ", processingTime='" + getProcessingTime() + "'" +
            ", totalItems=" + getTotalItems() +
            ", nbvCompilationJob=" + getNbvCompilationJob() +
            "}";
    }
}
