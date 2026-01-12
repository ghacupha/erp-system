package io.github.erp.domain;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.enumeration.ReconciliationStatusType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaseSettlement.
 */
@Entity
@Table(name = "lease_settlement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leasesettlement")
public class LeaseSettlement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "settlement_date", nullable = false)
    private LocalDate settlementDate;

    @NotNull
    @Column(name = "invoice_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal invoiceAmount;

    @NotNull
    @Column(name = "invoice_reference", nullable = false)
    private String invoiceReference;

    @Column(name = "variance_amount", precision = 21, scale = 2)
    private BigDecimal varianceAmount;

    @Column(name = "variance_reason")
    private String varianceReason;

    @Column(name = "posting_id")
    private UUID postingId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reconciliation_status", nullable = false)
    private ReconciliationStatusType reconciliationStatus = ReconciliationStatusType.PENDING;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "superintendentServiceOutlet",
            "mainDealer",
            "firstReportingPeriod",
            "lastReportingPeriod",
            "leaseContractDocument",
            "leaseContractCalculations",
            "leasePayments",
        },
        allowSetters = true
    )
    private IFRS16LeaseContract leaseContract;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalMonth" }, allowSetters = true)
    private LeaseRepaymentPeriod period;

    @ManyToOne
    @JsonIgnoreProperties(value = { "leasePaymentUpload", "leaseContract" }, allowSetters = true)
    private LeasePayment leasePayment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseSettlement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSettlementDate() {
        return this.settlementDate;
    }

    public LeaseSettlement settlementDate(LocalDate settlementDate) {
        this.setSettlementDate(settlementDate);
        return this;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getInvoiceAmount() {
        return this.invoiceAmount;
    }

    public LeaseSettlement invoiceAmount(BigDecimal invoiceAmount) {
        this.setInvoiceAmount(invoiceAmount);
        return this;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceReference() {
        return this.invoiceReference;
    }

    public LeaseSettlement invoiceReference(String invoiceReference) {
        this.setInvoiceReference(invoiceReference);
        return this;
    }

    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }

    public BigDecimal getVarianceAmount() {
        return this.varianceAmount;
    }

    public LeaseSettlement varianceAmount(BigDecimal varianceAmount) {
        this.setVarianceAmount(varianceAmount);
        return this;
    }

    public void setVarianceAmount(BigDecimal varianceAmount) {
        this.varianceAmount = varianceAmount;
    }

    public String getVarianceReason() {
        return this.varianceReason;
    }

    public LeaseSettlement varianceReason(String varianceReason) {
        this.setVarianceReason(varianceReason);
        return this;
    }

    public void setVarianceReason(String varianceReason) {
        this.varianceReason = varianceReason;
    }

    public UUID getPostingId() {
        return this.postingId;
    }

    public LeaseSettlement postingId(UUID postingId) {
        this.setPostingId(postingId);
        return this;
    }

    public void setPostingId(UUID postingId) {
        this.postingId = postingId;
    }

    public ReconciliationStatusType getReconciliationStatus() {
        return this.reconciliationStatus;
    }

    public LeaseSettlement reconciliationStatus(ReconciliationStatusType reconciliationStatus) {
        this.setReconciliationStatus(reconciliationStatus);
        return this;
    }

    public void setReconciliationStatus(ReconciliationStatusType reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return this.leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.leaseContract = iFRS16LeaseContract;
    }

    public LeaseSettlement leaseContract(IFRS16LeaseContract iFRS16LeaseContract) {
        this.setLeaseContract(iFRS16LeaseContract);
        return this;
    }

    public LeaseRepaymentPeriod getPeriod() {
        return this.period;
    }

    public void setPeriod(LeaseRepaymentPeriod leaseRepaymentPeriod) {
        this.period = leaseRepaymentPeriod;
    }

    public LeaseSettlement period(LeaseRepaymentPeriod leaseRepaymentPeriod) {
        this.setPeriod(leaseRepaymentPeriod);
        return this;
    }

    public LeasePayment getLeasePayment() {
        return this.leasePayment;
    }

    public void setLeasePayment(LeasePayment leasePayment) {
        this.leasePayment = leasePayment;
    }

    public LeaseSettlement leasePayment(LeasePayment leasePayment) {
        this.setLeasePayment(leasePayment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseSettlement)) {
            return false;
        }
        return id != null && id.equals(((LeaseSettlement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseSettlement{" +
            "id=" + getId() +
            ", settlementDate='" + getSettlementDate() + "'" +
            ", invoiceAmount=" + getInvoiceAmount() +
            ", invoiceReference='" + getInvoiceReference() + "'" +
            ", varianceAmount=" + getVarianceAmount() +
            ", varianceReason='" + getVarianceReason() + "'" +
            ", postingId='" + getPostingId() + "'" +
            ", reconciliationStatus='" + getReconciliationStatus() + "'" +
            "}";
    }
}
