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

    private LocalDate recognitionDate;

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

    public LocalDate getRecognitionDate() {
        return recognitionDate;
    }

    public void setRecognitionDate(LocalDate recognitionDate) {
        this.recognitionDate = recognitionDate;
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
            ", recognitionDate='" + getRecognitionDate() + "'" +
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
