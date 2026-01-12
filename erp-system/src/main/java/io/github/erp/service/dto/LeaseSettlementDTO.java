package io.github.erp.service.dto;

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
import io.github.erp.domain.enumeration.ReconciliationStatusType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.LeaseSettlement} entity.
 */
public class LeaseSettlementDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate settlementDate;

    @NotNull
    private BigDecimal invoiceAmount;

    @NotNull
    private String invoiceReference;

    private BigDecimal varianceAmount;

    private String varianceReason;

    private UUID postingId;

    @NotNull
    private ReconciliationStatusType reconciliationStatus;

    private IFRS16LeaseContractDTO leaseContract;

    private LeaseRepaymentPeriodDTO period;

    private LeasePaymentDTO leasePayment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceReference() {
        return invoiceReference;
    }

    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }

    public BigDecimal getVarianceAmount() {
        return varianceAmount;
    }

    public void setVarianceAmount(BigDecimal varianceAmount) {
        this.varianceAmount = varianceAmount;
    }

    public String getVarianceReason() {
        return varianceReason;
    }

    public void setVarianceReason(String varianceReason) {
        this.varianceReason = varianceReason;
    }

    public UUID getPostingId() {
        return postingId;
    }

    public void setPostingId(UUID postingId) {
        this.postingId = postingId;
    }

    public ReconciliationStatusType getReconciliationStatus() {
        return reconciliationStatus;
    }

    public void setReconciliationStatus(ReconciliationStatusType reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }

    public IFRS16LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    public LeaseRepaymentPeriodDTO getPeriod() {
        return period;
    }

    public void setPeriod(LeaseRepaymentPeriodDTO period) {
        this.period = period;
    }

    public LeasePaymentDTO getLeasePayment() {
        return leasePayment;
    }

    public void setLeasePayment(LeasePaymentDTO leasePayment) {
        this.leasePayment = leasePayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseSettlementDTO)) {
            return false;
        }

        LeaseSettlementDTO leaseSettlementDTO = (LeaseSettlementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaseSettlementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseSettlementDTO{" +
            "id=" + getId() +
            ", settlementDate='" + getSettlementDate() + "'" +
            ", invoiceAmount=" + getInvoiceAmount() +
            ", invoiceReference='" + getInvoiceReference() + "'" +
            ", varianceAmount=" + getVarianceAmount() +
            ", varianceReason='" + getVarianceReason() + "'" +
            ", postingId='" + getPostingId() + "'" +
            ", reconciliationStatus='" + getReconciliationStatus() + "'" +
            ", leaseContract=" + getLeaseContract() +
            ", period=" + getPeriod() +
            ", leasePayment=" + getLeasePayment() +
            "}";
    }
}
