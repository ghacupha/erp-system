package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LeaseModelMetadata} entity.
 */
public class LeaseModelMetadataDTO implements Serializable {

    private Long id;

    @NotNull
    private String modelTitle;

    @NotNull
    private Double modelVersion;

    private String description;

    @Lob
    private byte[] modelNotes;

    private String modelNotesContentType;

    @NotNull
    private Double annualDiscountingRate;

    @NotNull
    private LocalDate commencementDate;

    @NotNull
    private LocalDate terminalDate;

    private Double totalReportingPeriods;

    private Double reportingPeriodsPerYear;

    private Double settlementPeriodsPerYear;

    private BigDecimal initialLiabilityAmount;

    private BigDecimal initialROUAmount;

    private Double totalDepreciationPeriods;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> leaseMappings = new HashSet<>();

    private LeaseContractDTO leaseContract;

    private LeaseModelMetadataDTO predecessor;

    private SettlementCurrencyDTO liabilityCurrency;

    private SettlementCurrencyDTO rouAssetCurrency;

    private BusinessDocumentDTO modelAttachments;

    private SecurityClearanceDTO securityClearance;

    private TransactionAccountDTO leaseLiabilityAccount;

    private TransactionAccountDTO interestPayableAccount;

    private TransactionAccountDTO interestExpenseAccount;

    private TransactionAccountDTO rouAssetAccount;

    private TransactionAccountDTO rouDepreciationAccount;

    private TransactionAccountDTO accruedDepreciationAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public Double getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(Double modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getModelNotes() {
        return modelNotes;
    }

    public void setModelNotes(byte[] modelNotes) {
        this.modelNotes = modelNotes;
    }

    public String getModelNotesContentType() {
        return modelNotesContentType;
    }

    public void setModelNotesContentType(String modelNotesContentType) {
        this.modelNotesContentType = modelNotesContentType;
    }

    public Double getAnnualDiscountingRate() {
        return annualDiscountingRate;
    }

    public void setAnnualDiscountingRate(Double annualDiscountingRate) {
        this.annualDiscountingRate = annualDiscountingRate;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getTerminalDate() {
        return terminalDate;
    }

    public void setTerminalDate(LocalDate terminalDate) {
        this.terminalDate = terminalDate;
    }

    public Double getTotalReportingPeriods() {
        return totalReportingPeriods;
    }

    public void setTotalReportingPeriods(Double totalReportingPeriods) {
        this.totalReportingPeriods = totalReportingPeriods;
    }

    public Double getReportingPeriodsPerYear() {
        return reportingPeriodsPerYear;
    }

    public void setReportingPeriodsPerYear(Double reportingPeriodsPerYear) {
        this.reportingPeriodsPerYear = reportingPeriodsPerYear;
    }

    public Double getSettlementPeriodsPerYear() {
        return settlementPeriodsPerYear;
    }

    public void setSettlementPeriodsPerYear(Double settlementPeriodsPerYear) {
        this.settlementPeriodsPerYear = settlementPeriodsPerYear;
    }

    public BigDecimal getInitialLiabilityAmount() {
        return initialLiabilityAmount;
    }

    public void setInitialLiabilityAmount(BigDecimal initialLiabilityAmount) {
        this.initialLiabilityAmount = initialLiabilityAmount;
    }

    public BigDecimal getInitialROUAmount() {
        return initialROUAmount;
    }

    public void setInitialROUAmount(BigDecimal initialROUAmount) {
        this.initialROUAmount = initialROUAmount;
    }

    public Double getTotalDepreciationPeriods() {
        return totalDepreciationPeriods;
    }

    public void setTotalDepreciationPeriods(Double totalDepreciationPeriods) {
        this.totalDepreciationPeriods = totalDepreciationPeriods;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getLeaseMappings() {
        return leaseMappings;
    }

    public void setLeaseMappings(Set<UniversallyUniqueMappingDTO> leaseMappings) {
        this.leaseMappings = leaseMappings;
    }

    public LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    public LeaseModelMetadataDTO getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(LeaseModelMetadataDTO predecessor) {
        this.predecessor = predecessor;
    }

    public SettlementCurrencyDTO getLiabilityCurrency() {
        return liabilityCurrency;
    }

    public void setLiabilityCurrency(SettlementCurrencyDTO liabilityCurrency) {
        this.liabilityCurrency = liabilityCurrency;
    }

    public SettlementCurrencyDTO getRouAssetCurrency() {
        return rouAssetCurrency;
    }

    public void setRouAssetCurrency(SettlementCurrencyDTO rouAssetCurrency) {
        this.rouAssetCurrency = rouAssetCurrency;
    }

    public BusinessDocumentDTO getModelAttachments() {
        return modelAttachments;
    }

    public void setModelAttachments(BusinessDocumentDTO modelAttachments) {
        this.modelAttachments = modelAttachments;
    }

    public SecurityClearanceDTO getSecurityClearance() {
        return securityClearance;
    }

    public void setSecurityClearance(SecurityClearanceDTO securityClearance) {
        this.securityClearance = securityClearance;
    }

    public TransactionAccountDTO getLeaseLiabilityAccount() {
        return leaseLiabilityAccount;
    }

    public void setLeaseLiabilityAccount(TransactionAccountDTO leaseLiabilityAccount) {
        this.leaseLiabilityAccount = leaseLiabilityAccount;
    }

    public TransactionAccountDTO getInterestPayableAccount() {
        return interestPayableAccount;
    }

    public void setInterestPayableAccount(TransactionAccountDTO interestPayableAccount) {
        this.interestPayableAccount = interestPayableAccount;
    }

    public TransactionAccountDTO getInterestExpenseAccount() {
        return interestExpenseAccount;
    }

    public void setInterestExpenseAccount(TransactionAccountDTO interestExpenseAccount) {
        this.interestExpenseAccount = interestExpenseAccount;
    }

    public TransactionAccountDTO getRouAssetAccount() {
        return rouAssetAccount;
    }

    public void setRouAssetAccount(TransactionAccountDTO rouAssetAccount) {
        this.rouAssetAccount = rouAssetAccount;
    }

    public TransactionAccountDTO getRouDepreciationAccount() {
        return rouDepreciationAccount;
    }

    public void setRouDepreciationAccount(TransactionAccountDTO rouDepreciationAccount) {
        this.rouDepreciationAccount = rouDepreciationAccount;
    }

    public TransactionAccountDTO getAccruedDepreciationAccount() {
        return accruedDepreciationAccount;
    }

    public void setAccruedDepreciationAccount(TransactionAccountDTO accruedDepreciationAccount) {
        this.accruedDepreciationAccount = accruedDepreciationAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseModelMetadataDTO)) {
            return false;
        }

        LeaseModelMetadataDTO leaseModelMetadataDTO = (LeaseModelMetadataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaseModelMetadataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseModelMetadataDTO{" +
            "id=" + getId() +
            ", modelTitle='" + getModelTitle() + "'" +
            ", modelVersion=" + getModelVersion() +
            ", description='" + getDescription() + "'" +
            ", modelNotes='" + getModelNotes() + "'" +
            ", annualDiscountingRate=" + getAnnualDiscountingRate() +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", terminalDate='" + getTerminalDate() + "'" +
            ", totalReportingPeriods=" + getTotalReportingPeriods() +
            ", reportingPeriodsPerYear=" + getReportingPeriodsPerYear() +
            ", settlementPeriodsPerYear=" + getSettlementPeriodsPerYear() +
            ", initialLiabilityAmount=" + getInitialLiabilityAmount() +
            ", initialROUAmount=" + getInitialROUAmount() +
            ", totalDepreciationPeriods=" + getTotalDepreciationPeriods() +
            ", placeholders=" + getPlaceholders() +
            ", leaseMappings=" + getLeaseMappings() +
            ", leaseContract=" + getLeaseContract() +
            ", predecessor=" + getPredecessor() +
            ", liabilityCurrency=" + getLiabilityCurrency() +
            ", rouAssetCurrency=" + getRouAssetCurrency() +
            ", modelAttachments=" + getModelAttachments() +
            ", securityClearance=" + getSecurityClearance() +
            ", leaseLiabilityAccount=" + getLeaseLiabilityAccount() +
            ", interestPayableAccount=" + getInterestPayableAccount() +
            ", interestExpenseAccount=" + getInterestExpenseAccount() +
            ", rouAssetAccount=" + getRouAssetAccount() +
            ", rouDepreciationAccount=" + getRouDepreciationAccount() +
            ", accruedDepreciationAccount=" + getAccruedDepreciationAccount() +
            "}";
    }
}
