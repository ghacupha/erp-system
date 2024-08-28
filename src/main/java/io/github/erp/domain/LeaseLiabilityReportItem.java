package io.github.erp.domain;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
 * A LeaseLiabilityReportItem.
 */
@Entity
@Table(name = "lease_liability_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaseliabilityreportitem")
public class LeaseLiabilityReportItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "lease_title")
    private String leaseTitle;

    @Column(name = "liability_account_number")
    private String liabilityAccountNumber;

    @Column(name = "liability_amount", precision = 21, scale = 2)
    private BigDecimal liabilityAmount;

    @Column(name = "interest_payable_account_number")
    private String interestPayableAccountNumber;

    @Column(name = "interest_payable_amount", precision = 21, scale = 2)
    private BigDecimal interestPayableAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseLiabilityReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public LeaseLiabilityReportItem bookingId(String bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return this.leaseTitle;
    }

    public LeaseLiabilityReportItem leaseTitle(String leaseTitle) {
        this.setLeaseTitle(leaseTitle);
        return this;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getLiabilityAccountNumber() {
        return this.liabilityAccountNumber;
    }

    public LeaseLiabilityReportItem liabilityAccountNumber(String liabilityAccountNumber) {
        this.setLiabilityAccountNumber(liabilityAccountNumber);
        return this;
    }

    public void setLiabilityAccountNumber(String liabilityAccountNumber) {
        this.liabilityAccountNumber = liabilityAccountNumber;
    }

    public BigDecimal getLiabilityAmount() {
        return this.liabilityAmount;
    }

    public LeaseLiabilityReportItem liabilityAmount(BigDecimal liabilityAmount) {
        this.setLiabilityAmount(liabilityAmount);
        return this;
    }

    public void setLiabilityAmount(BigDecimal liabilityAmount) {
        this.liabilityAmount = liabilityAmount;
    }

    public String getInterestPayableAccountNumber() {
        return this.interestPayableAccountNumber;
    }

    public LeaseLiabilityReportItem interestPayableAccountNumber(String interestPayableAccountNumber) {
        this.setInterestPayableAccountNumber(interestPayableAccountNumber);
        return this;
    }

    public void setInterestPayableAccountNumber(String interestPayableAccountNumber) {
        this.interestPayableAccountNumber = interestPayableAccountNumber;
    }

    public BigDecimal getInterestPayableAmount() {
        return this.interestPayableAmount;
    }

    public LeaseLiabilityReportItem interestPayableAmount(BigDecimal interestPayableAmount) {
        this.setInterestPayableAmount(interestPayableAmount);
        return this;
    }

    public void setInterestPayableAmount(BigDecimal interestPayableAmount) {
        this.interestPayableAmount = interestPayableAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityReportItem)) {
            return false;
        }
        return id != null && id.equals(((LeaseLiabilityReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityReportItem{" +
            "id=" + getId() +
            ", bookingId='" + getBookingId() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", liabilityAccountNumber='" + getLiabilityAccountNumber() + "'" +
            ", liabilityAmount=" + getLiabilityAmount() +
            ", interestPayableAccountNumber='" + getInterestPayableAccountNumber() + "'" +
            ", interestPayableAmount=" + getInterestPayableAmount() +
            "}";
    }
}
