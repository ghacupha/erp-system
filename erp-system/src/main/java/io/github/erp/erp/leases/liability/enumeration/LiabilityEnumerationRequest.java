package io.github.erp.erp.leases.liability.enumeration;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LiabilityEnumerationRequest {

    @NotNull
    private Long leaseContractId;

    @NotNull
    private Long leasePaymentUploadId;

    @NotBlank
    private String interestRate;

    @NotBlank
    private String timeGranularity;

    private Boolean active = Boolean.TRUE;

    public Long getLeaseContractId() {
        return leaseContractId;
    }

    public void setLeaseContractId(Long leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public Long getLeasePaymentUploadId() {
        return leasePaymentUploadId;
    }

    public void setLeasePaymentUploadId(Long leasePaymentUploadId) {
        this.leasePaymentUploadId = leasePaymentUploadId;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getTimeGranularity() {
        return timeGranularity;
    }

    public void setTimeGranularity(String timeGranularity) {
        this.timeGranularity = timeGranularity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
