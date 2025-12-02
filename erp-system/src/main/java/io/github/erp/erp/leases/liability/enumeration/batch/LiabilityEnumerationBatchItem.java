package io.github.erp.erp.leases.liability.enumeration.batch;

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
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.LeasePaymentUpload;
import io.github.erp.domain.LiabilityEnumeration;
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationRequest;
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationResponse;
import io.github.erp.domain.enumeration.LiabilityTimeGranularity;
import io.github.erp.erp.leases.liability.enumeration.PresentValueLine;
import java.math.BigDecimal;
import java.util.List;

public class LiabilityEnumerationBatchItem {

    private final LiabilityEnumerationRequest request;
    private LiabilityTimeGranularity granularity;
    private BigDecimal annualRate;
    private IFRS16LeaseContract leaseContract;
    private LeasePaymentUpload leasePaymentUpload;
    private LiabilityEnumeration liabilityEnumeration;
    private List<LeasePayment> leasePayments;
    private List<PresentValueLine> presentValueLines;
    private LiabilityEnumerationResponse response;

    public LiabilityEnumerationBatchItem(LiabilityEnumerationRequest request) {
        this.request = request;
    }

    public LiabilityEnumerationRequest getRequest() {
        return request;
    }

    public LiabilityTimeGranularity getGranularity() {
        return granularity;
    }

    public void setGranularity(LiabilityTimeGranularity granularity) {
        this.granularity = granularity;
    }

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
    }

    public IFRS16LeaseContract getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContract leaseContract) {
        this.leaseContract = leaseContract;
    }

    public LeasePaymentUpload getLeasePaymentUpload() {
        return leasePaymentUpload;
    }

    public void setLeasePaymentUpload(LeasePaymentUpload leasePaymentUpload) {
        this.leasePaymentUpload = leasePaymentUpload;
    }

    public LiabilityEnumeration getLiabilityEnumeration() {
        return liabilityEnumeration;
    }

    public void setLiabilityEnumeration(LiabilityEnumeration liabilityEnumeration) {
        this.liabilityEnumeration = liabilityEnumeration;
    }

    public List<LeasePayment> getLeasePayments() {
        return leasePayments;
    }

    public void setLeasePayments(List<LeasePayment> leasePayments) {
        this.leasePayments = leasePayments;
    }

    public List<PresentValueLine> getPresentValueLines() {
        return presentValueLines;
    }

    public void setPresentValueLines(List<PresentValueLine> presentValueLines) {
        this.presentValueLines = presentValueLines;
    }

    public LiabilityEnumerationResponse getResponse() {
        return response;
    }

    public void setResponse(LiabilityEnumerationResponse response) {
        this.response = response;
    }
}
