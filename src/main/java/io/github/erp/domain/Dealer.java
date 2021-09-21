package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "dealer_name", nullable = false, unique = true)
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
    @JoinTable(
        name = "rel_dealer__payment_label",
        joinColumns = @JoinColumn(name = "dealer_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "paymentRequisitions", "placeholders" }, allowSetters = true)
    private Dealer dealerGroup;

    @OneToMany(mappedBy = "dealer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealer", "placeholders" }, allowSetters = true)
    private Set<PaymentRequisition> paymentRequisitions = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_dealer__placeholder",
        joinColumns = @JoinColumn(name = "dealer_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dealer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public Dealer dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getTaxNumber() {
        return this.taxNumber;
    }

    public Dealer taxNumber(String taxNumber) {
        this.setTaxNumber(taxNumber);
        return this;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getPostalAddress() {
        return this.postalAddress;
    }

    public Dealer postalAddress(String postalAddress) {
        this.setPostalAddress(postalAddress);
        return this;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPhysicalAddress() {
        return this.physicalAddress;
    }

    public Dealer physicalAddress(String physicalAddress) {
        this.setPhysicalAddress(physicalAddress);
        return this;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public Dealer accountName(String accountName) {
        this.setAccountName(accountName);
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public Dealer accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankersName() {
        return this.bankersName;
    }

    public Dealer bankersName(String bankersName) {
        this.setBankersName(bankersName);
        return this;
    }

    public void setBankersName(String bankersName) {
        this.bankersName = bankersName;
    }

    public String getBankersBranch() {
        return this.bankersBranch;
    }

    public Dealer bankersBranch(String bankersBranch) {
        this.setBankersBranch(bankersBranch);
        return this;
    }

    public void setBankersBranch(String bankersBranch) {
        this.bankersBranch = bankersBranch;
    }

    public String getBankersSwiftCode() {
        return this.bankersSwiftCode;
    }

    public Dealer bankersSwiftCode(String bankersSwiftCode) {
        this.setBankersSwiftCode(bankersSwiftCode);
        return this;
    }

    public void setBankersSwiftCode(String bankersSwiftCode) {
        this.bankersSwiftCode = bankersSwiftCode;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Dealer paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public Dealer addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        return this;
    }

    public Dealer removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        return this;
    }

    public Dealer getDealerGroup() {
        return this.dealerGroup;
    }

    public void setDealerGroup(Dealer dealer) {
        this.dealerGroup = dealer;
    }

    public Dealer dealerGroup(Dealer dealer) {
        this.setDealerGroup(dealer);
        return this;
    }

    public Set<PaymentRequisition> getPaymentRequisitions() {
        return this.paymentRequisitions;
    }

    public void setPaymentRequisitions(Set<PaymentRequisition> paymentRequisitions) {
        if (this.paymentRequisitions != null) {
            this.paymentRequisitions.forEach(i -> i.setDealer(null));
        }
        if (paymentRequisitions != null) {
            paymentRequisitions.forEach(i -> i.setDealer(this));
        }
        this.paymentRequisitions = paymentRequisitions;
    }

    public Dealer paymentRequisitions(Set<PaymentRequisition> paymentRequisitions) {
        this.setPaymentRequisitions(paymentRequisitions);
        return this;
    }

    public Dealer addPaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisitions.add(paymentRequisition);
        paymentRequisition.setDealer(this);
        return this;
    }

    public Dealer removePaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisitions.remove(paymentRequisition);
        paymentRequisition.setDealer(null);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Dealer placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public Dealer addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public Dealer removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
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
