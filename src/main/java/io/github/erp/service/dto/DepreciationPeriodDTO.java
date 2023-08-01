package io.github.erp.service.dto;

import io.github.erp.domain.enumeration.DepreciationPeriodStatusTypes;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DepreciationPeriod} entity.
 */
public class DepreciationPeriodDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private DepreciationPeriodStatusTypes depreciationPeriodStatus;

    private DepreciationPeriodDTO previousPeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DepreciationPeriodStatusTypes getDepreciationPeriodStatus() {
        return depreciationPeriodStatus;
    }

    public void setDepreciationPeriodStatus(DepreciationPeriodStatusTypes depreciationPeriodStatus) {
        this.depreciationPeriodStatus = depreciationPeriodStatus;
    }

    public DepreciationPeriodDTO getPreviousPeriod() {
        return previousPeriod;
    }

    public void setPreviousPeriod(DepreciationPeriodDTO previousPeriod) {
        this.previousPeriod = previousPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepreciationPeriodDTO)) {
            return false;
        }

        DepreciationPeriodDTO depreciationPeriodDTO = (DepreciationPeriodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depreciationPeriodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepreciationPeriodDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", depreciationPeriodStatus='" + getDepreciationPeriodStatus() + "'" +
            ", previousPeriod=" + getPreviousPeriod() +
            "}";
    }
}
