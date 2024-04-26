package io.github.erp.service.dto;

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
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.WorkInProgressOutstandingReport} entity.
 */
public class WorkInProgressOutstandingReportDTO implements Serializable {

    private Long id;

    private String sequenceNumber;

    private String particulars;

    private String dealerName;

    private String instalmentTransactionNumber;

    private LocalDate instalmentTransactionDate;

    private String iso4217Code;

    private BigDecimal instalmentAmount;

    private BigDecimal totalTransferAmount;

    private BigDecimal outstandingAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getInstalmentTransactionNumber() {
        return instalmentTransactionNumber;
    }

    public void setInstalmentTransactionNumber(String instalmentTransactionNumber) {
        this.instalmentTransactionNumber = instalmentTransactionNumber;
    }

    public LocalDate getInstalmentTransactionDate() {
        return instalmentTransactionDate;
    }

    public void setInstalmentTransactionDate(LocalDate instalmentTransactionDate) {
        this.instalmentTransactionDate = instalmentTransactionDate;
    }

    public String getIso4217Code() {
        return iso4217Code;
    }

    public void setIso4217Code(String iso4217Code) {
        this.iso4217Code = iso4217Code;
    }

    public BigDecimal getInstalmentAmount() {
        return instalmentAmount;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public BigDecimal getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public void setTotalTransferAmount(BigDecimal totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkInProgressOutstandingReportDTO)) {
            return false;
        }

        WorkInProgressOutstandingReportDTO workInProgressOutstandingReportDTO = (WorkInProgressOutstandingReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workInProgressOutstandingReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkInProgressOutstandingReportDTO{" +
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
