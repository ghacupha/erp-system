package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.enumeration.compilationProcessStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TACompilationRequest.
 */
@Entity
@Table(name = "tacompilation_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tacompilationrequest")
public class TACompilationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "requisition_id", nullable = false, unique = true)
    private UUID requisitionId;

    @Column(name = "time_of_request")
    private ZonedDateTime timeOfRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "compilation_process_status")
    private compilationProcessStatusTypes compilationProcessStatus;

    @Column(name = "number_of_enumerated_items")
    private Integer numberOfEnumeratedItems;

    @NotNull
    @Column(name = "batch_job_identifier", nullable = false, unique = true)
    private UUID batchJobIdentifier;

    @Column(name = "compilation_time")
    private ZonedDateTime compilationTime;

    @Column(name = "invalidated")
    private Boolean invalidated;

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

    public TACompilationRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequisitionId() {
        return this.requisitionId;
    }

    public TACompilationRequest requisitionId(UUID requisitionId) {
        this.setRequisitionId(requisitionId);
        return this;
    }

    public void setRequisitionId(UUID requisitionId) {
        this.requisitionId = requisitionId;
    }

    public ZonedDateTime getTimeOfRequest() {
        return this.timeOfRequest;
    }

    public TACompilationRequest timeOfRequest(ZonedDateTime timeOfRequest) {
        this.setTimeOfRequest(timeOfRequest);
        return this;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public compilationProcessStatusTypes getCompilationProcessStatus() {
        return this.compilationProcessStatus;
    }

    public TACompilationRequest compilationProcessStatus(compilationProcessStatusTypes compilationProcessStatus) {
        this.setCompilationProcessStatus(compilationProcessStatus);
        return this;
    }

    public void setCompilationProcessStatus(compilationProcessStatusTypes compilationProcessStatus) {
        this.compilationProcessStatus = compilationProcessStatus;
    }

    public Integer getNumberOfEnumeratedItems() {
        return this.numberOfEnumeratedItems;
    }

    public TACompilationRequest numberOfEnumeratedItems(Integer numberOfEnumeratedItems) {
        this.setNumberOfEnumeratedItems(numberOfEnumeratedItems);
        return this;
    }

    public void setNumberOfEnumeratedItems(Integer numberOfEnumeratedItems) {
        this.numberOfEnumeratedItems = numberOfEnumeratedItems;
    }

    public UUID getBatchJobIdentifier() {
        return this.batchJobIdentifier;
    }

    public TACompilationRequest batchJobIdentifier(UUID batchJobIdentifier) {
        this.setBatchJobIdentifier(batchJobIdentifier);
        return this;
    }

    public void setBatchJobIdentifier(UUID batchJobIdentifier) {
        this.batchJobIdentifier = batchJobIdentifier;
    }

    public ZonedDateTime getCompilationTime() {
        return this.compilationTime;
    }

    public TACompilationRequest compilationTime(ZonedDateTime compilationTime) {
        this.setCompilationTime(compilationTime);
        return this;
    }

    public void setCompilationTime(ZonedDateTime compilationTime) {
        this.compilationTime = compilationTime;
    }

    public Boolean getInvalidated() {
        return this.invalidated;
    }

    public TACompilationRequest invalidated(Boolean invalidated) {
        this.setInvalidated(invalidated);
        return this;
    }

    public void setInvalidated(Boolean invalidated) {
        this.invalidated = invalidated;
    }

    public ApplicationUser getInitiatedBy() {
        return this.initiatedBy;
    }

    public void setInitiatedBy(ApplicationUser applicationUser) {
        this.initiatedBy = applicationUser;
    }

    public TACompilationRequest initiatedBy(ApplicationUser applicationUser) {
        this.setInitiatedBy(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TACompilationRequest)) {
            return false;
        }
        return id != null && id.equals(((TACompilationRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TACompilationRequest{" +
            "id=" + getId() +
            ", requisitionId='" + getRequisitionId() + "'" +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", compilationProcessStatus='" + getCompilationProcessStatus() + "'" +
            ", numberOfEnumeratedItems=" + getNumberOfEnumeratedItems() +
            ", batchJobIdentifier='" + getBatchJobIdentifier() + "'" +
            ", compilationTime='" + getCompilationTime() + "'" +
            ", invalidated='" + getInvalidated() + "'" +
            "}";
    }
}
