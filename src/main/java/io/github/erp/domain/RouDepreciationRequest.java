package io.github.erp.domain;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.enumeration.depreciationProcessStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RouDepreciationRequest.
 */
@Entity
@Table(name = "rou_depreciation_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "roudepreciationrequest")
public class RouDepreciationRequest implements Serializable {

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
    @Column(name = "depreciation_process_status")
    private depreciationProcessStatusTypes depreciationProcessStatus;

    @Column(name = "number_of_enumerated_items")
    private Integer numberOfEnumeratedItems;

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

    public RouDepreciationRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequisitionId() {
        return this.requisitionId;
    }

    public RouDepreciationRequest requisitionId(UUID requisitionId) {
        this.setRequisitionId(requisitionId);
        return this;
    }

    public void setRequisitionId(UUID requisitionId) {
        this.requisitionId = requisitionId;
    }

    public ZonedDateTime getTimeOfRequest() {
        return this.timeOfRequest;
    }

    public RouDepreciationRequest timeOfRequest(ZonedDateTime timeOfRequest) {
        this.setTimeOfRequest(timeOfRequest);
        return this;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public depreciationProcessStatusTypes getDepreciationProcessStatus() {
        return this.depreciationProcessStatus;
    }

    public RouDepreciationRequest depreciationProcessStatus(depreciationProcessStatusTypes depreciationProcessStatus) {
        this.setDepreciationProcessStatus(depreciationProcessStatus);
        return this;
    }

    public void setDepreciationProcessStatus(depreciationProcessStatusTypes depreciationProcessStatus) {
        this.depreciationProcessStatus = depreciationProcessStatus;
    }

    public Integer getNumberOfEnumeratedItems() {
        return this.numberOfEnumeratedItems;
    }

    public RouDepreciationRequest numberOfEnumeratedItems(Integer numberOfEnumeratedItems) {
        this.setNumberOfEnumeratedItems(numberOfEnumeratedItems);
        return this;
    }

    public void setNumberOfEnumeratedItems(Integer numberOfEnumeratedItems) {
        this.numberOfEnumeratedItems = numberOfEnumeratedItems;
    }

    public ApplicationUser getInitiatedBy() {
        return this.initiatedBy;
    }

    public void setInitiatedBy(ApplicationUser applicationUser) {
        this.initiatedBy = applicationUser;
    }

    public RouDepreciationRequest initiatedBy(ApplicationUser applicationUser) {
        this.setInitiatedBy(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouDepreciationRequest)) {
            return false;
        }
        return id != null && id.equals(((RouDepreciationRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RouDepreciationRequest{" +
            "id=" + getId() +
            ", requisitionId='" + getRequisitionId() + "'" +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", depreciationProcessStatus='" + getDepreciationProcessStatus() + "'" +
            ", numberOfEnumeratedItems=" + getNumberOfEnumeratedItems() +
            "}";
    }
}
