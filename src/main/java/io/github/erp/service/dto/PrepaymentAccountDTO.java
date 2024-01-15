package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentAccount} entity.
 */
public class PrepaymentAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String catalogueNumber;

    @NotNull
    private String particulars;

    @Lob
    private String notes;

    private BigDecimal prepaymentAmount;

    private UUID prepaymentGuid;

    private SettlementCurrencyDTO settlementCurrency;

    private SettlementDTO prepaymentTransaction;

    private ServiceOutletDTO serviceOutlet;

    private DealerDTO dealer;

    private TransactionAccountDTO debitAccount;

    private TransactionAccountDTO transferAccount;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<UniversallyUniqueMappingDTO> generalParameters = new HashSet<>();

    private Set<PrepaymentMappingDTO> prepaymentParameters = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogueNumber() {
        return catalogueNumber;
    }

    public void setCatalogueNumber(String catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getPrepaymentAmount() {
        return prepaymentAmount;
    }

    public void setPrepaymentAmount(BigDecimal prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public UUID getPrepaymentGuid() {
        return prepaymentGuid;
    }

    public void setPrepaymentGuid(UUID prepaymentGuid) {
        this.prepaymentGuid = prepaymentGuid;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public SettlementDTO getPrepaymentTransaction() {
        return prepaymentTransaction;
    }

    public void setPrepaymentTransaction(SettlementDTO prepaymentTransaction) {
        this.prepaymentTransaction = prepaymentTransaction;
    }

    public ServiceOutletDTO getServiceOutlet() {
        return serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutletDTO serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public DealerDTO getDealer() {
        return dealer;
    }

    public void setDealer(DealerDTO dealer) {
        this.dealer = dealer;
    }

    public TransactionAccountDTO getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(TransactionAccountDTO debitAccount) {
        this.debitAccount = debitAccount;
    }

    public TransactionAccountDTO getTransferAccount() {
        return transferAccount;
    }

    public void setTransferAccount(TransactionAccountDTO transferAccount) {
        this.transferAccount = transferAccount;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<UniversallyUniqueMappingDTO> getGeneralParameters() {
        return generalParameters;
    }

    public void setGeneralParameters(Set<UniversallyUniqueMappingDTO> generalParameters) {
        this.generalParameters = generalParameters;
    }

    public Set<PrepaymentMappingDTO> getPrepaymentParameters() {
        return prepaymentParameters;
    }

    public void setPrepaymentParameters(Set<PrepaymentMappingDTO> prepaymentParameters) {
        this.prepaymentParameters = prepaymentParameters;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentAccountDTO)) {
            return false;
        }

        PrepaymentAccountDTO prepaymentAccountDTO = (PrepaymentAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prepaymentAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAccountDTO{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", notes='" + getNotes() + "'" +
            ", prepaymentAmount=" + getPrepaymentAmount() +
            ", prepaymentGuid='" + getPrepaymentGuid() + "'" +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", prepaymentTransaction=" + getPrepaymentTransaction() +
            ", serviceOutlet=" + getServiceOutlet() +
            ", dealer=" + getDealer() +
            ", debitAccount=" + getDebitAccount() +
            ", transferAccount=" + getTransferAccount() +
            ", placeholders=" + getPlaceholders() +
            ", generalParameters=" + getGeneralParameters() +
            ", prepaymentParameters=" + getPrepaymentParameters() +
            ", businessDocuments=" + getBusinessDocuments() +
            "}";
    }
}
