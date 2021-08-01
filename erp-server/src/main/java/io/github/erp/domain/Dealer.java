package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Dealer.
 */
@Entity
@Table(name = "dealer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dealer")
public class Dealer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "dealer_name", unique = true)
    private String dealerName;

    @Column(name = "tax_number")
    private String taxNumber;

    @Column(name = "postal_address")
    private String postalAddress;

    @Column(name = "physical_address")
    private String physicalAddress;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bankers_name")
    private String bankersName;

    @Column(name = "bankers_branch")
    private String bankersBranch;

    @Column(name = "bankers_swift_code")
    private String bankersSwiftCode;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_dealer__payment",
        joinColumns = @JoinColumn(name = "dealer_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_id")
    )
    @JsonIgnoreProperties(
        value = { "ownedInvoices", "paymentRequisition", "dealers", "taxRule", "paymentCategory", "paymentCalculation" },
        allowSetters = true
    )
    private Set<Payment> payments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dealer id(Long id) {
        this.id = id;
        return this;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public Dealer dealerName(String dealerName) {
        this.dealerName = dealerName;
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getTaxNumber() {
        return this.taxNumber;
    }

    public Dealer taxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
        return this;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getPostalAddress() {
        return this.postalAddress;
    }

    public Dealer postalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
        return this;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPhysicalAddress() {
        return this.physicalAddress;
    }

    public Dealer physicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
        return this;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public Dealer accountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public Dealer accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankersName() {
        return this.bankersName;
    }

    public Dealer bankersName(String bankersName) {
        this.bankersName = bankersName;
        return this;
    }

    public void setBankersName(String bankersName) {
        this.bankersName = bankersName;
    }

    public String getBankersBranch() {
        return this.bankersBranch;
    }

    public Dealer bankersBranch(String bankersBranch) {
        this.bankersBranch = bankersBranch;
        return this;
    }

    public void setBankersBranch(String bankersBranch) {
        this.bankersBranch = bankersBranch;
    }

    public String getBankersSwiftCode() {
        return this.bankersSwiftCode;
    }

    public Dealer bankersSwiftCode(String bankersSwiftCode) {
        this.bankersSwiftCode = bankersSwiftCode;
        return this;
    }

    public void setBankersSwiftCode(String bankersSwiftCode) {
        this.bankersSwiftCode = bankersSwiftCode;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public Dealer payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Dealer addPayment(Payment payment) {
        this.payments.add(payment);
        payment.getDealers().add(this);
        return this;
    }

    public Dealer removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.getDealers().remove(this);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dealer)) {
            return false;
        }
        return id != null && id.equals(((Dealer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dealer{" +
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
            "}";
    }
}
