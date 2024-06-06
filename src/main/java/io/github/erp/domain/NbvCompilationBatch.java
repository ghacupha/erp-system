package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.CompilationBatchStatusTypes;
import java.io.Serializable;
import java.time.Duration;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NbvCompilationBatch.
 */
@Entity
@Table(name = "nbv_compilation_batch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "nbvcompilationbatch")
public class NbvCompilationBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "start_index")
    private Integer startIndex;

    @Column(name = "end_index")
    private Integer endIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "compilation_batch_status")
    private CompilationBatchStatusTypes compilationBatchStatus;

    @Column(name = "compilation_batch_identifier")
    private UUID compilationBatchIdentifier;

    @Column(name = "compilation_jobidentifier")
    private UUID compilationJobidentifier;

    @Column(name = "depreciation_period_identifier")
    private UUID depreciationPeriodIdentifier;

    @Column(name = "fiscal_month_identifier")
    private UUID fiscalMonthIdentifier;

    @Column(name = "batch_size")
    private Integer batchSize;

    @Column(name = "processed_items")
    private Integer processedItems;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Column(name = "is_last_batch")
    private Boolean isLastBatch;

    @Column(name = "processing_time")
    private Duration processingTime;

    @Column(name = "total_items")
    private Integer totalItems;

    @ManyToOne
    @JsonIgnoreProperties(value = { "activePeriod", "initiatedBy" }, allowSetters = true)
    private NbvCompilationJob nbvCompilationJob;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NbvCompilationBatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartIndex() {
        return this.startIndex;
    }

    public NbvCompilationBatch startIndex(Integer startIndex) {
        this.setStartIndex(startIndex);
        return this;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return this.endIndex;
    }

    public NbvCompilationBatch endIndex(Integer endIndex) {
        this.setEndIndex(endIndex);
        return this;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public CompilationBatchStatusTypes getCompilationBatchStatus() {
        return this.compilationBatchStatus;
    }

    public NbvCompilationBatch compilationBatchStatus(CompilationBatchStatusTypes compilationBatchStatus) {
        this.setCompilationBatchStatus(compilationBatchStatus);
        return this;
    }

    public void setCompilationBatchStatus(CompilationBatchStatusTypes compilationBatchStatus) {
        this.compilationBatchStatus = compilationBatchStatus;
    }

    public UUID getCompilationBatchIdentifier() {
        return this.compilationBatchIdentifier;
    }

    public NbvCompilationBatch compilationBatchIdentifier(UUID compilationBatchIdentifier) {
        this.setCompilationBatchIdentifier(compilationBatchIdentifier);
        return this;
    }

    public void setCompilationBatchIdentifier(UUID compilationBatchIdentifier) {
        this.compilationBatchIdentifier = compilationBatchIdentifier;
    }

    public UUID getCompilationJobidentifier() {
        return this.compilationJobidentifier;
    }

    public NbvCompilationBatch compilationJobidentifier(UUID compilationJobidentifier) {
        this.setCompilationJobidentifier(compilationJobidentifier);
        return this;
    }

    public void setCompilationJobidentifier(UUID compilationJobidentifier) {
        this.compilationJobidentifier = compilationJobidentifier;
    }

    public UUID getDepreciationPeriodIdentifier() {
        return this.depreciationPeriodIdentifier;
    }

    public NbvCompilationBatch depreciationPeriodIdentifier(UUID depreciationPeriodIdentifier) {
        this.setDepreciationPeriodIdentifier(depreciationPeriodIdentifier);
        return this;
    }

    public void setDepreciationPeriodIdentifier(UUID depreciationPeriodIdentifier) {
        this.depreciationPeriodIdentifier = depreciationPeriodIdentifier;
    }

    public UUID getFiscalMonthIdentifier() {
        return this.fiscalMonthIdentifier;
    }

    public NbvCompilationBatch fiscalMonthIdentifier(UUID fiscalMonthIdentifier) {
        this.setFiscalMonthIdentifier(fiscalMonthIdentifier);
        return this;
    }

    public void setFiscalMonthIdentifier(UUID fiscalMonthIdentifier) {
        this.fiscalMonthIdentifier = fiscalMonthIdentifier;
    }

    public Integer getBatchSize() {
        return this.batchSize;
    }

    public NbvCompilationBatch batchSize(Integer batchSize) {
        this.setBatchSize(batchSize);
        return this;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getProcessedItems() {
        return this.processedItems;
    }

    public NbvCompilationBatch processedItems(Integer processedItems) {
        this.setProcessedItems(processedItems);
        return this;
    }

    public void setProcessedItems(Integer processedItems) {
        this.processedItems = processedItems;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public NbvCompilationBatch sequenceNumber(Integer sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Boolean getIsLastBatch() {
        return this.isLastBatch;
    }

    public NbvCompilationBatch isLastBatch(Boolean isLastBatch) {
        this.setIsLastBatch(isLastBatch);
        return this;
    }

    public void setIsLastBatch(Boolean isLastBatch) {
        this.isLastBatch = isLastBatch;
    }

    public Duration getProcessingTime() {
        return this.processingTime;
    }

    public NbvCompilationBatch processingTime(Duration processingTime) {
        this.setProcessingTime(processingTime);
        return this;
    }

    public void setProcessingTime(Duration processingTime) {
        this.processingTime = processingTime;
    }

    public Integer getTotalItems() {
        return this.totalItems;
    }

    public NbvCompilationBatch totalItems(Integer totalItems) {
        this.setTotalItems(totalItems);
        return this;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public NbvCompilationJob getNbvCompilationJob() {
        return this.nbvCompilationJob;
    }

    public void setNbvCompilationJob(NbvCompilationJob nbvCompilationJob) {
        this.nbvCompilationJob = nbvCompilationJob;
    }

    public NbvCompilationBatch nbvCompilationJob(NbvCompilationJob nbvCompilationJob) {
        this.setNbvCompilationJob(nbvCompilationJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NbvCompilationBatch)) {
            return false;
        }
        return id != null && id.equals(((NbvCompilationBatch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NbvCompilationBatch{" +
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
            "}";
    }
}
