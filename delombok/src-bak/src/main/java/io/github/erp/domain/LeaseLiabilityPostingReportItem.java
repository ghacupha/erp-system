package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
 * A LeaseLiabilityPostingReportItem.
 */
@Entity
@Table(name = "lease_liability_posting_report_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leaseliabilitypostingreportitem")
public class LeaseLiabilityPostingReportItem implements Serializable {

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

    @Column(name = "lease_description")
    private String leaseDescription;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "posting")
    private String posting;

    @Column(name = "posting_amount", precision = 21, scale = 2)
    private BigDecimal postingAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaseLiabilityPostingReportItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public LeaseLiabilityPostingReportItem bookingId(String bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return this.leaseTitle;
    }

    public LeaseLiabilityPostingReportItem leaseTitle(String leaseTitle) {
        this.setLeaseTitle(leaseTitle);
        return this;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getLeaseDescription() {
        return this.leaseDescription;
    }

    public LeaseLiabilityPostingReportItem leaseDescription(String leaseDescription) {
        this.setLeaseDescription(leaseDescription);
        return this;
    }

    public void setLeaseDescription(String leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public LeaseLiabilityPostingReportItem accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPosting() {
        return this.posting;
    }

    public LeaseLiabilityPostingReportItem posting(String posting) {
        this.setPosting(posting);
        return this;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public BigDecimal getPostingAmount() {
        return this.postingAmount;
    }

    public LeaseLiabilityPostingReportItem postingAmount(BigDecimal postingAmount) {
        this.setPostingAmount(postingAmount);
        return this;
    }

    public void setPostingAmount(BigDecimal postingAmount) {
        this.postingAmount = postingAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityPostingReportItem)) {
            return false;
        }
        return id != null && id.equals(((LeaseLiabilityPostingReportItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityPostingReportItem{" +
            "id=" + getId() +
            ", bookingId='" + getBookingId() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", leaseDescription='" + getLeaseDescription() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", posting='" + getPosting() + "'" +
            ", postingAmount=" + getPostingAmount() +
            "}";
    }
}
