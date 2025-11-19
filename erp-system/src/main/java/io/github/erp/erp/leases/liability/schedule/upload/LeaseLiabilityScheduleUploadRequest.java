package io.github.erp.erp.leases.liability.schedule.upload;

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

import javax.validation.constraints.NotNull;

/**
 * Request payload describing how to process an uploaded lease liability schedule
 * CSV file.
 */
public class LeaseLiabilityScheduleUploadRequest {

    @NotNull
    private Long leaseLiabilityId;

    // Optional: a new schedule will be created for the selected liability when omitted
    private Long leaseAmortizationScheduleId;

    // Optional: a fresh compilation is generated server-side when not supplied
    private Long leaseLiabilityCompilationId;

    private boolean launchBatchImmediately = true;

    public Long getLeaseLiabilityId() {
        return leaseLiabilityId;
    }

    public void setLeaseLiabilityId(Long leaseLiabilityId) {
        this.leaseLiabilityId = leaseLiabilityId;
    }

    public Long getLeaseAmortizationScheduleId() {
        return leaseAmortizationScheduleId;
    }

    public void setLeaseAmortizationScheduleId(Long leaseAmortizationScheduleId) {
        this.leaseAmortizationScheduleId = leaseAmortizationScheduleId;
    }

    public Long getLeaseLiabilityCompilationId() {
        return leaseLiabilityCompilationId;
    }

    public void setLeaseLiabilityCompilationId(Long leaseLiabilityCompilationId) {
        this.leaseLiabilityCompilationId = leaseLiabilityCompilationId;
    }

    public boolean isLaunchBatchImmediately() {
        return launchBatchImmediately;
    }

    public void setLaunchBatchImmediately(boolean launchBatchImmediately) {
        this.launchBatchImmediately = launchBatchImmediately;
    }
}

