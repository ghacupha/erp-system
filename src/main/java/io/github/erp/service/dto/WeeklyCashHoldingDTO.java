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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.WeeklyCashHolding} entity.
 */
public class WeeklyCashHoldingDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private Integer fitUnits;

    @NotNull
    private Integer unfitUnits;

    private InstitutionCodeDTO bankCode;

    private BankBranchCodeDTO branchId;

    private CountySubCountyCodeDTO subCountyCode;

    private KenyanCurrencyDenominationDTO denomination;

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

    public Integer getFitUnits() {
        return fitUnits;
    }

    public void setFitUnits(Integer fitUnits) {
        this.fitUnits = fitUnits;
    }

    public Integer getUnfitUnits() {
        return unfitUnits;
    }

    public void setUnfitUnits(Integer unfitUnits) {
        this.unfitUnits = unfitUnits;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public BankBranchCodeDTO getBranchId() {
        return branchId;
    }

    public void setBranchId(BankBranchCodeDTO branchId) {
        this.branchId = branchId;
    }

    public CountySubCountyCodeDTO getSubCountyCode() {
        return subCountyCode;
    }

    public void setSubCountyCode(CountySubCountyCodeDTO subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public KenyanCurrencyDenominationDTO getDenomination() {
        return denomination;
    }

    public void setDenomination(KenyanCurrencyDenominationDTO denomination) {
        this.denomination = denomination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeeklyCashHoldingDTO)) {
            return false;
        }

        WeeklyCashHoldingDTO weeklyCashHoldingDTO = (WeeklyCashHoldingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weeklyCashHoldingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyCashHoldingDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", fitUnits=" + getFitUnits() +
            ", unfitUnits=" + getUnfitUnits() +
            ", bankCode=" + getBankCode() +
            ", branchId=" + getBranchId() +
            ", subCountyCode=" + getSubCountyCode() +
            ", denomination=" + getDenomination() +
            "}";
    }
}
