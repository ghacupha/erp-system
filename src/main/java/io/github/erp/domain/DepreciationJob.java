package io.github.erp.domain;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DepreciationJob.
 */
@Entity
@Table(name = "depreciation_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depreciationjob")
public class DepreciationJob implements Serializable {

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

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "organization", "department", "securityClearance", "systemIdentity", "userProperties", "dealerIdentity", "placeholders" },
        allowSetters = true
    )
    private ApplicationUser createdBy;

    @JsonIgnoreProperties(value = { "previousPeriod", "createdBy", "fiscalYear", "fiscalMonth", "fiscalQuarter" }, allowSetters = true)
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
            "}";
    }
}
