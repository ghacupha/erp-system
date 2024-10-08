package io.github.erp.service.dto;

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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.JobSheet} entity.
 */
public class JobSheetDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    private LocalDate jobSheetDate;

    private String details;

    @Lob
    private String remarks;

    private DealerDTO biller;

    private Set<DealerDTO> signatories = new HashSet<>();

    private DealerDTO contactPerson;

    private Set<BusinessStampDTO> businessStamps = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getJobSheetDate() {
        return jobSheetDate;
    }

    public void setJobSheetDate(LocalDate jobSheetDate) {
        this.jobSheetDate = jobSheetDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public DealerDTO getBiller() {
        return biller;
    }

    public void setBiller(DealerDTO biller) {
        this.biller = biller;
    }

    public Set<DealerDTO> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<DealerDTO> signatories) {
        this.signatories = signatories;
    }

    public DealerDTO getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(DealerDTO contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Set<BusinessStampDTO> getBusinessStamps() {
        return businessStamps;
    }

    public void setBusinessStamps(Set<BusinessStampDTO> businessStamps) {
        this.businessStamps = businessStamps;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<PaymentLabelDTO> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabelDTO> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobSheetDTO)) {
            return false;
        }

        JobSheetDTO jobSheetDTO = (JobSheetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jobSheetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobSheetDTO{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", jobSheetDate='" + getJobSheetDate() + "'" +
            ", details='" + getDetails() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", biller=" + getBiller() +
            ", signatories=" + getSignatories() +
            ", contactPerson=" + getContactPerson() +
            ", businessStamps=" + getBusinessStamps() +
            ", placeholders=" + getPlaceholders() +
            ", paymentLabels=" + getPaymentLabels() +
            ", businessDocuments=" + getBusinessDocuments() +
            "}";
    }
}
