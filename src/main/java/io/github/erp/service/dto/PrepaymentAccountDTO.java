package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

    private SettlementCurrencyDTO settlementCurrency;

    private SettlementDTO prepaymentTransaction;

    private ServiceOutletDTO serviceOutlet;

    private DealerDTO dealer;

    private TransactionAccountDTO debitAccount;

    private TransactionAccountDTO transferAccount;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

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
            ", settlementCurrency=" + getSettlementCurrency() +
            ", prepaymentTransaction=" + getPrepaymentTransaction() +
            ", serviceOutlet=" + getServiceOutlet() +
            ", dealer=" + getDealer() +
            ", debitAccount=" + getDebitAccount() +
            ", transferAccount=" + getTransferAccount() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
