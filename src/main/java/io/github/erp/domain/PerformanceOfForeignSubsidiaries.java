package io.github.erp.domain;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PerformanceOfForeignSubsidiaries.
 */
@Entity
@Table(name = "foreign_subsidiary_performance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "performanceofforeignsubsidiaries")
public class PerformanceOfForeignSubsidiaries implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "subsidiary_name", nullable = false, unique = true)
    private String subsidiaryName;

    @NotNull
    @Column(name = "reporting_date", nullable = false)
    private LocalDate reportingDate;

    @NotNull
    @Column(name = "subsidiary_id", nullable = false, unique = true)
    private String subsidiaryId;

    @NotNull
    @Column(name = "gross_loans_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal grossLoansAmount;

    @NotNull
    @Column(name = "gross_npa_loan_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal grossNPALoanAmount;

    @NotNull
    @Column(name = "gross_assets_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal grossAssetsAmount;

    @NotNull
    @Column(name = "gross_deposits_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal grossDepositsAmount;

    @NotNull
    @Column(name = "profit_before_tax", precision = 21, scale = 2, nullable = false)
    private BigDecimal profitBeforeTax;

    @NotNull
    @Column(name = "total_capital_adequacy_ratio", nullable = false)
    private Double totalCapitalAdequacyRatio;

    @NotNull
    @Column(name = "liquidity_ratio", nullable = false)
    private Double liquidityRatio;

    @NotNull
    @Column(name = "general_provisions", precision = 21, scale = 2, nullable = false)
    private BigDecimal generalProvisions;

    @NotNull
    @Column(name = "specific_provisions", precision = 21, scale = 2, nullable = false)
    private BigDecimal specificProvisions;

    @NotNull
    @Column(name = "interest_in_suspense_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal interestInSuspenseAmount;

    @NotNull
    @Min(value = 1)
    @Column(name = "total_number_of_staff", nullable = false)
    private Integer totalNumberOfStaff;

    @NotNull
    @Min(value = 1)
    @Column(name = "number_of_branches", nullable = false)
    private Integer numberOfBranches;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private InstitutionCode bankCode;

    @ManyToOne(optional = false)
    @NotNull
    private IsoCountryCode subsidiaryCountryCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PerformanceOfForeignSubsidiaries id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubsidiaryName() {
        return this.subsidiaryName;
    }

    public PerformanceOfForeignSubsidiaries subsidiaryName(String subsidiaryName) {
        this.setSubsidiaryName(subsidiaryName);
        return this;
    }

    public void setSubsidiaryName(String subsidiaryName) {
        this.subsidiaryName = subsidiaryName;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public PerformanceOfForeignSubsidiaries reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getSubsidiaryId() {
        return this.subsidiaryId;
    }

    public PerformanceOfForeignSubsidiaries subsidiaryId(String subsidiaryId) {
        this.setSubsidiaryId(subsidiaryId);
        return this;
    }

    public void setSubsidiaryId(String subsidiaryId) {
        this.subsidiaryId = subsidiaryId;
    }

    public BigDecimal getGrossLoansAmount() {
        return this.grossLoansAmount;
    }

    public PerformanceOfForeignSubsidiaries grossLoansAmount(BigDecimal grossLoansAmount) {
        this.setGrossLoansAmount(grossLoansAmount);
        return this;
    }

    public void setGrossLoansAmount(BigDecimal grossLoansAmount) {
        this.grossLoansAmount = grossLoansAmount;
    }

    public BigDecimal getGrossNPALoanAmount() {
        return this.grossNPALoanAmount;
    }

    public PerformanceOfForeignSubsidiaries grossNPALoanAmount(BigDecimal grossNPALoanAmount) {
        this.setGrossNPALoanAmount(grossNPALoanAmount);
        return this;
    }

    public void setGrossNPALoanAmount(BigDecimal grossNPALoanAmount) {
        this.grossNPALoanAmount = grossNPALoanAmount;
    }

    public BigDecimal getGrossAssetsAmount() {
        return this.grossAssetsAmount;
    }

    public PerformanceOfForeignSubsidiaries grossAssetsAmount(BigDecimal grossAssetsAmount) {
        this.setGrossAssetsAmount(grossAssetsAmount);
        return this;
    }

    public void setGrossAssetsAmount(BigDecimal grossAssetsAmount) {
        this.grossAssetsAmount = grossAssetsAmount;
    }

    public BigDecimal getGrossDepositsAmount() {
        return this.grossDepositsAmount;
    }

    public PerformanceOfForeignSubsidiaries grossDepositsAmount(BigDecimal grossDepositsAmount) {
        this.setGrossDepositsAmount(grossDepositsAmount);
        return this;
    }

    public void setGrossDepositsAmount(BigDecimal grossDepositsAmount) {
        this.grossDepositsAmount = grossDepositsAmount;
    }

    public BigDecimal getProfitBeforeTax() {
        return this.profitBeforeTax;
    }

    public PerformanceOfForeignSubsidiaries profitBeforeTax(BigDecimal profitBeforeTax) {
        this.setProfitBeforeTax(profitBeforeTax);
        return this;
    }

    public void setProfitBeforeTax(BigDecimal profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    public Double getTotalCapitalAdequacyRatio() {
        return this.totalCapitalAdequacyRatio;
    }

    public PerformanceOfForeignSubsidiaries totalCapitalAdequacyRatio(Double totalCapitalAdequacyRatio) {
        this.setTotalCapitalAdequacyRatio(totalCapitalAdequacyRatio);
        return this;
    }

    public void setTotalCapitalAdequacyRatio(Double totalCapitalAdequacyRatio) {
        this.totalCapitalAdequacyRatio = totalCapitalAdequacyRatio;
    }

    public Double getLiquidityRatio() {
        return this.liquidityRatio;
    }

    public PerformanceOfForeignSubsidiaries liquidityRatio(Double liquidityRatio) {
        this.setLiquidityRatio(liquidityRatio);
        return this;
    }

    public void setLiquidityRatio(Double liquidityRatio) {
        this.liquidityRatio = liquidityRatio;
    }

    public BigDecimal getGeneralProvisions() {
        return this.generalProvisions;
    }

    public PerformanceOfForeignSubsidiaries generalProvisions(BigDecimal generalProvisions) {
        this.setGeneralProvisions(generalProvisions);
        return this;
    }

    public void setGeneralProvisions(BigDecimal generalProvisions) {
        this.generalProvisions = generalProvisions;
    }

    public BigDecimal getSpecificProvisions() {
        return this.specificProvisions;
    }

    public PerformanceOfForeignSubsidiaries specificProvisions(BigDecimal specificProvisions) {
        this.setSpecificProvisions(specificProvisions);
        return this;
    }

    public void setSpecificProvisions(BigDecimal specificProvisions) {
        this.specificProvisions = specificProvisions;
    }

    public BigDecimal getInterestInSuspenseAmount() {
        return this.interestInSuspenseAmount;
    }

    public PerformanceOfForeignSubsidiaries interestInSuspenseAmount(BigDecimal interestInSuspenseAmount) {
        this.setInterestInSuspenseAmount(interestInSuspenseAmount);
        return this;
    }

    public void setInterestInSuspenseAmount(BigDecimal interestInSuspenseAmount) {
        this.interestInSuspenseAmount = interestInSuspenseAmount;
    }

    public Integer getTotalNumberOfStaff() {
        return this.totalNumberOfStaff;
    }

    public PerformanceOfForeignSubsidiaries totalNumberOfStaff(Integer totalNumberOfStaff) {
        this.setTotalNumberOfStaff(totalNumberOfStaff);
        return this;
    }

    public void setTotalNumberOfStaff(Integer totalNumberOfStaff) {
        this.totalNumberOfStaff = totalNumberOfStaff;
    }

    public Integer getNumberOfBranches() {
        return this.numberOfBranches;
    }

    public PerformanceOfForeignSubsidiaries numberOfBranches(Integer numberOfBranches) {
        this.setNumberOfBranches(numberOfBranches);
        return this;
    }

    public void setNumberOfBranches(Integer numberOfBranches) {
        this.numberOfBranches = numberOfBranches;
    }

    public InstitutionCode getBankCode() {
        return this.bankCode;
    }

    public void setBankCode(InstitutionCode institutionCode) {
        this.bankCode = institutionCode;
    }

    public PerformanceOfForeignSubsidiaries bankCode(InstitutionCode institutionCode) {
        this.setBankCode(institutionCode);
        return this;
    }

    public IsoCountryCode getSubsidiaryCountryCode() {
        return this.subsidiaryCountryCode;
    }

    public void setSubsidiaryCountryCode(IsoCountryCode isoCountryCode) {
        this.subsidiaryCountryCode = isoCountryCode;
    }

    public PerformanceOfForeignSubsidiaries subsidiaryCountryCode(IsoCountryCode isoCountryCode) {
        this.setSubsidiaryCountryCode(isoCountryCode);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PerformanceOfForeignSubsidiaries)) {
            return false;
        }
        return id != null && id.equals(((PerformanceOfForeignSubsidiaries) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerformanceOfForeignSubsidiaries{" +
            "id=" + getId() +
            ", subsidiaryName='" + getSubsidiaryName() + "'" +
            ", reportingDate='" + getReportingDate() + "'" +
            ", subsidiaryId='" + getSubsidiaryId() + "'" +
            ", grossLoansAmount=" + getGrossLoansAmount() +
            ", grossNPALoanAmount=" + getGrossNPALoanAmount() +
            ", grossAssetsAmount=" + getGrossAssetsAmount() +
            ", grossDepositsAmount=" + getGrossDepositsAmount() +
            ", profitBeforeTax=" + getProfitBeforeTax() +
            ", totalCapitalAdequacyRatio=" + getTotalCapitalAdequacyRatio() +
            ", liquidityRatio=" + getLiquidityRatio() +
            ", generalProvisions=" + getGeneralProvisions() +
            ", specificProvisions=" + getSpecificProvisions() +
            ", interestInSuspenseAmount=" + getInterestInSuspenseAmount() +
            ", totalNumberOfStaff=" + getTotalNumberOfStaff() +
            ", numberOfBranches=" + getNumberOfBranches() +
            "}";
    }
}
