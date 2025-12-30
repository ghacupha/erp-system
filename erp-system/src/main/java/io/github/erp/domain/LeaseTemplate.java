package io.github.erp.domain;
/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A LeaseTemplate.
 */
@Entity
@Table(name = "lease_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Document(indexName = "leasetemplate")
public class LeaseTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "template_title", nullable = false, unique = true)
    private String templateTitle;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount assetAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount depreciationAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount accruedDepreciationAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount interestPaidTransferDebitAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount interestPaidTransferCreditAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount interestAccruedDebitAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount interestAccruedCreditAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount leaseRecognitionDebitAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount leaseRecognitionCreditAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount leaseRepaymentDebitAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount leaseRepaymentCreditAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount rouRecognitionCreditAccount;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "accountLedger", "accountCategory", "placeholders", "serviceOutlet", "settlementCurrency", "institution" },
        allowSetters = true
    )
    private TransactionAccount rouRecognitionDebitAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "depreciationMethod", "placeholders" }, allowSetters = true)
    private AssetCategory assetCategory;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet serviceOutlet;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer mainDealer;

    @OneToMany(mappedBy = "leaseTemplate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "superintendentServiceOutlet",
            "mainDealer",
            "firstReportingPeriod",
            "lastReportingPeriod",
            "leaseContractDocument",
            "leaseContractCalculations",
            "leasePayments",
            "leaseTemplate",
        },
        allowSetters = true
    )
    private Set<IFRS16LeaseContract> leaseContracts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateTitle() {
        return this.templateTitle;
    }

    public LeaseTemplate templateTitle(String templateTitle) {
        this.setTemplateTitle(templateTitle);
        return this;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    public TransactionAccount getAssetAccount() {
        return this.assetAccount;
    }

    public void setAssetAccount(TransactionAccount transactionAccount) {
        this.assetAccount = transactionAccount;
    }

    public LeaseTemplate assetAccount(TransactionAccount transactionAccount) {
        this.setAssetAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getDepreciationAccount() {
        return this.depreciationAccount;
    }

    public void setDepreciationAccount(TransactionAccount transactionAccount) {
        this.depreciationAccount = transactionAccount;
    }

    public LeaseTemplate depreciationAccount(TransactionAccount transactionAccount) {
        this.setDepreciationAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getAccruedDepreciationAccount() {
        return this.accruedDepreciationAccount;
    }

    public void setAccruedDepreciationAccount(TransactionAccount transactionAccount) {
        this.accruedDepreciationAccount = transactionAccount;
    }

    public LeaseTemplate accruedDepreciationAccount(TransactionAccount transactionAccount) {
        this.setAccruedDepreciationAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getInterestPaidTransferDebitAccount() {
        return this.interestPaidTransferDebitAccount;
    }

    public void setInterestPaidTransferDebitAccount(TransactionAccount transactionAccount) {
        this.interestPaidTransferDebitAccount = transactionAccount;
    }

    public LeaseTemplate interestPaidTransferDebitAccount(TransactionAccount transactionAccount) {
        this.setInterestPaidTransferDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getInterestPaidTransferCreditAccount() {
        return this.interestPaidTransferCreditAccount;
    }

    public void setInterestPaidTransferCreditAccount(TransactionAccount transactionAccount) {
        this.interestPaidTransferCreditAccount = transactionAccount;
    }

    public LeaseTemplate interestPaidTransferCreditAccount(TransactionAccount transactionAccount) {
        this.setInterestPaidTransferCreditAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getInterestAccruedDebitAccount() {
        return this.interestAccruedDebitAccount;
    }

    public void setInterestAccruedDebitAccount(TransactionAccount transactionAccount) {
        this.interestAccruedDebitAccount = transactionAccount;
    }

    public LeaseTemplate interestAccruedDebitAccount(TransactionAccount transactionAccount) {
        this.setInterestAccruedDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getInterestAccruedCreditAccount() {
        return this.interestAccruedCreditAccount;
    }

    public void setInterestAccruedCreditAccount(TransactionAccount transactionAccount) {
        this.interestAccruedCreditAccount = transactionAccount;
    }

    public LeaseTemplate interestAccruedCreditAccount(TransactionAccount transactionAccount) {
        this.setInterestAccruedCreditAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getLeaseRecognitionDebitAccount() {
        return this.leaseRecognitionDebitAccount;
    }

    public void setLeaseRecognitionDebitAccount(TransactionAccount transactionAccount) {
        this.leaseRecognitionDebitAccount = transactionAccount;
    }

    public LeaseTemplate leaseRecognitionDebitAccount(TransactionAccount transactionAccount) {
        this.setLeaseRecognitionDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getLeaseRecognitionCreditAccount() {
        return this.leaseRecognitionCreditAccount;
    }

    public void setLeaseRecognitionCreditAccount(TransactionAccount transactionAccount) {
        this.leaseRecognitionCreditAccount = transactionAccount;
    }

    public LeaseTemplate leaseRecognitionCreditAccount(TransactionAccount transactionAccount) {
        this.setLeaseRecognitionCreditAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getLeaseRepaymentDebitAccount() {
        return this.leaseRepaymentDebitAccount;
    }

    public void setLeaseRepaymentDebitAccount(TransactionAccount transactionAccount) {
        this.leaseRepaymentDebitAccount = transactionAccount;
    }

    public LeaseTemplate leaseRepaymentDebitAccount(TransactionAccount transactionAccount) {
        this.setLeaseRepaymentDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getLeaseRepaymentCreditAccount() {
        return this.leaseRepaymentCreditAccount;
    }

    public void setLeaseRepaymentCreditAccount(TransactionAccount transactionAccount) {
        this.leaseRepaymentCreditAccount = transactionAccount;
    }

    public LeaseTemplate leaseRepaymentCreditAccount(TransactionAccount transactionAccount) {
        this.setLeaseRepaymentCreditAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getRouRecognitionCreditAccount() {
        return this.rouRecognitionCreditAccount;
    }

    public void setRouRecognitionCreditAccount(TransactionAccount transactionAccount) {
        this.rouRecognitionCreditAccount = transactionAccount;
    }

    public LeaseTemplate rouRecognitionCreditAccount(TransactionAccount transactionAccount) {
        this.setRouRecognitionCreditAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getRouRecognitionDebitAccount() {
        return this.rouRecognitionDebitAccount;
    }

    public void setRouRecognitionDebitAccount(TransactionAccount transactionAccount) {
        this.rouRecognitionDebitAccount = transactionAccount;
    }

    public LeaseTemplate rouRecognitionDebitAccount(TransactionAccount transactionAccount) {
        this.setRouRecognitionDebitAccount(transactionAccount);
        return this;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public LeaseTemplate assetCategory(AssetCategory assetCategory) {
        this.setAssetCategory(assetCategory);
        return this;
    }

    public ServiceOutlet getServiceOutlet() {
        return this.serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public LeaseTemplate serviceOutlet(ServiceOutlet serviceOutlet) {
        this.setServiceOutlet(serviceOutlet);
        return this;
    }

    public Dealer getMainDealer() {
        return this.mainDealer;
    }

    public void setMainDealer(Dealer dealer) {
        this.mainDealer = dealer;
    }

    public LeaseTemplate mainDealer(Dealer dealer) {
        this.setMainDealer(dealer);
        return this;
    }

    public Set<IFRS16LeaseContract> getLeaseContracts() {
        return this.leaseContracts;
    }

    public void setLeaseContracts(Set<IFRS16LeaseContract> iFRS16LeaseContracts) {
        if (this.leaseContracts != null) {
            this.leaseContracts.forEach(i -> i.setLeaseTemplate(null));
        }
        if (iFRS16LeaseContracts != null) {
            iFRS16LeaseContracts.forEach(i -> i.setLeaseTemplate(this));
        }
        this.leaseContracts = iFRS16LeaseContracts;
    }

    public LeaseTemplate leaseContracts(Set<IFRS16LeaseContract> iFRS16LeaseContracts) {
        this.setLeaseContracts(iFRS16LeaseContracts);
        return this;
    }

    public LeaseTemplate addLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContracts.add(iFRS16LeaseContract);
        iFRS16LeaseContract.setLeaseTemplate(this);
        return this;
    }

    public LeaseTemplate removeLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContracts.remove(iFRS16LeaseContract);
        iFRS16LeaseContract.setLeaseTemplate(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseTemplate)) {
            return false;
        }
        return id != null && id.equals(((LeaseTemplate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseTemplate{" +
            "id=" + getId() +
            ", templateTitle='" + getTemplateTitle() + "'" +
            "}";
    }
}
