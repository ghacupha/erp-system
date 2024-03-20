package io.github.erp.domain;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.enumeration.NVBCompilationStatus;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NbvCompilationJob.
 */
@Entity
@Table(name = "nbv_compilation_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "nbvcompilationjob")
public class NbvCompilationJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "compilation_job_identifier", nullable = false, unique = true)
    private UUID compilationJobIdentifier;

    @Column(name = "compilation_job_time_of_request")
    private ZonedDateTime compilationJobTimeOfRequest;

    @Column(name = "entities_affected")
    private Integer entitiesAffected;

    @Enumerated(EnumType.STRING)
    @Column(name = "compilation_status")
    private NVBCompilationStatus compilationStatus;

    @NotNull
    @Column(name = "job_title", nullable = false, unique = true)
    private String jobTitle;

    @Column(name = "number_of_batches")
    private Integer numberOfBatches;

    @Column(name = "number_of_processed_batches")
    private Integer numberOfProcessedBatches;

    @Column(name = "processing_time")
    private Duration processingTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "previousPeriod", "fiscalMonth" }, allowSetters = true)
    private DepreciationPeriod activePeriod;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser initiatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NbvCompilationJob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCompilationJobIdentifier() {
        return this.compilationJobIdentifier;
    }

    public NbvCompilationJob compilationJobIdentifier(UUID compilationJobIdentifier) {
        this.setCompilationJobIdentifier(compilationJobIdentifier);
        return this;
    }

    public void setCompilationJobIdentifier(UUID compilationJobIdentifier) {
        this.compilationJobIdentifier = compilationJobIdentifier;
    }

    public ZonedDateTime getCompilationJobTimeOfRequest() {
        return this.compilationJobTimeOfRequest;
    }

    public NbvCompilationJob compilationJobTimeOfRequest(ZonedDateTime compilationJobTimeOfRequest) {
        this.setCompilationJobTimeOfRequest(compilationJobTimeOfRequest);
        return this;
    }

    public void setCompilationJobTimeOfRequest(ZonedDateTime compilationJobTimeOfRequest) {
        this.compilationJobTimeOfRequest = compilationJobTimeOfRequest;
    }

    public Integer getEntitiesAffected() {
        return this.entitiesAffected;
    }

    public NbvCompilationJob entitiesAffected(Integer entitiesAffected) {
        this.setEntitiesAffected(entitiesAffected);
        return this;
    }

    public void setEntitiesAffected(Integer entitiesAffected) {
        this.entitiesAffected = entitiesAffected;
    }

    public NVBCompilationStatus getCompilationStatus() {
        return this.compilationStatus;
    }

    public NbvCompilationJob compilationStatus(NVBCompilationStatus compilationStatus) {
        this.setCompilationStatus(compilationStatus);
        return this;
    }

    public void setCompilationStatus(NVBCompilationStatus compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public NbvCompilationJob jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getNumberOfBatches() {
        return this.numberOfBatches;
    }

    public NbvCompilationJob numberOfBatches(Integer numberOfBatches) {
        this.setNumberOfBatches(numberOfBatches);
        return this;
    }

    public void setNumberOfBatches(Integer numberOfBatches) {
        this.numberOfBatches = numberOfBatches;
    }

    public Integer getNumberOfProcessedBatches() {
        return this.numberOfProcessedBatches;
    }

    public NbvCompilationJob numberOfProcessedBatches(Integer numberOfProcessedBatches) {
        this.setNumberOfProcessedBatches(numberOfProcessedBatches);
        return this;
    }

    public void setNumberOfProcessedBatches(Integer numberOfProcessedBatches) {
        this.numberOfProcessedBatches = numberOfProcessedBatches;
    }

    public Duration getProcessingTime() {
        return this.processingTime;
    }

    public NbvCompilationJob processingTime(Duration processingTime) {
        this.setProcessingTime(processingTime);
        return this;
    }

    public void setProcessingTime(Duration processingTime) {
        this.processingTime = processingTime;
    }

    public DepreciationPeriod getActivePeriod() {
        return this.activePeriod;
    }

    public void setActivePeriod(DepreciationPeriod depreciationPeriod) {
        this.activePeriod = depreciationPeriod;
    }

    public NbvCompilationJob activePeriod(DepreciationPeriod depreciationPeriod) {
        this.setActivePeriod(depreciationPeriod);
        return this;
    }

    public ApplicationUser getInitiatedBy() {
        return this.initiatedBy;
    }

    public void setInitiatedBy(ApplicationUser applicationUser) {
        this.initiatedBy = applicationUser;
    }

    public NbvCompilationJob initiatedBy(ApplicationUser applicationUser) {
        this.setInitiatedBy(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NbvCompilationJob)) {
            return false;
        }
        return id != null && id.equals(((NbvCompilationJob) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NbvCompilationJob{" +
            "id=" + getId() +
            ", compilationJobIdentifier='" + getCompilationJobIdentifier() + "'" +
            ", compilationJobTimeOfRequest='" + getCompilationJobTimeOfRequest() + "'" +
            ", entitiesAffected=" + getEntitiesAffected() +
            ", compilationStatus='" + getCompilationStatus() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", numberOfBatches=" + getNumberOfBatches() +
            ", numberOfProcessedBatches=" + getNumberOfProcessedBatches() +
            ", processingTime='" + getProcessingTime() + "'" +
            "}";
    }
}
