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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.WeeklyCounterfeitHolding} entity.
 */
public class WeeklyCounterfeitHoldingDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private LocalDate dateConfiscated;

    @NotNull
    private String serialNumber;

    @NotNull
    private String depositorsNames;

    @NotNull
    private String tellersNames;

    @NotNull
    private LocalDate dateSubmittedToCBK;

    @Lob
    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public LocalDate getDateConfiscated() {
        return dateConfiscated;
    }

    public void setDateConfiscated(LocalDate dateConfiscated) {
        this.dateConfiscated = dateConfiscated;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDepositorsNames() {
        return depositorsNames;
    }

    public void setDepositorsNames(String depositorsNames) {
        this.depositorsNames = depositorsNames;
    }

    public String getTellersNames() {
        return tellersNames;
    }

    public void setTellersNames(String tellersNames) {
        this.tellersNames = tellersNames;
    }

    public LocalDate getDateSubmittedToCBK() {
        return dateSubmittedToCBK;
    }

    public void setDateSubmittedToCBK(LocalDate dateSubmittedToCBK) {
        this.dateSubmittedToCBK = dateSubmittedToCBK;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeeklyCounterfeitHoldingDTO)) {
            return false;
        }

        WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO = (WeeklyCounterfeitHoldingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weeklyCounterfeitHoldingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyCounterfeitHoldingDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", dateConfiscated='" + getDateConfiscated() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", depositorsNames='" + getDepositorsNames() + "'" +
            ", tellersNames='" + getTellersNames() + "'" +
            ", dateSubmittedToCBK='" + getDateSubmittedToCBK() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
