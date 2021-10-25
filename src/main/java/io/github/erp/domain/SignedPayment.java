package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.CurrencyTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SignedPayment.
 */
@Entity
@Table(name = "signed_payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "signedpayment")
public class SignedPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "transaction_number", nullable = false)
    private String transactionNumber;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_currency", nullable = false)
    private CurrencyTypes transactionCurrency;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "transaction_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_signed_payment__payment_label",
        joinColumns = @JoinColumn(name = "signed_payment_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealerGroup", "paymentRequisitions", "signedPayments", "placeholders" },
        allowSetters = true
    )
    private Dealer dealer;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paymentLabels", "paymentCalculations", "placeholders" }, allowSetters = true)
    private PaymentCategory paymentCategory;

    @ManyToMany
    @JoinTable(
        name = "rel_signed_payment__placeholder",
        joinColumns = @JoinColumn(name = "signed_payment_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealer", "paymentCategory", "placeholders", "signedPaymentGroup" },
        allowSetters = true
    )
    private SignedPayment signedPaymentGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SignedPayment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionNumber() {
        return this.transactionNumber;
    }

    public SignedPayment transactionNumber(String transactionNumber) {
        this.setTransactionNumber(transactionNumber);
        return this;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public SignedPayment transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public CurrencyTypes getTransactionCurrency() {
        return this.transactionCurrency;
    }

    public SignedPayment transactionCurrency(CurrencyTypes transactionCurrency) {
        this.setTransactionCurrency(transactionCurrency);
        return this;
    }

    public void setTransactionCurrency(CurrencyTypes transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public BigDecimal getTransactionAmount() {
        return this.transactionAmount;
    }

    public SignedPayment transactionAmount(BigDecimal transactionAmount) {
        this.setTransactionAmount(transactionAmount);
        return this;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public SignedPayment fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public SignedPayment compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public SignedPayment paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public SignedPayment addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        return this;
    }

    public SignedPayment removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        return this;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public SignedPayment dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    public PaymentCategory getPaymentCategory() {
        return this.paymentCategory;
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public SignedPayment paymentCategory(PaymentCategory paymentCategory) {
        this.setPaymentCategory(paymentCategory);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public SignedPayment placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public SignedPayment addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public SignedPayment removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public SignedPayment getSignedPaymentGroup() {
        return this.signedPaymentGroup;
    }

    public void setSignedPaymentGroup(SignedPayment signedPayment) {
        this.signedPaymentGroup = signedPayment;
    }

    public SignedPayment signedPaymentGroup(SignedPayment signedPayment) {
        this.setSignedPaymentGroup(signedPayment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignedPayment)) {
            return false;
        }
        return id != null && id.equals(((SignedPayment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignedPayment{" +
            "id=" + getId() +
            ", transactionNumber='" + getTransactionNumber() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", transactionCurrency='" + getTransactionCurrency() + "'" +
            ", transactionAmount=" + getTransactionAmount() +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
