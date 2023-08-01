package io.github.erp.service.dto;

import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationBatchSequence} entity.
 */
public class DepreciationBatchSequenceDTO implements Serializable {

    private Long id;

    private Integer startIndex;

    private Integer endIndex;

    private ZonedDateTime createdAt;

    private DepreciationBatchStatusType depreciationBatchStatus;

    private DepreciationJobDTO depreciationJob;

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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DepreciationBatchStatusType getDepreciationBatchStatus() {
        return depreciationBatchStatus;
    }

    public void setDepreciationBatchStatus(DepreciationBatchStatusType depreciationBatchStatus) {
        this.depreciationBatchStatus = depreciationBatchStatus;
    }

    public DepreciationJobDTO getDepreciationJob() {
        return depreciationJob;
    }

    public void setDepreciationJob(DepreciationJobDTO depreciationJob) {
        this.depreciationJob = depreciationJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationBatchSequenceDTO)) {
            return false;
        }

        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = (DepreciationBatchSequenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationBatchSequenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationBatchSequenceDTO{" +
            "id=" + getId() +
            ", startIndex=" + getStartIndex() +
            ", endIndex=" + getEndIndex() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", depreciationBatchStatus='" + getDepreciationBatchStatus() + "'" +
            ", depreciationJob=" + getDepreciationJob() +
            "}";
    }
}
