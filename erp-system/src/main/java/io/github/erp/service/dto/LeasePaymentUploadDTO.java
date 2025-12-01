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

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class LeasePaymentUploadDTO implements Serializable {

    private Long id;

    private String uploadStatus;

    @NotNull
    private Instant createdAt;

    private Boolean active;

    private CsvFileUploadDTO csvFileUpload;

    private IFRS16LeaseContractDTO leaseContract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CsvFileUploadDTO getCsvFileUpload() {
        return csvFileUpload;
    }

    public void setCsvFileUpload(CsvFileUploadDTO csvFileUpload) {
        this.csvFileUpload = csvFileUpload;
    }

    public IFRS16LeaseContractDTO getLeaseContract() {
        return leaseContract;
    }

    public void setLeaseContract(IFRS16LeaseContractDTO leaseContract) {
        this.leaseContract = leaseContract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeasePaymentUploadDTO)) {
            return false;
        }

        LeasePaymentUploadDTO leasePaymentUploadDTO = (LeasePaymentUploadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leasePaymentUploadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "LeasePaymentUploadDTO{" +
            "id=" + getId() +
            ", uploadStatus='" + getUploadStatus() + '\'' +
            ", createdAt=" + getCreatedAt() +
            ", active=" + getActive() +
            "}";
    }
}
