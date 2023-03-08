package io.github.erp.domain;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.8.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Settlement.
 */
@Entity
@Table(name = "settlement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "settlement")
public class Settlement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    @Lob
    @Column(name = "calculation_file")
    private byte[] calculationFile;

    @Column(name = "calculation_file_content_type")
    private String calculationFileContentType;

    @Column(name = "file_upload_token")
    private String fileUploadToken;

    @Column(name = "compilation_token")
    private String compilationToken;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "remarks")
    private String remarks;

    @ManyToMany
    @JoinTable(
        name = "rel_settlement__placeholder",
        joinColumns = @JoinColumn(name = "settlement_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToMany
    @JoinTable(
        name = "rel_settlement__payment_label",
        joinColumns = @JoinColumn(name = "settlement_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "paymentCalculations", "placeholders" }, allowSetters = true)
    private PaymentCategory paymentCategory;

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
            "businessDocuments",
        },
        allowSetters = true
    )
    private Settlement groupSettlement;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer biller;

    @ManyToMany
    @JoinTable(
        name = "rel_settlement__payment_invoice",
        joinColumns = @JoinColumn(name = "settlement_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_invoice_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "purchaseOrders",
            "placeholders",
            "paymentLabels",
            "settlementCurrency",
            "biller",
            "deliveryNotes",
            "jobSheets",
            "businessDocuments",
        },
        allowSetters = true
    )
    private Set<PaymentInvoice> paymentInvoices = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement__signatories",
        joinColumns = @JoinColumn(name = "settlement_id"),
        inverseJoinColumns = @JoinColumn(name = "signatories_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Set<Dealer> signatories = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_settlement__business_document",
        joinColumns = @JoinColumn(name = "settlement_id"),
        inverseJoinColumns = @JoinColumn(name = "business_document_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private Set<BusinessDocument> businessDocuments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Settlement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return this.paymentNumber;
    }

    public Settlement paymentNumber(String paymentNumber) {
        this.setPaymentNumber(paymentNumber);
        return this;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public Settlement paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public Settlement paymentAmount(BigDecimal paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public Settlement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return this.notes;
    }

    public Settlement notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public byte[] getCalculationFile() {
        return this.calculationFile;
    }

    public Settlement calculationFile(byte[] calculationFile) {
        this.setCalculationFile(calculationFile);
        return this;
    }

    public void setCalculationFile(byte[] calculationFile) {
        this.calculationFile = calculationFile;
    }

    public String getCalculationFileContentType() {
        return this.calculationFileContentType;
    }

    public Settlement calculationFileContentType(String calculationFileContentType) {
        this.calculationFileContentType = calculationFileContentType;
        return this;
    }

    public void setCalculationFileContentType(String calculationFileContentType) {
        this.calculationFileContentType = calculationFileContentType;
    }

    public String getFileUploadToken() {
        return this.fileUploadToken;
    }

    public Settlement fileUploadToken(String fileUploadToken) {
        this.setFileUploadToken(fileUploadToken);
        return this;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return this.compilationToken;
    }

    public Settlement compilationToken(String compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Settlement remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Settlement placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public Settlement addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public Settlement removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    public SettlementCurrency getSettlementCurrency() {
        return this.settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public Settlement settlementCurrency(SettlementCurrency settlementCurrency) {
        this.setSettlementCurrency(settlementCurrency);
        return this;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return this.paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Settlement paymentLabels(Set<PaymentLabel> paymentLabels) {
        this.setPaymentLabels(paymentLabels);
        return this;
    }

    public Settlement addPaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.add(paymentLabel);
        return this;
    }

    public Settlement removePaymentLabel(PaymentLabel paymentLabel) {
        this.paymentLabels.remove(paymentLabel);
        return this;
    }

    public PaymentCategory getPaymentCategory() {
        return this.paymentCategory;
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public Settlement paymentCategory(PaymentCategory paymentCategory) {
        this.setPaymentCategory(paymentCategory);
        return this;
    }

    public Settlement getGroupSettlement() {
        return this.groupSettlement;
    }

    public void setGroupSettlement(Settlement settlement) {
        this.groupSettlement = settlement;
    }

    public Settlement groupSettlement(Settlement settlement) {
        this.setGroupSettlement(settlement);
        return this;
    }

    public Dealer getBiller() {
        return this.biller;
    }

    public void setBiller(Dealer dealer) {
        this.biller = dealer;
    }

    public Settlement biller(Dealer dealer) {
        this.setBiller(dealer);
        return this;
    }

    public Set<PaymentInvoice> getPaymentInvoices() {
        return this.paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public Settlement paymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.setPaymentInvoices(paymentInvoices);
        return this;
    }

    public Settlement addPaymentInvoice(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.add(paymentInvoice);
        return this;
    }

    public Settlement removePaymentInvoice(PaymentInvoice paymentInvoice) {
        this.paymentInvoices.remove(paymentInvoice);
        return this;
    }

    public Set<Dealer> getSignatories() {
        return this.signatories;
    }

    public void setSignatories(Set<Dealer> dealers) {
        this.signatories = dealers;
    }

    public Settlement signatories(Set<Dealer> dealers) {
        this.setSignatories(dealers);
        return this;
    }

    public Settlement addSignatories(Dealer dealer) {
        this.signatories.add(dealer);
        return this;
    }

    public Settlement removeSignatories(Dealer dealer) {
        this.signatories.remove(dealer);
        return this;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return this.businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    public Settlement businessDocuments(Set<BusinessDocument> businessDocuments) {
        this.setBusinessDocuments(businessDocuments);
        return this;
    }

    public Settlement addBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.add(businessDocument);
        return this;
    }

    public Settlement removeBusinessDocument(BusinessDocument businessDocument) {
        this.businessDocuments.remove(businessDocument);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Settlement)) {
            return false;
        }
        return id != null && id.equals(((Settlement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Settlement{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", calculationFile='" + getCalculationFile() + "'" +
            ", calculationFileContentType='" + getCalculationFileContentType() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
