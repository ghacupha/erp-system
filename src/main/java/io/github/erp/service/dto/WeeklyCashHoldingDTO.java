package io.github.erp.service.dto;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
