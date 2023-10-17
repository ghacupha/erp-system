package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DepreciationBatchSequence.
 */
@Entity
@Table(name = "depreciation_batch_sequence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "depreciationbatchsequence")
public class DepreciationBatchSequence implements Serializable {

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

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "depreciation_batch_status")
    private DepreciationBatchStatusType depreciationBatchStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "createdBy", "depreciationPeriod" }, allowSetters = true)
    private DepreciationJob depreciationJob;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepreciationBatchSequence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartIndex() {
        return this.startIndex;
    }

    public DepreciationBatchSequence startIndex(Integer startIndex) {
        this.setStartIndex(startIndex);
        return this;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return this.endIndex;
    }

    public DepreciationBatchSequence endIndex(Integer endIndex) {
        this.setEndIndex(endIndex);
        return this;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public DepreciationBatchSequence createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DepreciationBatchStatusType getDepreciationBatchStatus() {
        return this.depreciationBatchStatus;
    }

    public DepreciationBatchSequence depreciationBatchStatus(DepreciationBatchStatusType depreciationBatchStatus) {
        this.setDepreciationBatchStatus(depreciationBatchStatus);
        return this;
    }

    public void setDepreciationBatchStatus(DepreciationBatchStatusType depreciationBatchStatus) {
        this.depreciationBatchStatus = depreciationBatchStatus;
    }

    public DepreciationJob getDepreciationJob() {
        return this.depreciationJob;
    }

    public void setDepreciationJob(DepreciationJob depreciationJob) {
        this.depreciationJob = depreciationJob;
    }

    public DepreciationBatchSequence depreciationJob(DepreciationJob depreciationJob) {
        this.setDepreciationJob(depreciationJob);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationBatchSequence)) {
            return false;
        }
        return id != null && id.equals(((DepreciationBatchSequence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationBatchSequence{" +
            "id=" + getId() +
            ", startIndex=" + getStartIndex() +
            ", endIndex=" + getEndIndex() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", depreciationBatchStatus='" + getDepreciationBatchStatus() + "'" +
            "}";
    }
}
