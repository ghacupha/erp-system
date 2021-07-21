package io.github.erp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link io.github.erp.domain.Dealer} entity.
 */
public class DealerDTO implements Serializable {
    
    private Long id;

    
    private String dealerName;

    private String taxNumber;

    private String postalAddress;

    private String physicalAddress;

    private String accountName;

    private String accountNumber;

    private String bankersName;

    private String bankersBranch;

    private String bankersSwiftCode;

    private Set<PaymentDTO> payments = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankersName() {
        return bankersName;
    }

    public void setBankersName(String bankersName) {
        this.bankersName = bankersName;
    }

    public String getBankersBranch() {
        return bankersBranch;
    }

    public void setBankersBranch(String bankersBranch) {
        this.bankersBranch = bankersBranch;
    }

    public String getBankersSwiftCode() {
        return bankersSwiftCode;
    }

    public void setBankersSwiftCode(String bankersSwiftCode) {
        this.bankersSwiftCode = bankersSwiftCode;
    }

    public Set<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(Set<PaymentDTO> payments) {
        this.payments = payments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealerDTO)) {
            return false;
        }

        return id != null && id.equals(((DealerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DealerDTO{" +
            "id=" + getId() +
            ", dealerName='" + getDealerName() + "'" +
            ", taxNumber='" + getTaxNumber() + "'" +
            ", postalAddress='" + getPostalAddress() + "'" +
            ", physicalAddress='" + getPhysicalAddress() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", bankersName='" + getBankersName() + "'" +
            ", bankersBranch='" + getBankersBranch() + "'" +
            ", bankersSwiftCode='" + getBankersSwiftCode() + "'" +
            ", payments='" + getPayments() + "'" +
            "}";
    }
}
