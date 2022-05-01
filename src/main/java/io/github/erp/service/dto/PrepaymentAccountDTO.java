package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
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

    private SettlementCurrencyDTO settlementCurrency;

    private SettlementDTO prepaymentTransaction;

    private ServiceOutletDTO serviceOutlet;

    private DealerDTO dealer;

    private PlaceholderDTO placeholder;

    private TransactionAccountDTO debitAccount;

    private TransactionAccountDTO transferAccount;

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

    public PlaceholderDTO getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(PlaceholderDTO placeholder) {
        this.placeholder = placeholder;
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
            ", settlementCurrency=" + getSettlementCurrency() +
            ", prepaymentTransaction=" + getPrepaymentTransaction() +
            ", serviceOutlet=" + getServiceOutlet() +
            ", dealer=" + getDealer() +
            ", placeholder=" + getPlaceholder() +
            ", debitAccount=" + getDebitAccount() +
            ", transferAccount=" + getTransferAccount() +
            "}";
    }
}
