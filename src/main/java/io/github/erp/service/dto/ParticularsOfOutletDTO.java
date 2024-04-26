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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ParticularsOfOutlet} entity.
 */
public class ParticularsOfOutletDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate businessReportingDate;

    @NotNull
    private String outletName;

    @NotNull
    private String town;

    @NotNull
    private Double iso6709Latitute;

    @NotNull
    private Double iso6709Longitude;

    @NotNull
    private LocalDate cbkApprovalDate;

    @NotNull
    private LocalDate outletOpeningDate;

    private LocalDate outletClosureDate;

    @NotNull
    private BigDecimal licenseFeePayable;

    private CountySubCountyCodeDTO subCountyCode;

    private InstitutionCodeDTO bankCode;

    private BankBranchCodeDTO outletId;

    private OutletTypeDTO typeOfOutlet;

    private OutletStatusDTO outletStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBusinessReportingDate() {
        return businessReportingDate;
    }

    public void setBusinessReportingDate(LocalDate businessReportingDate) {
        this.businessReportingDate = businessReportingDate;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Double getIso6709Latitute() {
        return iso6709Latitute;
    }

    public void setIso6709Latitute(Double iso6709Latitute) {
        this.iso6709Latitute = iso6709Latitute;
    }

    public Double getIso6709Longitude() {
        return iso6709Longitude;
    }

    public void setIso6709Longitude(Double iso6709Longitude) {
        this.iso6709Longitude = iso6709Longitude;
    }

    public LocalDate getCbkApprovalDate() {
        return cbkApprovalDate;
    }

    public void setCbkApprovalDate(LocalDate cbkApprovalDate) {
        this.cbkApprovalDate = cbkApprovalDate;
    }

    public LocalDate getOutletOpeningDate() {
        return outletOpeningDate;
    }

    public void setOutletOpeningDate(LocalDate outletOpeningDate) {
        this.outletOpeningDate = outletOpeningDate;
    }

    public LocalDate getOutletClosureDate() {
        return outletClosureDate;
    }

    public void setOutletClosureDate(LocalDate outletClosureDate) {
        this.outletClosureDate = outletClosureDate;
    }

    public BigDecimal getLicenseFeePayable() {
        return licenseFeePayable;
    }

    public void setLicenseFeePayable(BigDecimal licenseFeePayable) {
        this.licenseFeePayable = licenseFeePayable;
    }

    public CountySubCountyCodeDTO getSubCountyCode() {
        return subCountyCode;
    }

    public void setSubCountyCode(CountySubCountyCodeDTO subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public InstitutionCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(InstitutionCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public BankBranchCodeDTO getOutletId() {
        return outletId;
    }

    public void setOutletId(BankBranchCodeDTO outletId) {
        this.outletId = outletId;
    }

    public OutletTypeDTO getTypeOfOutlet() {
        return typeOfOutlet;
    }

    public void setTypeOfOutlet(OutletTypeDTO typeOfOutlet) {
        this.typeOfOutlet = typeOfOutlet;
    }

    public OutletStatusDTO getOutletStatus() {
        return outletStatus;
    }

    public void setOutletStatus(OutletStatusDTO outletStatus) {
        this.outletStatus = outletStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticularsOfOutletDTO)) {
            return false;
        }

        ParticularsOfOutletDTO particularsOfOutletDTO = (ParticularsOfOutletDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, particularsOfOutletDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticularsOfOutletDTO{" +
            "id=" + getId() +
            ", businessReportingDate='" + getBusinessReportingDate() + "'" +
            ", outletName='" + getOutletName() + "'" +
            ", town='" + getTown() + "'" +
            ", iso6709Latitute=" + getIso6709Latitute() +
            ", iso6709Longitude=" + getIso6709Longitude() +
            ", cbkApprovalDate='" + getCbkApprovalDate() + "'" +
            ", outletOpeningDate='" + getOutletOpeningDate() + "'" +
            ", outletClosureDate='" + getOutletClosureDate() + "'" +
            ", licenseFeePayable=" + getLicenseFeePayable() +
            ", subCountyCode=" + getSubCountyCode() +
            ", bankCode=" + getBankCode() +
            ", outletId=" + getOutletId() +
            ", typeOfOutlet=" + getTypeOfOutlet() +
            ", outletStatus=" + getOutletStatus() +
            "}";
    }
}
