package io.github.erp.internal.report.attachment.models;

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
import io.github.erp.internal.model.HasChecksum;
import io.github.erp.internal.report.attachment.AttachedUnTamperedReport;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Model for report-attachment protocol on the prepayment-by-account-report-requisition api
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachedPrepaymentByAccountReportRequisitionDTO  implements AttachedUnTamperedReport<PrepaymentByAccountReportRequisitionDTO>, HasChecksum {

    private Long id;

    private UUID requestId;

    @NotNull
    private ZonedDateTime timeOfRequisition;

    private String fileChecksum;

    private UUID filename;

    private String reportParameters;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;

    @NotNull
    private LocalDate reportDate;

    private Boolean tampered;

    private ApplicationUserDTO requestedBy;

    private ApplicationUserDTO lastAccessedBy;

    @Override
    public void setChecksum(String fileChecksum) {

        this.fileChecksum = fileChecksum;
    }

    @Override
    public AttachedUnTamperedReport<PrepaymentByAccountReportRequisitionDTO> setReportAttachment(byte[] reportResource) {

        this.reportFile = reportResource;

        return this;
    }

    /**
     * @return Report name as it is saved in the DB
     */
    @Override
    public String getReportName() {
        return requestId.toString();
    }

    /**
     * @return Filename as UUID
     */
    @Override
    public UUID getReportId() {
        return filename;
    }

    @Override
    public void setReportTampered(boolean reportIsTampered) {

        this.tampered = reportIsTampered;
    }

    @Override
    public boolean getReportTampered() {
        return tampered;
    }
}
