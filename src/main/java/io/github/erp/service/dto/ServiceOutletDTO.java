package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
