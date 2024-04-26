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
 * A DTO for the {@link io.github.erp.domain.TerminalsAndPOS} entity.
 */
public class TerminalsAndPOSDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate reportingDate;

    @NotNull
    private String terminalId;

    @NotNull
    private String merchantId;

    @NotNull
    private String terminalName;

    @NotNull
    private String terminalLocation;

    @NotNull
    private Double iso6709Latitute;

    @NotNull
    private Double iso6709Longitude;

    @NotNull
    private LocalDate terminalOpeningDate;

    private LocalDate terminalClosureDate;

    private TerminalTypesDTO terminalType;

    private TerminalFunctionsDTO terminalFunctionality;

    private CountySubCountyCodeDTO physicalLocation;

    private InstitutionCodeDTO bankId;

    private BankBranchCodeDTO branchId;

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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalLocation() {
        return terminalLocation;
    }

    public void setTerminalLocation(String terminalLocation) {
        this.terminalLocation = terminalLocation;
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

    public LocalDate getTerminalOpeningDate() {
        return terminalOpeningDate;
    }

    public void setTerminalOpeningDate(LocalDate terminalOpeningDate) {
        this.terminalOpeningDate = terminalOpeningDate;
    }

    public LocalDate getTerminalClosureDate() {
        return terminalClosureDate;
    }

    public void setTerminalClosureDate(LocalDate terminalClosureDate) {
        this.terminalClosureDate = terminalClosureDate;
    }

    public TerminalTypesDTO getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(TerminalTypesDTO terminalType) {
        this.terminalType = terminalType;
    }

    public TerminalFunctionsDTO getTerminalFunctionality() {
        return terminalFunctionality;
    }

    public void setTerminalFunctionality(TerminalFunctionsDTO terminalFunctionality) {
        this.terminalFunctionality = terminalFunctionality;
    }

    public CountySubCountyCodeDTO getPhysicalLocation() {
        return physicalLocation;
    }

    public void setPhysicalLocation(CountySubCountyCodeDTO physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    public InstitutionCodeDTO getBankId() {
        return bankId;
    }

    public void setBankId(InstitutionCodeDTO bankId) {
        this.bankId = bankId;
    }

    public BankBranchCodeDTO getBranchId() {
        return branchId;
    }

    public void setBranchId(BankBranchCodeDTO branchId) {
        this.branchId = branchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TerminalsAndPOSDTO)) {
            return false;
        }

        TerminalsAndPOSDTO terminalsAndPOSDTO = (TerminalsAndPOSDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, terminalsAndPOSDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalsAndPOSDTO{" +
            "id=" + getId() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", terminalId='" + getTerminalId() + "'" +
            ", merchantId='" + getMerchantId() + "'" +
            ", terminalName='" + getTerminalName() + "'" +
            ", terminalLocation='" + getTerminalLocation() + "'" +
            ", iso6709Latitute=" + getIso6709Latitute() +
            ", iso6709Longitude=" + getIso6709Longitude() +
            ", terminalOpeningDate='" + getTerminalOpeningDate() + "'" +
            ", terminalClosureDate='" + getTerminalClosureDate() + "'" +
            ", terminalType=" + getTerminalType() +
            ", terminalFunctionality=" + getTerminalFunctionality() +
            ", physicalLocation=" + getPhysicalLocation() +
            ", bankId=" + getBankId() +
            ", branchId=" + getBranchId() +
            "}";
    }
}
