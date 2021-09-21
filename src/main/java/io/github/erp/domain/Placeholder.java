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
 * A Placeholder.
 */
@Entity
@Table(name = "placeholder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "placeholder")
public class Placeholder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "token", unique = true)
    private String token;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "containingPlaceholder",
            "dealers",
            "fileTypes",
            "fileUploads",
            "fixedAssetAcquisitions",
            "fixedAssetDepreciations",
            "fixedAssetNetBookValues",
            "invoices",
            "messageTokens",
            "payments",
            "paymentCalculations",
            "paymentRequisitions",
            "paymentCategories",
            "taxReferences",
            "taxRules",
        },
        allowSetters = true
    )
    private Placeholder containingPlaceholder;

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealerGroup", "payments", "paymentRequisitions", "placeholders" },
        allowSetters = true
    )
    private Set<Dealer> dealers = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<FileType> fileTypes = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<FileUpload> fileUploads = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<FixedAssetAcquisition> fixedAssetAcquisitions = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<FixedAssetDepreciation> fixedAssetDepreciations = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<FixedAssetNetBookValue> fixedAssetNetBookValues = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "payment", "dealer", "placeholders" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<MessageToken> messageTokens = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "ownedInvoices", "dealers", "paymentCategory", "taxRule", "paymentCalculation", "placeholders", "paymentGroup",
        },
        allowSetters = true
    )
    private Set<Payment> payments = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "payment", "paymentCategory", "placeholders" }, allowSetters = true)
    private Set<PaymentCalculation> paymentCalculations = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealer", "placeholders" }, allowSetters = true)
    private Set<PaymentRequisition> paymentRequisitions = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "paymentCalculations", "payments", "placeholders" }, allowSetters = true)
    private Set<PaymentCategory> paymentCategories = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private Set<TaxReference> taxReferences = new HashSet<>();

    @ManyToMany(mappedBy = "placeholders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payments", "placeholders" }, allowSetters = true)
    private Set<TaxRule> taxRules = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Placeholder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Placeholder description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return this.token;
    }

    public Placeholder token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Placeholder getContainingPlaceholder() {
        return this.containingPlaceholder;
    }

    public void setContainingPlaceholder(Placeholder placeholder) {
        this.containingPlaceholder = placeholder;
    }

    public Placeholder containingPlaceholder(Placeholder placeholder) {
        this.setContainingPlaceholder(placeholder);
        return this;
    }

    public Set<Dealer> getDealers() {
        return this.dealers;
    }

    public void setDealers(Set<Dealer> dealers) {
        if (this.dealers != null) {
            this.dealers.forEach(i -> i.removePlaceholder(this));
        }
        if (dealers != null) {
            dealers.forEach(i -> i.addPlaceholder(this));
        }
        this.dealers = dealers;
    }

    public Placeholder dealers(Set<Dealer> dealers) {
        this.setDealers(dealers);
        return this;
    }

    public Placeholder addDealer(Dealer dealer) {
        this.dealers.add(dealer);
        dealer.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeDealer(Dealer dealer) {
        this.dealers.remove(dealer);
        dealer.getPlaceholders().remove(this);
        return this;
    }

    public Set<FileType> getFileTypes() {
        return this.fileTypes;
    }

    public void setFileTypes(Set<FileType> fileTypes) {
        if (this.fileTypes != null) {
            this.fileTypes.forEach(i -> i.removePlaceholder(this));
        }
        if (fileTypes != null) {
            fileTypes.forEach(i -> i.addPlaceholder(this));
        }
        this.fileTypes = fileTypes;
    }

    public Placeholder fileTypes(Set<FileType> fileTypes) {
        this.setFileTypes(fileTypes);
        return this;
    }

    public Placeholder addFileType(FileType fileType) {
        this.fileTypes.add(fileType);
        fileType.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeFileType(FileType fileType) {
        this.fileTypes.remove(fileType);
        fileType.getPlaceholders().remove(this);
        return this;
    }

    public Set<FileUpload> getFileUploads() {
        return this.fileUploads;
    }

    public void setFileUploads(Set<FileUpload> fileUploads) {
        if (this.fileUploads != null) {
            this.fileUploads.forEach(i -> i.removePlaceholder(this));
        }
        if (fileUploads != null) {
            fileUploads.forEach(i -> i.addPlaceholder(this));
        }
        this.fileUploads = fileUploads;
    }

    public Placeholder fileUploads(Set<FileUpload> fileUploads) {
        this.setFileUploads(fileUploads);
        return this;
    }

    public Placeholder addFileUpload(FileUpload fileUpload) {
        this.fileUploads.add(fileUpload);
        fileUpload.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeFileUpload(FileUpload fileUpload) {
        this.fileUploads.remove(fileUpload);
        fileUpload.getPlaceholders().remove(this);
        return this;
    }

    public Set<FixedAssetAcquisition> getFixedAssetAcquisitions() {
        return this.fixedAssetAcquisitions;
    }

    public void setFixedAssetAcquisitions(Set<FixedAssetAcquisition> fixedAssetAcquisitions) {
        if (this.fixedAssetAcquisitions != null) {
            this.fixedAssetAcquisitions.forEach(i -> i.removePlaceholder(this));
        }
        if (fixedAssetAcquisitions != null) {
            fixedAssetAcquisitions.forEach(i -> i.addPlaceholder(this));
        }
        this.fixedAssetAcquisitions = fixedAssetAcquisitions;
    }

    public Placeholder fixedAssetAcquisitions(Set<FixedAssetAcquisition> fixedAssetAcquisitions) {
        this.setFixedAssetAcquisitions(fixedAssetAcquisitions);
        return this;
    }

    public Placeholder addFixedAssetAcquisition(FixedAssetAcquisition fixedAssetAcquisition) {
        this.fixedAssetAcquisitions.add(fixedAssetAcquisition);
        fixedAssetAcquisition.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeFixedAssetAcquisition(FixedAssetAcquisition fixedAssetAcquisition) {
        this.fixedAssetAcquisitions.remove(fixedAssetAcquisition);
        fixedAssetAcquisition.getPlaceholders().remove(this);
        return this;
    }

    public Set<FixedAssetDepreciation> getFixedAssetDepreciations() {
        return this.fixedAssetDepreciations;
    }

    public void setFixedAssetDepreciations(Set<FixedAssetDepreciation> fixedAssetDepreciations) {
        if (this.fixedAssetDepreciations != null) {
            this.fixedAssetDepreciations.forEach(i -> i.removePlaceholder(this));
        }
        if (fixedAssetDepreciations != null) {
            fixedAssetDepreciations.forEach(i -> i.addPlaceholder(this));
        }
        this.fixedAssetDepreciations = fixedAssetDepreciations;
    }

    public Placeholder fixedAssetDepreciations(Set<FixedAssetDepreciation> fixedAssetDepreciations) {
        this.setFixedAssetDepreciations(fixedAssetDepreciations);
        return this;
    }

    public Placeholder addFixedAssetDepreciation(FixedAssetDepreciation fixedAssetDepreciation) {
        this.fixedAssetDepreciations.add(fixedAssetDepreciation);
        fixedAssetDepreciation.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeFixedAssetDepreciation(FixedAssetDepreciation fixedAssetDepreciation) {
        this.fixedAssetDepreciations.remove(fixedAssetDepreciation);
        fixedAssetDepreciation.getPlaceholders().remove(this);
        return this;
    }

    public Set<FixedAssetNetBookValue> getFixedAssetNetBookValues() {
        return this.fixedAssetNetBookValues;
    }

    public void setFixedAssetNetBookValues(Set<FixedAssetNetBookValue> fixedAssetNetBookValues) {
        if (this.fixedAssetNetBookValues != null) {
            this.fixedAssetNetBookValues.forEach(i -> i.removePlaceholder(this));
        }
        if (fixedAssetNetBookValues != null) {
            fixedAssetNetBookValues.forEach(i -> i.addPlaceholder(this));
        }
        this.fixedAssetNetBookValues = fixedAssetNetBookValues;
    }

    public Placeholder fixedAssetNetBookValues(Set<FixedAssetNetBookValue> fixedAssetNetBookValues) {
        this.setFixedAssetNetBookValues(fixedAssetNetBookValues);
        return this;
    }

    public Placeholder addFixedAssetNetBookValue(FixedAssetNetBookValue fixedAssetNetBookValue) {
        this.fixedAssetNetBookValues.add(fixedAssetNetBookValue);
        fixedAssetNetBookValue.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeFixedAssetNetBookValue(FixedAssetNetBookValue fixedAssetNetBookValue) {
        this.fixedAssetNetBookValues.remove(fixedAssetNetBookValue);
        fixedAssetNetBookValue.getPlaceholders().remove(this);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.removePlaceholder(this));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.addPlaceholder(this));
        }
        this.invoices = invoices;
    }

    public Placeholder invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Placeholder addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.getPlaceholders().remove(this);
        return this;
    }

    public Set<MessageToken> getMessageTokens() {
        return this.messageTokens;
    }

    public void setMessageTokens(Set<MessageToken> messageTokens) {
        if (this.messageTokens != null) {
            this.messageTokens.forEach(i -> i.removePlaceholder(this));
        }
        if (messageTokens != null) {
            messageTokens.forEach(i -> i.addPlaceholder(this));
        }
        this.messageTokens = messageTokens;
    }

    public Placeholder messageTokens(Set<MessageToken> messageTokens) {
        this.setMessageTokens(messageTokens);
        return this;
    }

    public Placeholder addMessageToken(MessageToken messageToken) {
        this.messageTokens.add(messageToken);
        messageToken.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeMessageToken(MessageToken messageToken) {
        this.messageTokens.remove(messageToken);
        messageToken.getPlaceholders().remove(this);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.removePlaceholder(this));
        }
        if (payments != null) {
            payments.forEach(i -> i.addPlaceholder(this));
        }
        this.payments = payments;
    }

    public Placeholder payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Placeholder addPayment(Payment payment) {
        this.payments.add(payment);
        payment.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.getPlaceholders().remove(this);
        return this;
    }

    public Set<PaymentCalculation> getPaymentCalculations() {
        return this.paymentCalculations;
    }

    public void setPaymentCalculations(Set<PaymentCalculation> paymentCalculations) {
        if (this.paymentCalculations != null) {
            this.paymentCalculations.forEach(i -> i.removePlaceholder(this));
        }
        if (paymentCalculations != null) {
            paymentCalculations.forEach(i -> i.addPlaceholder(this));
        }
        this.paymentCalculations = paymentCalculations;
    }

    public Placeholder paymentCalculations(Set<PaymentCalculation> paymentCalculations) {
        this.setPaymentCalculations(paymentCalculations);
        return this;
    }

    public Placeholder addPaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculations.add(paymentCalculation);
        paymentCalculation.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removePaymentCalculation(PaymentCalculation paymentCalculation) {
        this.paymentCalculations.remove(paymentCalculation);
        paymentCalculation.getPlaceholders().remove(this);
        return this;
    }

    public Set<PaymentRequisition> getPaymentRequisitions() {
        return this.paymentRequisitions;
    }

    public void setPaymentRequisitions(Set<PaymentRequisition> paymentRequisitions) {
        if (this.paymentRequisitions != null) {
            this.paymentRequisitions.forEach(i -> i.removePlaceholder(this));
        }
        if (paymentRequisitions != null) {
            paymentRequisitions.forEach(i -> i.addPlaceholder(this));
        }
        this.paymentRequisitions = paymentRequisitions;
    }

    public Placeholder paymentRequisitions(Set<PaymentRequisition> paymentRequisitions) {
        this.setPaymentRequisitions(paymentRequisitions);
        return this;
    }

    public Placeholder addPaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisitions.add(paymentRequisition);
        paymentRequisition.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removePaymentRequisition(PaymentRequisition paymentRequisition) {
        this.paymentRequisitions.remove(paymentRequisition);
        paymentRequisition.getPlaceholders().remove(this);
        return this;
    }

    public Set<PaymentCategory> getPaymentCategories() {
        return this.paymentCategories;
    }

    public void setPaymentCategories(Set<PaymentCategory> paymentCategories) {
        if (this.paymentCategories != null) {
            this.paymentCategories.forEach(i -> i.removePlaceholder(this));
        }
        if (paymentCategories != null) {
            paymentCategories.forEach(i -> i.addPlaceholder(this));
        }
        this.paymentCategories = paymentCategories;
    }

    public Placeholder paymentCategories(Set<PaymentCategory> paymentCategories) {
        this.setPaymentCategories(paymentCategories);
        return this;
    }

    public Placeholder addPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategories.add(paymentCategory);
        paymentCategory.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removePaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategories.remove(paymentCategory);
        paymentCategory.getPlaceholders().remove(this);
        return this;
    }

    public Set<TaxReference> getTaxReferences() {
        return this.taxReferences;
    }

    public void setTaxReferences(Set<TaxReference> taxReferences) {
        if (this.taxReferences != null) {
            this.taxReferences.forEach(i -> i.removePlaceholder(this));
        }
        if (taxReferences != null) {
            taxReferences.forEach(i -> i.addPlaceholder(this));
        }
        this.taxReferences = taxReferences;
    }

    public Placeholder taxReferences(Set<TaxReference> taxReferences) {
        this.setTaxReferences(taxReferences);
        return this;
    }

    public Placeholder addTaxReference(TaxReference taxReference) {
        this.taxReferences.add(taxReference);
        taxReference.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeTaxReference(TaxReference taxReference) {
        this.taxReferences.remove(taxReference);
        taxReference.getPlaceholders().remove(this);
        return this;
    }

    public Set<TaxRule> getTaxRules() {
        return this.taxRules;
    }

    public void setTaxRules(Set<TaxRule> taxRules) {
        if (this.taxRules != null) {
            this.taxRules.forEach(i -> i.removePlaceholder(this));
        }
        if (taxRules != null) {
            taxRules.forEach(i -> i.addPlaceholder(this));
        }
        this.taxRules = taxRules;
    }

    public Placeholder taxRules(Set<TaxRule> taxRules) {
        this.setTaxRules(taxRules);
        return this;
    }

    public Placeholder addTaxRule(TaxRule taxRule) {
        this.taxRules.add(taxRule);
        taxRule.getPlaceholders().add(this);
        return this;
    }

    public Placeholder removeTaxRule(TaxRule taxRule) {
        this.taxRules.remove(taxRule);
        taxRule.getPlaceholders().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Placeholder)) {
            return false;
        }
        return id != null && id.equals(((Placeholder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Placeholder{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", token='" + getToken() + "'" +
            "}";
    }
}
