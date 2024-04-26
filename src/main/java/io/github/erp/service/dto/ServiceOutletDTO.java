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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ServiceOutlet} entity.
 */
public class ServiceOutletDTO implements Serializable {

    private Long id;

    @NotNull
    private String outletCode;

    @NotNull
    private String outletName;

    private String town;

    private String parliamentaryConstituency;

    private String gpsCoordinates;

    private LocalDate outletOpeningDate;

    private LocalDate regulatorApprovalDate;

    private LocalDate outletClosureDate;

    private LocalDate dateLastModified;

    private BigDecimal licenseFeePayable;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private BankBranchCodeDTO bankCode;

    private OutletTypeDTO outletType;

    private OutletStatusDTO outletStatus;

    private CountyCodeDTO countyName;

    private CountyCodeDTO subCountyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
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

    public String getParliamentaryConstituency() {
        return parliamentaryConstituency;
    }

    public void setParliamentaryConstituency(String parliamentaryConstituency) {
        this.parliamentaryConstituency = parliamentaryConstituency;
    }

    public String getGpsCoordinates() {
        return gpsCoordinates;
    }

    public void setGpsCoordinates(String gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }

    public LocalDate getOutletOpeningDate() {
        return outletOpeningDate;
    }

    public void setOutletOpeningDate(LocalDate outletOpeningDate) {
        this.outletOpeningDate = outletOpeningDate;
    }

    public LocalDate getRegulatorApprovalDate() {
        return regulatorApprovalDate;
    }

    public void setRegulatorApprovalDate(LocalDate regulatorApprovalDate) {
        this.regulatorApprovalDate = regulatorApprovalDate;
    }

    public LocalDate getOutletClosureDate() {
        return outletClosureDate;
    }

    public void setOutletClosureDate(LocalDate outletClosureDate) {
        this.outletClosureDate = outletClosureDate;
    }

    public LocalDate getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(LocalDate dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public BigDecimal getLicenseFeePayable() {
        return licenseFeePayable;
    }

    public void setLicenseFeePayable(BigDecimal licenseFeePayable) {
        this.licenseFeePayable = licenseFeePayable;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public BankBranchCodeDTO getBankCode() {
        return bankCode;
    }

    public void setBankCode(BankBranchCodeDTO bankCode) {
        this.bankCode = bankCode;
    }

    public OutletTypeDTO getOutletType() {
        return outletType;
    }

    public void setOutletType(OutletTypeDTO outletType) {
        this.outletType = outletType;
    }

    public OutletStatusDTO getOutletStatus() {
        return outletStatus;
    }

    public void setOutletStatus(OutletStatusDTO outletStatus) {
        this.outletStatus = outletStatus;
    }

    public CountyCodeDTO getCountyName() {
        return countyName;
    }

    public void setCountyName(CountyCodeDTO countyName) {
        this.countyName = countyName;
    }

    public CountyCodeDTO getSubCountyName() {
        return subCountyName;
    }

    public void setSubCountyName(CountyCodeDTO subCountyName) {
        this.subCountyName = subCountyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceOutletDTO)) {
            return false;
        }

        ServiceOutletDTO serviceOutletDTO = (ServiceOutletDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceOutletDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceOutletDTO{" +
            "id=" + getId() +
            ", outletCode='" + getOutletCode() + "'" +
            ", outletName='" + getOutletName() + "'" +
            ", town='" + getTown() + "'" +
            ", parliamentaryConstituency='" + getParliamentaryConstituency() + "'" +
            ", gpsCoordinates='" + getGpsCoordinates() + "'" +
            ", outletOpeningDate='" + getOutletOpeningDate() + "'" +
            ", regulatorApprovalDate='" + getRegulatorApprovalDate() + "'" +
            ", outletClosureDate='" + getOutletClosureDate() + "'" +
            ", dateLastModified='" + getDateLastModified() + "'" +
            ", licenseFeePayable=" + getLicenseFeePayable() +
            ", placeholders=" + getPlaceholders() +
            ", bankCode=" + getBankCode() +
            ", outletType=" + getOutletType() +
            ", outletStatus=" + getOutletStatus() +
            ", countyName=" + getCountyName() +
            ", subCountyName=" + getSubCountyName() +
            "}";
    }
}
