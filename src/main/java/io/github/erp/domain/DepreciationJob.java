package io.github.erp.domain;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DepreciationJob.
 */
@Entity
@Table(name = "depreciation_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depreciationjob")
public class DepreciationJob extends AbstractIdentifiableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "time_of_commencement")
    private ZonedDateTime timeOfCommencement;

    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_job_status")
    private DepreciationJobStatusType depreciationJobStatus;

    @NotNull
    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "number_of_batches")
    private Integer numberOfBatches;

    @Column(name = "processed_batches")
    private Integer processedBatches;

    @Column(name = "last_batch_size")
    private Integer lastBatchSize;

    @Column(name = "processed_items")
    private Integer processedItems;

    @Column(name = "processing_time")
    private Duration processingTime;

    @Column(name = "total_items")
    private Integer totalItems;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser createdBy;

    @JsonIgnoreProperties(value = { "previousPeriod", "fiscalMonth" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private DepreciationPeriod depreciationPeriod;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepreciationJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeOfCommencement() {
        return this.timeOfCommencement;
    }

    public DepreciationJob timeOfCommencement(ZonedDateTime timeOfCommencement) {
        this.setTimeOfCommencement(timeOfCommencement);
        return this;
    }

    public void setTimeOfCommencement(ZonedDateTime timeOfCommencement) {
        this.timeOfCommencement = timeOfCommencement;
    }

    public DepreciationJobStatusType getDepreciationJobStatus() {
        return this.depreciationJobStatus;
    }

    public DepreciationJob depreciationJobStatus(DepreciationJobStatusType depreciationJobStatus) {
        this.setDepreciationJobStatus(depreciationJobStatus);
        return this;
    }

    public void setDepreciationJobStatus(DepreciationJobStatusType depreciationJobStatus) {
        this.depreciationJobStatus = depreciationJobStatus;
    }

    public String getDescription() {
        return this.description;
    }

    public DepreciationJob description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfBatches() {
        return this.numberOfBatches;
    }

    public DepreciationJob numberOfBatches(Integer numberOfBatches) {
        this.setNumberOfBatches(numberOfBatches);
        return this;
    }

    public void setNumberOfBatches(Integer numberOfBatches) {
        this.numberOfBatches = numberOfBatches;
    }

    public Integer getProcessedBatches() {
        return this.processedBatches;
    }

    public DepreciationJob processedBatches(Integer processedBatches) {
        this.setProcessedBatches(processedBatches);
        return this;
    }

    public void setProcessedBatches(Integer processedBatches) {
        this.processedBatches = processedBatches;
    }

    public Integer getLastBatchSize() {
        return this.lastBatchSize;
    }

    public DepreciationJob lastBatchSize(Integer lastBatchSize) {
        this.setLastBatchSize(lastBatchSize);
        return this;
    }

    public void setLastBatchSize(Integer lastBatchSize) {
        this.lastBatchSize = lastBatchSize;
    }

    public Integer getProcessedItems() {
        return this.processedItems;
    }

    public DepreciationJob processedItems(Integer processedItems) {
        this.setProcessedItems(processedItems);
        return this;
    }

    public void setProcessedItems(Integer processedItems) {
        this.processedItems = processedItems;
    }

    public Duration getProcessingTime() {
        return this.processingTime;
    }

    public DepreciationJob processingTime(Duration processingTime) {
        this.setProcessingTime(processingTime);
        return this;
    }

    public void setProcessingTime(Duration processingTime) {
        this.processingTime = processingTime;
    }

    public Integer getTotalItems() {
        return this.totalItems;
    }

    public DepreciationJob totalItems(Integer totalItems) {
        this.setTotalItems(totalItems);
        return this;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public ApplicationUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(ApplicationUser applicationUser) {
        this.createdBy = applicationUser;
    }

    public DepreciationJob createdBy(ApplicationUser applicationUser) {
        this.setCreatedBy(applicationUser);
        return this;
    }

    public DepreciationPeriod getDepreciationPeriod() {
        return this.depreciationPeriod;
    }

    public void setDepreciationPeriod(DepreciationPeriod depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public DepreciationJob depreciationPeriod(DepreciationPeriod depreciationPeriod) {
        this.setDepreciationPeriod(depreciationPeriod);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationJob)) {
            return false;
        }
        return id != null && id.equals(((DepreciationJob) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationJob{" +
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
            "}";
    }
}
