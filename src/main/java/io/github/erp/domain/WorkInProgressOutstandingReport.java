package io.github.erp.domain;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "instalment_transaction_number")
    private String instalmentTransactionNumber;

    @Column(name = "instalment_transaction_date")
    private LocalDate instalmentTransactionDate;

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

    public String getInstalmentTransactionNumber() {
        return this.instalmentTransactionNumber;
    }

    public WorkInProgressOutstandingReport instalmentTransactionNumber(String instalmentTransactionNumber) {
        this.setInstalmentTransactionNumber(instalmentTransactionNumber);
        return this;
    }

    public void setInstalmentTransactionNumber(String instalmentTransactionNumber) {
        this.instalmentTransactionNumber = instalmentTransactionNumber;
    }

    public LocalDate getInstalmentTransactionDate() {
        return this.instalmentTransactionDate;
    }

    public WorkInProgressOutstandingReport instalmentTransactionDate(LocalDate instalmentTransactionDate) {
        this.setInstalmentTransactionDate(instalmentTransactionDate);
        return this;
    }

    public void setInstalmentTransactionDate(LocalDate instalmentTransactionDate) {
        this.instalmentTransactionDate = instalmentTransactionDate;
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
            ", instalmentTransactionNumber='" + getInstalmentTransactionNumber() + "'" +
            ", instalmentTransactionDate='" + getInstalmentTransactionDate() + "'" +
            ", iso4217Code='" + getIso4217Code() + "'" +
            ", instalmentAmount=" + getInstalmentAmount() +
            ", totalTransferAmount=" + getTotalTransferAmount() +
            ", outstandingAmount=" + getOutstandingAmount() +
            "}";
    }
}
