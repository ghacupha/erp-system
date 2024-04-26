package io.github.erp.internal.model;

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
import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.attachment.AttachedReport;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.dto.ReportTemplateDTO;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
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
public class AttachedXlsxReportRequisitionDTO implements AttachedReport<XlsxReportRequisitionDTO>, HasChecksum {

    @Override
    public void setChecksum(String fileChecksum) {
        this.fileCheckSum = fileChecksum;
    }

    @Override
    public String getFileChecksum() {
        return this.fileCheckSum;
    }

    private String fileCheckSum;

    private Long id;

    @NotNull
    private String reportName;

    private LocalDate reportDate;

    private String userPassword;

    private ReportStatusTypes reportStatus;

    @NotNull
    private UUID reportId;

    private ReportTemplateDTO reportTemplate;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private byte[] reportAttachment;

    private String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    // private AttachedReport<AttachedXlsxReportRequisitionDTO> reportMapping;

    @Override
    public AttachedReport<XlsxReportRequisitionDTO> setReportAttachment(byte[] reportAttachment) {

        this.reportAttachment = reportAttachment;

        return this;
    }

    public AttachedReport<XlsxReportRequisitionDTO> implementation() {
        return this;
    }
}
