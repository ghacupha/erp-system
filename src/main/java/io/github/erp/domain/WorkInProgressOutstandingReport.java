package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
 * A WorkInProgressOutstandingReport.
 */
@Entity
@Table(name = "work_in_progress_outstanding_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "workinprogressoutstandingreport")
public class WorkInProgressOutstandingReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sequence_number")
    private String sequenceNumber;

    @Column(name = "particulars")
    private String particulars;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "iso_4217_code")
    private String iso4217Code;

    @Column(name = "instalment_amount", precision = 21, scale = 2)
    private BigDecimal instalmentAmount;

    @Column(name = "total_transfer_amount", precision = 21, scale = 2)
    private BigDecimal totalTransferAmount;

    @Column(name = "outstanding_amount", precision = 21, scale = 2)
    private BigDecimal outstandingAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WorkInProgressOutstandingReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return this.sequenceNumber;
    }

    public WorkInProgressOutstandingReport sequenceNumber(String sequenceNumber) {
        this.setSequenceNumber(sequenceNumber);
        return this;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getParticulars() {
        return this.particulars;
    }

    public WorkInProgressOutstandingReport particulars(String particulars) {
        this.setParticulars(particulars);
        return this;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public WorkInProgressOutstandingReport dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getIso4217Code() {
        return this.iso4217Code;
    }

    public WorkInProgressOutstandingReport iso4217Code(String iso4217Code) {
        this.setIso4217Code(iso4217Code);
        return this;
    }

    public void setIso4217Code(String iso4217Code) {
        this.iso4217Code = iso4217Code;
    }

    public BigDecimal getInstalmentAmount() {
        return this.instalmentAmount;
    }

    public WorkInProgressOutstandingReport instalmentAmount(BigDecimal instalmentAmount) {
        this.setInstalmentAmount(instalmentAmount);
        return this;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public BigDecimal getTotalTransferAmount() {
        return this.totalTransferAmount;
    }

    public WorkInProgressOutstandingReport totalTransferAmount(BigDecimal totalTransferAmount) {
        this.setTotalTransferAmount(totalTransferAmount);
        return this;
    }

    public void setTotalTransferAmount(BigDecimal totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return this.outstandingAmount;
    }

    public WorkInProgressOutstandingReport outstandingAmount(BigDecimal outstandingAmount) {
        this.setOutstandingAmount(outstandingAmount);
        return this;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressOutstandingReport)) {
            return false;
        }
        return id != null && id.equals(((WorkInProgressOutstandingReport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressOutstandingReport{" +
            "id=" + getId() +
            ", sequenceNumber='" + getSequenceNumber() + "'" +
            ", particulars='" + getParticulars() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", iso4217Code='" + getIso4217Code() + "'" +
            ", instalmentAmount=" + getInstalmentAmount() +
            ", totalTransferAmount=" + getTotalTransferAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            "}";
    }
}
