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
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.IFRS16LeaseContract} entity.
 */
public class IFRS16LeaseContractDTO implements Serializable {

    private Long id;

    @NotNull
    private String bookingId;

    @NotNull
    private String leaseTitle;

    private String shortTitle;

    private String description;

    @NotNull
    private LocalDate inceptionDate;

    @NotNull
    private LocalDate commencementDate;

    private UUID serialNumber;

    private ServiceOutletDTO superintendentServiceOutlet;

    private DealerDTO mainDealer;

    private FiscalMonthDTO firstReportingPeriod;

    private FiscalMonthDTO lastReportingPeriod;

    private BusinessDocumentDTO leaseContractDocument;

    private BusinessDocumentDTO leaseContractCalculations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return leaseTitle;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(LocalDate inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public UUID getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ServiceOutletDTO getSuperintendentServiceOutlet() {
        return superintendentServiceOutlet;
    }

    public void setSuperintendentServiceOutlet(ServiceOutletDTO superintendentServiceOutlet) {
        this.superintendentServiceOutlet = superintendentServiceOutlet;
    }

    public DealerDTO getMainDealer() {
        return mainDealer;
    }

    public void setMainDealer(DealerDTO mainDealer) {
        this.mainDealer = mainDealer;
    }

    public FiscalMonthDTO getFirstReportingPeriod() {
        return firstReportingPeriod;
    }

    public void setFirstReportingPeriod(FiscalMonthDTO firstReportingPeriod) {
        this.firstReportingPeriod = firstReportingPeriod;
    }

    public FiscalMonthDTO getLastReportingPeriod() {
        return lastReportingPeriod;
    }

    public void setLastReportingPeriod(FiscalMonthDTO lastReportingPeriod) {
        this.lastReportingPeriod = lastReportingPeriod;
    }

    public BusinessDocumentDTO getLeaseContractDocument() {
        return leaseContractDocument;
    }

    public void setLeaseContractDocument(BusinessDocumentDTO leaseContractDocument) {
        this.leaseContractDocument = leaseContractDocument;
    }

    public BusinessDocumentDTO getLeaseContractCalculations() {
        return leaseContractCalculations;
    }

    public void setLeaseContractCalculations(BusinessDocumentDTO leaseContractCalculations) {
        this.leaseContractCalculations = leaseContractCalculations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IFRS16LeaseContractDTO)) {
            return false;
        }

        IFRS16LeaseContractDTO iFRS16LeaseContractDTO = (IFRS16LeaseContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, iFRS16LeaseContractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IFRS16LeaseContractDTO{" +
            "id=" + getId() +
            ", bookingId='" + getBookingId() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", shortTitle='" + getShortTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", inceptionDate='" + getInceptionDate() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", superintendentServiceOutlet=" + getSuperintendentServiceOutlet() +
            ", mainDealer=" + getMainDealer() +
            ", firstReportingPeriod=" + getFirstReportingPeriod() +
            ", lastReportingPeriod=" + getLastReportingPeriod() +
            ", leaseContractDocument=" + getLeaseContractDocument() +
            ", leaseContractCalculations=" + getLeaseContractCalculations() +
            "}";
    }
}
