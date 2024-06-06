package io.github.erp.service.dto;

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
