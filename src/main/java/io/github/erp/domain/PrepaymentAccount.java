package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A PrepaymentAccount.
 */
@Entity
@Table(name = "prepayment_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prepaymentaccount")
public class PrepaymentAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "catalogue_number", nullable = false, unique = true)
    private String catalogueNumber;

    @NotNull
    @Column(name = "particulars", nullable = false)
    private String particulars;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "notes")
    private String notes;

    @Column(name = "prepayment_amount", precision = 21, scale = 2)
    private BigDecimal prepaymentAmount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "placeholders",
            "settlementCurrency",
            "paymentLabels",
            "paymentCategory",
            "groupSettlement",
            "biller",
            "paymentInvoices",
            "signatories",
        },
        allowSetters = true
    )
    private Settlement prepaymentTransaction;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet serviceOutlet;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer dealer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount debitAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentAccount", "placeholders" }, allowSetters = true)
    private TransactionAccount transferAccount;

    @ManyToMany
    @JoinTable(
        name = "rel_prepayment_account__placeholder",
        joinColumns = @JoinColumn(name = "prepayment_account_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrepaymentAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogueNumber() {
        return this.catalogueNumber;
    }

    public PrepaymentAccount catalogueNumber(String catalogueNumber) {
        this.setCatalogueNumber(catalogueNumber);
        return this;
    }

    public void setCatalogueNumber(String catalogueNumber) {
        this.catalogueNumber = catalogueNumber;
    }

    public String getParticulars() {
        return this.particulars;
    }

    public PrepaymentAccount particulars(String particulars) {
        this.setParticulars(particulars);
        return this;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getNotes() {
        return this.notes;
    }

    public PrepaymentAccount notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getPrepaymentAmount() {
        return this.prepaymentAmount;
    }

    public PrepaymentAccount prepaymentAmount(BigDecimal prepaymentAmount) {
        this.setPrepaymentAmount(prepaymentAmount);
        return this;
    }

    public void setPrepaymentAmount(BigDecimal prepaymentAmount) {
        this.prepaymentAmount = prepaymentAmount;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public PrepaymentAccount settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public Settlement getPrepaymentTransaction() {
        return this.prepaymentTransaction;
    }

    public void setPrepaymentTransaction(Settlement settlement) {
        this.prepaymentTransaction = settlement;
    }

    public PrepaymentAccount prepaymentTransaction(Settlement settlement) {
        this.setPrepaymentTransaction(settlement);
        return this;
    }

    public ServiceOutlet getServiceOutlet() {
        return this.serviceOutlet;
    }

    public void setServiceOutlet(ServiceOutlet serviceOutlet) {
        this.serviceOutlet = serviceOutlet;
    }

    public PrepaymentAccount serviceOutlet(ServiceOutlet serviceOutlet) {
        this.setServiceOutlet(serviceOutlet);
        return this;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public PrepaymentAccount dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    public TransactionAccount getDebitAccount() {
        return this.debitAccount;
    }

    public void setDebitAccount(TransactionAccount transactionAccount) {
        this.debitAccount = transactionAccount;
    }

    public PrepaymentAccount debitAccount(TransactionAccount transactionAccount) {
        this.setDebitAccount(transactionAccount);
        return this;
    }

    public TransactionAccount getTransferAccount() {
        return this.transferAccount;
    }

    public void setTransferAccount(TransactionAccount transactionAccount) {
        this.transferAccount = transactionAccount;
    }

    public PrepaymentAccount transferAccount(TransactionAccount transactionAccount) {
        this.setTransferAccount(transactionAccount);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PrepaymentAccount placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PrepaymentAccount addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PrepaymentAccount removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentAccount)) {
            return false;
        }
        return id != null && id.equals(((PrepaymentAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentAccount{" +
            "id=" + getId() +
            ", catalogueNumber='" + getCatalogueNumber() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", notes='" + getNotes() + "'" +
            ", prepaymentAmount=" + getPrepaymentAmount() +
            "}";
    }
}
