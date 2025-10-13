package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.enumeration.AccountSubTypes;
import io.github.erp.domain.enumeration.AccountTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A TransactionAccount.
 */
@Entity
@Table(name = "transaction_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transactionaccount-" + "#{ T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM')) }")
public class TransactionAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "account_number", nullable = false, unique = true)
    @Field(type = FieldType.Keyword)
    private String accountNumber;

    @NotNull
    @Column(name = "account_name", nullable = false)
    @Field(type = FieldType.Keyword)
    private String accountName;

    @Lob
    @Column(name = "notes")
    @Field(type = FieldType.Binary, index = false)
    private byte[] notes;

    @Column(name = "notes_content_type")
    @Field(type = FieldType.Text, index = false)
    private String notesContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    @Field(type = FieldType.Text, index = false)
    private AccountTypes accountType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_sub_type", nullable = false)
    @Field(type = FieldType.Text, index = false)
    private AccountSubTypes accountSubType;

    @Column(name = "dummy_account")
    @Field(type = FieldType.Boolean, index = false)
    private Boolean dummyAccount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private TransactionAccountLedger accountLedger;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders", "accountLedger" }, allowSetters = true)
    private TransactionAccountCategory accountCategory;

    @ManyToMany
    @JoinTable(
        name = "rel_transaction_account__placeholder",
        joinColumns = @JoinColumn(name = "transaction_account_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet serviceOutlet;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne(optional = false)
    @NotNull
    private ReportingEntity institution;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public TransactionAccount accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public TransactionAccount accountName(String accountName) {
        this.setAccountName(accountName);
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public byte[] getNotes() {
        return this.notes;
    }

    public TransactionAccount notes(byte[] notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(byte[] notes) {
        this.notes = notes;
    }

    public String getNotesContentType() {
        return this.notesContentType;
    }

    public TransactionAccount notesContentType(String notesContentType) {
        this.notesContentType = notesContentType;
        return this;
    }

    public void setNotesContentType(String notesContentType) {
        this.notesContentType = notesContentType;
    }

    public AccountTypes getAccountType() {
        return this.accountType;
    }

    public TransactionAccount accountType(AccountTypes accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(AccountTypes accountType) {
        this.accountType = accountType;
    }

    public AccountSubTypes getAccountSubType() {
        return this.accountSubType;
    }

    public TransactionAccount accountSubType(AccountSubTypes accountSubType) {
        this.setAccountSubType(accountSubType);
        return this;
    }

    public void setAccountSubType(AccountSubTypes accountSubType) {
        this.accountSubType = accountSubType;
    }

    public Boolean getDummyAccount() {
        return this.dummyAccount;
    }

    public TransactionAccount dummyAccount(Boolean dummyAccount) {
        this.setDummyAccount(dummyAccount);
        return this;
    }

    public void setDummyAccount(Boolean dummyAccount) {
        this.dummyAccount = dummyAccount;
    }

    public TransactionAccountLedger getAccountLedger() {
        return this.accountLedger;
    }

    public void setAccountLedger(TransactionAccountLedger transactionAccountLedger) {
        this.accountLedger = transactionAccountLedger;
    }

    public TransactionAccount accountLedger(TransactionAccountLedger transactionAccountLedger) {
        this.setAccountLedger(transactionAccountLedger);
        return this;
    }

    public TransactionAccountCategory getAccountCategory() {
        return this.accountCategory;
    }

    public void setAccountCategory(TransactionAccountCategory transactionAccountCategory) {
        this.accountCategory = transactionAccountCategory;
    }

    public TransactionAccount accountCategory(TransactionAccountCategory transactionAccountCategory) {
        this.setAccountCategory(transactionAccountCategory);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public TransactionAccount placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public TransactionAccount addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public TransactionAccount removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public ServiceOutlet getServiceOutlet() {
        return this.serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public TransactionAccount serviceOutlet(ServiceOutlet serviceOutlet) {
        this.setServiceOutlet(serviceOutlet);
        return this;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public TransactionAccount settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public ReportingEntity getInstitution() {
        return this.institution;
    }

    public void setInstitution(ReportingEntity reportingEntity) {
        this.institution = reportingEntity;
    }

    public TransactionAccount institution(ReportingEntity reportingEntity) {
        this.setInstitution(reportingEntity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccount)) {
            return false;
        }
        return id != null && id.equals(((TransactionAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccount{" +
            "id=" + getId() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", notes='" + getNotes() + "'" +
            ", notesContentType='" + getNotesContentType() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", accountSubType='" + getAccountSubType() + "'" +
            ", dummyAccount='" + getDummyAccount() + "'" +
            "}";
    }
}
