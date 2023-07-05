package io.github.erp.domain;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.0
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaseModelMetadata.
 */
@Entity
@Table(name = "lease_model_metadata")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leasemodelmetadata")
public class LeaseModelMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "model_title", nullable = false, unique = true)
    private String modelTitle;

    @NotNull
    @Column(name = "model_version", nullable = false)
    private Double modelVersion;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "model_notes")
    private byte[] modelNotes;

    @Column(name = "model_notes_content_type")
    private String modelNotesContentType;

    @NotNull
    @Column(name = "annual_discounting_rate", nullable = false)
    private Double annualDiscountingRate;

    @NotNull
    @Column(name = "commencement_date", nullable = false)
    private LocalDate commencementDate;

    @NotNull
    @Column(name = "terminal_date", nullable = false)
    private LocalDate terminalDate;

    @Column(name = "total_reporting_periods")
    private Double totalReportingPeriods;

    @Column(name = "reporting_periods_per_year")
    private Double reportingPeriodsPerYear;

    @Column(name = "settlement_periods_per_year")
    private Double settlementPeriodsPerYear;

    @Column(name = "initial_liability_amount", precision = 21, scale = 2)
    private BigDecimal initialLiabilityAmount;

    @Column(name = "initial_rou_amount", precision = 21, scale = 2)
    private BigDecimal initialROUAmount;

    @Column(name = "total_depreciation_periods")
    private Double totalDepreciationPeriods;

    @ManyToMany
    @JoinTable(
        name = "rel_lease_model_metadata__placeholder",
        joinColumns = @JoinColumn(name = "lease_model_metadata_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_lease_model_metadata__lease_mapping",
        joinColumns = @JoinColumn(name = "lease_model_metadata_id"),
        inverseJoinColumns = @JoinColumn(name = "lease_mapping_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parentMapping", "placeholders" }, allowSetters = true)
    private Set<UniversallyUniqueMapping> leaseMappings = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "systemMappings", "businessDocuments", "contractMetadata" }, allowSetters = true)
    private LeaseContract leaseContract;

    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "leaseMappings",
            "leaseContract",
            "predecessor",
            "liabilityCurrency",
            "rouAssetCurrency",
            "modelAttachments",
            "securityClearance",
            "leaseLiabilityAccount",
            "interestPayableAccount",
            "interestExpenseAccount",
            "rouAssetAccount",
            "rouDepreciationAccount",
            "accruedDepreciationAccount",
        },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private LeaseModelMetadata predecessor;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency liabilityCurrency;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency rouAssetCurrency;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private BusinessDocument modelAttachments;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grantedClearances", "placeholders", "systemParameters" }, allowSetters = true)
    private SecurityClearance securityClearance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount leaseLiabilityAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount interestPayableAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount interestExpenseAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount rouAssetAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount rouDepreciationAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount accruedDepreciationAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseModelMetadata id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelTitle() {
        return this.modelTitle;
    }

    public LeaseModelMetadata modelTitle(String modelTitle) {
        this.setModelTitle(modelTitle);
        return this;
    }

    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }

    public Double getModelVersion() {
        return this.modelVersion;
    }

    public LeaseModelMetadata modelVersion(Double modelVersion) {
        this.setModelVersion(modelVersion);
        return this;
    }

    public void setModelVersion(Double modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDescription() {
        return this.description;
    }

    public LeaseModelMetadata description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getModelNotes() {
        return this.modelNotes;
    }

    public LeaseModelMetadata modelNotes(byte[] modelNotes) {
        this.setModelNotes(modelNotes);
        return this;
    }

    public void setModelNotes(byte[] modelNotes) {
        this.modelNotes = modelNotes;
    }

    public String getModelNotesContentType() {
        return this.modelNotesContentType;
    }

    public LeaseModelMetadata modelNotesContentType(String modelNotesContentType) {
        this.modelNotesContentType = modelNotesContentType;
        return this;
    }

    public void setModelNotesContentType(String modelNotesContentType) {
        this.modelNotesContentType = modelNotesContentType;
    }

    public Double getAnnualDiscountingRate() {
        return this.annualDiscountingRate;
    }

    public LeaseModelMetadata annualDiscountingRate(Double annualDiscountingRate) {
        this.setAnnualDiscountingRate(annualDiscountingRate);
        return this;
    }

    public void setAnnualDiscountingRate(Double annualDiscountingRate) {
        this.annualDiscountingRate = annualDiscountingRate;
    }

    public LocalDate getCommencementDate() {
        return this.commencementDate;
    }

    public LeaseModelMetadata commencementDate(LocalDate commencementDate) {
        this.setCommencementDate(commencementDate);
        return this;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getTerminalDate() {
        return this.terminalDate;
    }

    public LeaseModelMetadata terminalDate(LocalDate terminalDate) {
        this.setTerminalDate(terminalDate);
        return this;
    }

    public void setTerminalDate(LocalDate terminalDate) {
        this.terminalDate = terminalDate;
    }

    public Double getTotalReportingPeriods() {
        return this.totalReportingPeriods;
    }

    public LeaseModelMetadata totalReportingPeriods(Double totalReportingPeriods) {
        this.setTotalReportingPeriods(totalReportingPeriods);
        return this;
    }

    public void setTotalReportingPeriods(Double totalReportingPeriods) {
        this.totalReportingPeriods = totalReportingPeriods;
    }

    public Double getReportingPeriodsPerYear() {
        return this.reportingPeriodsPerYear;
    }

    public LeaseModelMetadata reportingPeriodsPerYear(Double reportingPeriodsPerYear) {
        this.setReportingPeriodsPerYear(reportingPeriodsPerYear);
        return this;
    }

    public void setReportingPeriodsPerYear(Double reportingPeriodsPerYear) {
        this.reportingPeriodsPerYear = reportingPeriodsPerYear;
    }

    public Double getSettlementPeriodsPerYear() {
        return this.settlementPeriodsPerYear;
    }

    public LeaseModelMetadata settlementPeriodsPerYear(Double settlementPeriodsPerYear) {
        this.setSettlementPeriodsPerYear(settlementPeriodsPerYear);
        return this;
    }

    public void setSettlementPeriodsPerYear(Double settlementPeriodsPerYear) {
        this.settlementPeriodsPerYear = settlementPeriodsPerYear;
    }

    public BigDecimal getInitialLiabilityAmount() {
        return this.initialLiabilityAmount;
    }

    public LeaseModelMetadata initialLiabilityAmount(BigDecimal initialLiabilityAmount) {
        this.setInitialLiabilityAmount(initialLiabilityAmount);
        return this;
    }

    public void setInitialLiabilityAmount(BigDecimal initialLiabilityAmount) {
        this.initialLiabilityAmount = initialLiabilityAmount;
    }

    public BigDecimal getInitialROUAmount() {
        return this.initialROUAmount;
    }

    public LeaseModelMetadata initialROUAmount(BigDecimal initialROUAmount) {
        this.setInitialROUAmount(initialROUAmount);
        return this;
    }

    public void setInitialROUAmount(BigDecimal initialROUAmount) {
        this.initialROUAmount = initialROUAmount;
    }

    public Double getTotalDepreciationPeriods() {
        return this.totalDepreciationPeriods;
    }

    public LeaseModelMetadata totalDepreciationPeriods(Double totalDepreciationPeriods) {
        this.setTotalDepreciationPeriods(totalDepreciationPeriods);
        return this;
    }

    public void setTotalDepreciationPeriods(Double totalDepreciationPeriods) {
        this.totalDepreciationPeriods = totalDepreciationPeriods;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public LeaseModelMetadata placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public LeaseModelMetadata addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public LeaseModelMetadata removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public Set<UniversallyUniqueMapping> getLeaseMappings() {
        return this.leaseMappings;
    }

    public void setLeaseMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.leaseMappings = universallyUniqueMappings;
    }

    public LeaseModelMetadata leaseMappings(Set<UniversallyUniqueMapping> universallyUniqueMappings) {
        this.setLeaseMappings(universallyUniqueMappings);
        return this;
    }

    public LeaseModelMetadata addLeaseMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.leaseMappings.add(universallyUniqueMapping);
        return this;
    }

    public LeaseModelMetadata removeLeaseMapping(UniversallyUniqueMapping universallyUniqueMapping) {
        this.leaseMappings.remove(universallyUniqueMapping);
        return this;
    }

    public LeaseContract getLeaseContract() {
        return this.leaseContract;
    }

    public void setLeaseContract(LeaseContract leaseContract) {
        this.leaseContract = leaseContract;
    }

    public LeaseModelMetadata leaseContract(LeaseContract leaseContract) {
        this.setLeaseContract(leaseContract);
        return this;
    }

    public LeaseModelMetadata getPredecessor() {
        return this.predecessor;
    }

    public void setPredecessor(LeaseModelMetadata leaseModelMetadata) {
        this.predecessor = leaseModelMetadata;
    }

    public LeaseModelMetadata predecessor(LeaseModelMetadata leaseModelMetadata) {
        this.setPredecessor(leaseModelMetadata);
        return this;
    }

    public SettlementCurrency getLiabilityCurrency() {
        return this.liabilityCurrency;
    }

    public void setLiabilityCurrency(SettlementCurrency settlementCurrency) {
        this.liabilityCurrency = settlementCurrency;
    }

    public LeaseModelMetadata liabilityCurrency(SettlementCurrency settlementCurrency) {
        this.setLiabilityCurrency(settlementCurrency);
        return this;
    }

    public SettlementCurrency getRouAssetCurrency() {
        return this.rouAssetCurrency;
    }

    public void setRouAssetCurrency(SettlementCurrency settlementCurrency) {
        this.rouAssetCurrency = settlementCurrency;
    }

    public LeaseModelMetadata rouAssetCurrency(SettlementCurrency settlementCurrency) {
        this.setRouAssetCurrency(settlementCurrency);
        return this;
    }

    public BusinessDocument getModelAttachments() {
        return this.modelAttachments;
    }

    public void setModelAttachments(BusinessDocument businessDocument) {
        this.modelAttachments = businessDocument;
    }

    public LeaseModelMetadata modelAttachments(BusinessDocument businessDocument) {
        this.setModelAttachments(businessDocument);
        return this;
    }

    public SecurityClearance getSecurityClearance() {
        return this.securityClearance;
    }

    public void setSecurityClearance(SecurityClearance securityClearance) {
        this.securityClearance = securityClearance;
    }

    public LeaseModelMetadata securityClearance(SecurityClearance securityClearance) {
        this.setSecurityClearance(securityClearance);
        return this;
    }

    public TransactionAccount getLeaseLiabilityAccount() {
        return this.leaseLiabilityAccount;
    }

    public void setLeaseLiabilityAccount(TransactionAccount transactionAccount) {
        this.leaseLiabilityAccount = transactionAccount;
    }

    public LeaseModelMetadata leaseLiabilityAccount(TransactionAccount transactionAccount) {
        this.setLeaseLiabilityAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getInterestPayableAccount() {
        return this.interestPayableAccount;
    }

    public void setInterestPayableAccount(TransactionAccount transactionAccount) {
        this.interestPayableAccount = transactionAccount;
    }

    public LeaseModelMetadata interestPayableAccount(TransactionAccount transactionAccount) {
        this.setInterestPayableAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getInterestExpenseAccount() {
        return this.interestExpenseAccount;
    }

    public void setInterestExpenseAccount(TransactionAccount transactionAccount) {
        this.interestExpenseAccount = transactionAccount;
    }

    public LeaseModelMetadata interestExpenseAccount(TransactionAccount transactionAccount) {
        this.setInterestExpenseAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getRouAssetAccount() {
        return this.rouAssetAccount;
    }

    public void setRouAssetAccount(TransactionAccount transactionAccount) {
        this.rouAssetAccount = transactionAccount;
    }

    public LeaseModelMetadata rouAssetAccount(TransactionAccount transactionAccount) {
        this.setRouAssetAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getRouDepreciationAccount() {
        return this.rouDepreciationAccount;
    }

    public void setRouDepreciationAccount(TransactionAccount transactionAccount) {
        this.rouDepreciationAccount = transactionAccount;
    }

    public LeaseModelMetadata rouDepreciationAccount(TransactionAccount transactionAccount) {
        this.setRouDepreciationAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getAccruedDepreciationAccount() {
        return this.accruedDepreciationAccount;
    }

    public void setAccruedDepreciationAccount(TransactionAccount transactionAccount) {
        this.accruedDepreciationAccount = transactionAccount;
    }

    public LeaseModelMetadata accruedDepreciationAccount(TransactionAccount transactionAccount) {
        this.setAccruedDepreciationAccount(transactionAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseModelMetadata)) {
            return false;
        }
        return id != null && id.equals(((LeaseModelMetadata) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseModelMetadata{" +
            "id=" + getId() +
            ", modelTitle='" + getModelTitle() + "'" +
            ", modelVersion=" + getModelVersion() +
            ", description='" + getDescription() + "'" +
            ", modelNotes='" + getModelNotes() + "'" +
            ", modelNotesContentType='" + getModelNotesContentType() + "'" +
            ", annualDiscountingRate=" + getAnnualDiscountingRate() +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", terminalDate='" + getTerminalDate() + "'" +
            ", totalReportingPeriods=" + getTotalReportingPeriods() +
            ", reportingPeriodsPerYear=" + getReportingPeriodsPerYear() +
            ", settlementPeriodsPerYear=" + getSettlementPeriodsPerYear() +
            ", initialLiabilityAmount=" + getInitialLiabilityAmount() +
            ", initialROUAmount=" + getInitialROUAmount() +
            ", totalDepreciationPeriods=" + getTotalDepreciationPeriods() +
            "}";
    }
}
