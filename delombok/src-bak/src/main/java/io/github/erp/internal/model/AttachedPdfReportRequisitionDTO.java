package io.github.erp.internal.model;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.attachment.AttachedReport;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.dto.ReportTemplateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachedPdfReportRequisitionDTO implements AttachedReport<PdfReportRequisitionDTO>, HasChecksum {

    @Override
    public void setChecksum(String fileChecksum) {
        this.fileCheckSum = fileChecksum;
    }

    @Override
    public String getFileChecksum() {
        return this.fileCheckSum;
    }

    private Long id;

    private String fileCheckSum;

    @NotNull
    private String reportName;

    private LocalDate reportDate;

    private String userPassword;

    @NotNull
    private String ownerPassword;

    private ReportStatusTypes reportStatus;

    @NotNull
    private UUID reportId;

    private ReportTemplateDTO reportTemplate;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private byte[] reportAttachment;

    private String contentType = "application/pdf";

    @Override
    public AttachedReport<PdfReportRequisitionDTO> setReportAttachment(byte[] reportAttachment) {

        this.reportAttachment = reportAttachment;

        return this;
    }
}
