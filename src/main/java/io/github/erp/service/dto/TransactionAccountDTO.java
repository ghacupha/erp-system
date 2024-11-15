package io.github.erp.service.dto;

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

import io.github.erp.domain.enumeration.AccountSubTypes;
import io.github.erp.domain.enumeration.AccountTypes;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.TransactionAccount} entity.
 */
public class TransactionAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String accountNumber;

    @NotNull
    private String accountName;

    @Lob
    private byte[] notes;

    private String notesContentType;

    @NotNull
    private AccountTypes accountType;

    @NotNull
    private AccountSubTypes accountSubType;

    private Boolean dummyAccount;

    private TransactionAccountLedgerDTO accountLedger;

    private TransactionAccountCategoryDTO accountCategory;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private ServiceOutletDTO serviceOutlet;

    private SettlementCurrencyDTO settlementCurrency;

    private ReportingEntityDTO institution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public byte[] getNotes() {
        return notes;
    }

    public void setNotes(byte[] notes) {
        this.notes = notes;
    }

    public String getNotesContentType() {
        return notesContentType;
    }

    public void setNotesContentType(String notesContentType) {
        this.notesContentType = notesContentType;
    }

    public AccountTypes getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypes accountType) {
        this.accountType = accountType;
    }

    public AccountSubTypes getAccountSubType() {
        return accountSubType;
    }

    public void setAccountSubType(AccountSubTypes accountSubType) {
        this.accountSubType = accountSubType;
    }

    public Boolean getDummyAccount() {
        return dummyAccount;
    }

    public void setDummyAccount(Boolean dummyAccount) {
        this.dummyAccount = dummyAccount;
    }

    public TransactionAccountLedgerDTO getAccountLedger() {
        return accountLedger;
    }

    public void setAccountLedger(TransactionAccountLedgerDTO accountLedger) {
        this.accountLedger = accountLedger;
    }

    public TransactionAccountCategoryDTO getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(TransactionAccountCategoryDTO accountCategory) {
        this.accountCategory = accountCategory;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public ServiceOutletDTO getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutletDTO serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public ReportingEntityDTO getInstitution() {
        return institution;
    }

    public void setInstitution(ReportingEntityDTO institution) {
        this.institution = institution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionAccountDTO)) {
            return false;
        }

        TransactionAccountDTO transactionAccountDTO = (TransactionAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionAccountDTO{" +
            "id=" + getId() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", notes='" + getNotes() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", accountSubType='" + getAccountSubType() + "'" +
            ", dummyAccount='" + getDummyAccount() + "'" +
            ", accountLedger=" + getAccountLedger() +
            ", accountCategory=" + getAccountCategory() +
            ", placeholders=" + getPlaceholders() +
            ", serviceOutlet=" + getServiceOutlet() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", institution=" + getInstitution() +
            "}";
    }
}
