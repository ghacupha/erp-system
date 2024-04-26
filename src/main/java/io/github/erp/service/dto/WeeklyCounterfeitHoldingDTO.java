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
