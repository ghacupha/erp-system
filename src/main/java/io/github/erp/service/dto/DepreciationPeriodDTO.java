package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

    @NotNull
    private String periodCode;

    private Boolean processLocked;

    private DepreciationPeriodDTO previousPeriod;

    private ApplicationUserDTO createdBy;

    private FiscalYearDTO fiscalYear;

    private FiscalMonthDTO fiscalMonth;

    private FiscalQuarterDTO fiscalQuarter;

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

    public String getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(String periodCode) {
        this.periodCode = periodCode;
    }

    public Boolean getProcessLocked() {
        return processLocked;
    }

    public void setProcessLocked(Boolean processLocked) {
        this.processLocked = processLocked;
    }

    public DepreciationPeriodDTO getPreviousPeriod() {
        return previousPeriod;
    }

    public void setPreviousPeriod(DepreciationPeriodDTO previousPeriod) {
        this.previousPeriod = previousPeriod;
    }

    public ApplicationUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public FiscalYearDTO getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYearDTO fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public FiscalMonthDTO getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(FiscalMonthDTO fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public FiscalQuarterDTO getFiscalQuarter() {
        return fiscalQuarter;
    }

    public void setFiscalQuarter(FiscalQuarterDTO fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
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
            ", periodCode='" + getPeriodCode() + "'" +
            ", processLocked='" + getProcessLocked() + "'" +
            ", previousPeriod=" + getPreviousPeriod() +
            ", createdBy=" + getCreatedBy() +
            ", fiscalYear=" + getFiscalYear() +
            ", fiscalMonth=" + getFiscalMonth() +
            ", fiscalQuarter=" + getFiscalQuarter() +
            "}";
    }
}
