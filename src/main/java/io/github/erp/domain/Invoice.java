package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.CurrencyTypes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "invoice_amount", precision = 21, scale = 2)
    private BigDecimal invoiceAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyTypes currency;

    @NotNull
    @DecimalMin(value = "1.00")
    @Column(name = "conversion_rate", nullable = false)
    private Double conversionRate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "ownedInvoices", "dealers", "paymentCategory", "taxRule", "paymentCalculation", "paymentRequisition" },
        allowSetters = true
    )
    private Payment payment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "payments", "paymentRequisitions" }, allowSetters = true)
    private Dealer dealer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice id(Long id) {
        this.id = id;
        return this;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public Invoice invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public Invoice invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceAmount() {
        return this.invoiceAmount;
    }

    public Invoice invoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public CurrencyTypes getCurrency() {
        return this.currency;
    }

    public Invoice currency(CurrencyTypes currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(CurrencyTypes currency) {
        this.currency = currency;
    }

    public Double getConversionRate() {
        return this.conversionRate;
    }

    public Invoice conversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
        return this;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public Invoice payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public Invoice dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceAmount=" + getInvoiceAmount() +
            ", currency='" + getCurrency() + "'" +
            ", conversionRate=" + getConversionRate() +
            "}";
    }
}
