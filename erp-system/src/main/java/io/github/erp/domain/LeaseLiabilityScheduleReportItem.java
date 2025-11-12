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
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaseLiabilityScheduleReportItem.
 */
@Entity
@Table(name = "lease_liability_schedule_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaseliabilityschedulereportitem")
public class LeaseLiabilityScheduleReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Column(name = "opening_balance", precision = 21, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "cash_payment", precision = 21, scale = 2)
    private BigDecimal cashPayment;

    @Column(name = "principal_payment", precision = 21, scale = 2)
    private BigDecimal principalPayment;

    @Column(name = "interest_payment", precision = 21, scale = 2)
    private BigDecimal interestPayment;

    @Column(name = "outstanding_balance", precision = 21, scale = 2)
    private BigDecimal outstandingBalance;

    @Column(name = "interest_payable_opening", precision = 21, scale = 2)
    private BigDecimal interestPayableOpening;

    @Column(name = "interest_accrued", precision = 21, scale = 2)
    private BigDecimal interestAccrued;

    @Column(name = "interest_payable_closing", precision = 21, scale = 2)
    private BigDecimal interestPayableClosing;

    @Column(name = "amortization_schedule_id")
    private Long amortizationScheduleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseLiabilityScheduleReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public LeaseLiabilityScheduleReportItem sequenceNumber(Integer sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public BigDecimal getOpeningBalance() {
        return this.openingBalance;
    }

    public LeaseLiabilityScheduleReportItem openingBalance(BigDecimal openingBalance) {
        this.setOpeningBalance(openingBalance);
        return this;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getCashPayment() {
        return this.cashPayment;
    }

    public LeaseLiabilityScheduleReportItem cashPayment(BigDecimal cashPayment) {
        this.setCashPayment(cashPayment);
        return this;
    }

    public void setCashPayment(BigDecimal cashPayment) {
        this.cashPayment = cashPayment;
    }

    public BigDecimal getPrincipalPayment() {
        return this.principalPayment;
    }

    public LeaseLiabilityScheduleReportItem principalPayment(BigDecimal principalPayment) {
        this.setPrincipalPayment(principalPayment);
        return this;
    }

    public void setPrincipalPayment(BigDecimal principalPayment) {
        this.principalPayment = principalPayment;
    }

    public BigDecimal getInterestPayment() {
        return this.interestPayment;
    }

    public LeaseLiabilityScheduleReportItem interestPayment(BigDecimal interestPayment) {
        this.setInterestPayment(interestPayment);
        return this;
    }

    public void setInterestPayment(BigDecimal interestPayment) {
        this.interestPayment = interestPayment;
    }

    public BigDecimal getOutstandingBalance() {
        return this.outstandingBalance;
    }

    public LeaseLiabilityScheduleReportItem outstandingBalance(BigDecimal outstandingBalance) {
        this.setOutstandingBalance(outstandingBalance);
        return this;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public BigDecimal getInterestPayableOpening() {
        return this.interestPayableOpening;
    }

    public LeaseLiabilityScheduleReportItem interestPayableOpening(BigDecimal interestPayableOpening) {
        this.setInterestPayableOpening(interestPayableOpening);
        return this;
    }

    public void setInterestPayableOpening(BigDecimal interestPayableOpening) {
        this.interestPayableOpening = interestPayableOpening;
    }

    public BigDecimal getInterestAccrued() {
        return this.interestAccrued;
    }

    public LeaseLiabilityScheduleReportItem interestAccrued(BigDecimal interestAccrued) {
        this.setInterestAccrued(interestAccrued);
        return this;
    }

    public void setInterestAccrued(BigDecimal interestAccrued) {
        this.interestAccrued = interestAccrued;
    }

    public BigDecimal getInterestPayableClosing() {
        return this.interestPayableClosing;
    }

    public LeaseLiabilityScheduleReportItem interestPayableClosing(BigDecimal interestPayableClosing) {
        this.setInterestPayableClosing(interestPayableClosing);
        return this;
    }

    public void setInterestPayableClosing(BigDecimal interestPayableClosing) {
        this.interestPayableClosing = interestPayableClosing;
    }

    public Long getAmortizationScheduleId() {
        return this.amortizationScheduleId;
    }

    public LeaseLiabilityScheduleReportItem amortizationScheduleId(Long amortizationScheduleId) {
        this.setAmortizationScheduleId(amortizationScheduleId);
        return this;
    }

    public void setAmortizationScheduleId(Long amortizationScheduleId) {
        this.amortizationScheduleId = amortizationScheduleId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityScheduleReportItem)) {
            return false;
        }
        return id != null && id.equals(((LeaseLiabilityScheduleReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityScheduleReportItem{" +
            "id=" + getId() +
            ", sequenceNumber=" + getSequenceNumber() +
            ", openingBalance=" + getOpeningBalance() +
            ", cashPayment=" + getCashPayment() +
            ", principalPayment=" + getPrincipalPayment() +
            ", interestPayment=" + getInterestPayment() +
            ", outstandingBalance=" + getOutstandingBalance() +
            ", interestPayableOpening=" + getInterestPayableOpening() +
            ", interestAccrued=" + getInterestAccrued() +
            ", interestPayableClosing=" + getInterestPayableClosing() +
            ", amortizationScheduleId=" + getAmortizationScheduleId() +
            "}";
    }
}
