package io.github.erp.internal.model;

import io.github.erp.domain.enumeration.ReportStatusTypes;
import io.github.erp.internal.report.AttachedReport;
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
public class AttachedXlsxReportRequisitionDTO implements AttachedReport<XlsxReportRequisitionDTO> {

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
