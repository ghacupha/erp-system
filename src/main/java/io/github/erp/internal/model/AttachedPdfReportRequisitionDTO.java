package io.github.erp.internal.model;

import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.AttachedReport;
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
public class AttachedPdfReportRequisitionDTO implements AttachedReport<PdfReportRequisitionDTO> {

    private Long id;

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
