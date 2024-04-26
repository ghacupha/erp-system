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
import io.github.erp.domain.enumeration.NVBCompilationStatus;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.NbvCompilationJob} entity.
 */
public class NbvCompilationJobDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID compilationJobIdentifier;

    private ZonedDateTime compilationJobTimeOfRequest;

    private Integer entitiesAffected;

    private NVBCompilationStatus compilationStatus;

    @NotNull
    private String jobTitle;

    private Integer numberOfBatches;

    private Integer numberOfProcessedBatches;

    private Duration processingTime;

    private DepreciationPeriodDTO activePeriod;

    private ApplicationUserDTO initiatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCompilationJobIdentifier() {
        return compilationJobIdentifier;
    }

    public void setCompilationJobIdentifier(UUID compilationJobIdentifier) {
        this.compilationJobIdentifier = compilationJobIdentifier;
    }

    public ZonedDateTime getCompilationJobTimeOfRequest() {
        return compilationJobTimeOfRequest;
    }

    public void setCompilationJobTimeOfRequest(ZonedDateTime compilationJobTimeOfRequest) {
        this.compilationJobTimeOfRequest = compilationJobTimeOfRequest;
    }

    public Integer getEntitiesAffected() {
        return entitiesAffected;
    }

    public void setEntitiesAffected(Integer entitiesAffected) {
        this.entitiesAffected = entitiesAffected;
    }

    public NVBCompilationStatus getCompilationStatus() {
        return compilationStatus;
    }

    public void setCompilationStatus(NVBCompilationStatus compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getNumberOfBatches() {
        return numberOfBatches;
    }

    public void setNumberOfBatches(Integer numberOfBatches) {
        this.numberOfBatches = numberOfBatches;
    }

    public Integer getNumberOfProcessedBatches() {
        return numberOfProcessedBatches;
    }

    public void setNumberOfProcessedBatches(Integer numberOfProcessedBatches) {
        this.numberOfProcessedBatches = numberOfProcessedBatches;
    }

    public Duration getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Duration processingTime) {
        this.processingTime = processingTime;
    }

    public DepreciationPeriodDTO getActivePeriod() {
        return activePeriod;
    }

    public void setActivePeriod(DepreciationPeriodDTO activePeriod) {
        this.activePeriod = activePeriod;
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
        if (!(o instanceof NbvCompilationJobDTO)) {
            return false;
        }

        NbvCompilationJobDTO nbvCompilationJobDTO = (NbvCompilationJobDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nbvCompilationJobDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NbvCompilationJobDTO{" +
            "id=" + getId() +
            ", compilationJobIdentifier='" + getCompilationJobIdentifier() + "'" +
            ", compilationJobTimeOfRequest='" + getCompilationJobTimeOfRequest() + "'" +
            ", entitiesAffected=" + getEntitiesAffected() +
            ", compilationStatus='" + getCompilationStatus() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", numberOfBatches=" + getNumberOfBatches() +
            ", numberOfProcessedBatches=" + getNumberOfProcessedBatches() +
            ", processingTime='" + getProcessingTime() + "'" +
            ", activePeriod=" + getActivePeriod() +
            ", initiatedBy=" + getInitiatedBy() +
            "}";
    }
}
