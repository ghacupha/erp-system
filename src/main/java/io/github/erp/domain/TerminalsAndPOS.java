package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TerminalsAndPOS.
 */
@Entity
@Table(name = "terminals_andpos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "terminalsandpos")
public class TerminalsAndPOS implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reporting_date", nullable = false)
    private LocalDate reportingDate;

    @NotNull
    @Column(name = "terminal_id", nullable = false, unique = true)
    private String terminalId;

    @NotNull
    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @NotNull
    @Column(name = "terminal_name", nullable = false)
    private String terminalName;

    @NotNull
    @Column(name = "terminal_location", nullable = false)
    private String terminalLocation;

    @NotNull
    @Column(name = "iso_6709_latitute", nullable = false)
    private Double iso6709Latitute;

    @NotNull
    @Column(name = "iso_6709_longitude", nullable = false)
    private Double iso6709Longitude;

    @NotNull
    @Column(name = "terminal_opening_date", nullable = false)
    private LocalDate terminalOpeningDate;

    @Column(name = "terminal_closure_date")
    private LocalDate terminalClosureDate;

    @ManyToOne(optional = false)
    @NotNull
    private TerminalTypes terminalType;

    @ManyToOne(optional = false)
    @NotNull
    private TerminalFunctions terminalFunctionality;

    @ManyToOne(optional = false)
    @NotNull
    private CountySubCountyCode physicalLocation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private BankBranchCode branchId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TerminalsAndPOS id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public TerminalsAndPOS reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getTerminalId() {
        return this.terminalId;
    }

    public TerminalsAndPOS terminalId(String terminalId) {
        this.setTerminalId(terminalId);
        return this;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public TerminalsAndPOS merchantId(String merchantId) {
        this.setMerchantId(merchantId);
        return this;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTerminalName() {
        return this.terminalName;
    }

    public TerminalsAndPOS terminalName(String terminalName) {
        this.setTerminalName(terminalName);
        return this;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalLocation() {
        return this.terminalLocation;
    }

    public TerminalsAndPOS terminalLocation(String terminalLocation) {
        this.setTerminalLocation(terminalLocation);
        return this;
    }

    public void setTerminalLocation(String terminalLocation) {
        this.terminalLocation = terminalLocation;
    }

    public Double getIso6709Latitute() {
        return this.iso6709Latitute;
    }

    public TerminalsAndPOS iso6709Latitute(Double iso6709Latitute) {
        this.setIso6709Latitute(iso6709Latitute);
        return this;
    }

    public void setIso6709Latitute(Double iso6709Latitute) {
        this.iso6709Latitute = iso6709Latitute;
    }

    public Double getIso6709Longitude() {
        return this.iso6709Longitude;
    }

    public TerminalsAndPOS iso6709Longitude(Double iso6709Longitude) {
        this.setIso6709Longitude(iso6709Longitude);
        return this;
    }

    public void setIso6709Longitude(Double iso6709Longitude) {
        this.iso6709Longitude = iso6709Longitude;
    }

    public LocalDate getTerminalOpeningDate() {
        return this.terminalOpeningDate;
    }

    public TerminalsAndPOS terminalOpeningDate(LocalDate terminalOpeningDate) {
        this.setTerminalOpeningDate(terminalOpeningDate);
        return this;
    }

    public void setTerminalOpeningDate(LocalDate terminalOpeningDate) {
        this.terminalOpeningDate = terminalOpeningDate;
    }

    public LocalDate getTerminalClosureDate() {
        return this.terminalClosureDate;
    }

    public TerminalsAndPOS terminalClosureDate(LocalDate terminalClosureDate) {
        this.setTerminalClosureDate(terminalClosureDate);
        return this;
    }

    public void setTerminalClosureDate(LocalDate terminalClosureDate) {
        this.terminalClosureDate = terminalClosureDate;
    }

    public TerminalTypes getTerminalType() {
        return this.terminalType;
    }

    public void setTerminalType(TerminalTypes terminalTypes) {
        this.terminalType = terminalTypes;
    }

    public TerminalsAndPOS terminalType(TerminalTypes terminalTypes) {
        this.setTerminalType(terminalTypes);
        return this;
    }

    public TerminalFunctions getTerminalFunctionality() {
        return this.terminalFunctionality;
    }

    public void setTerminalFunctionality(TerminalFunctions terminalFunctions) {
        this.terminalFunctionality = terminalFunctions;
    }

    public TerminalsAndPOS terminalFunctionality(TerminalFunctions terminalFunctions) {
        this.setTerminalFunctionality(terminalFunctions);
        return this;
    }

    public CountySubCountyCode getPhysicalLocation() {
        return this.physicalLocation;
    }

    public void setPhysicalLocation(CountySubCountyCode countySubCountyCode) {
        this.physicalLocation = countySubCountyCode;
    }

    public TerminalsAndPOS physicalLocation(CountySubCountyCode countySubCountyCode) {
        this.setPhysicalLocation(countySubCountyCode);
        return this;
    }

    public InstitutionCode getBankId() {
        return this.bankId;
    }

    public void setBankId(InstitutionCode institutionCode) {
        this.bankId = institutionCode;
    }

    public TerminalsAndPOS bankId(InstitutionCode institutionCode) {
        this.setBankId(institutionCode);
        return this;
    }

    public BankBranchCode getBranchId() {
        return this.branchId;
    }

    public void setBranchId(BankBranchCode bankBranchCode) {
        this.branchId = bankBranchCode;
    }

    public TerminalsAndPOS branchId(BankBranchCode bankBranchCode) {
        this.setBranchId(bankBranchCode);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TerminalsAndPOS)) {
            return false;
        }
        return id != null && id.equals(((TerminalsAndPOS) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalsAndPOS{" +
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
            "}";
    }
}
