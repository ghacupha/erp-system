package io.github.erp.internal.report;

import io.github.erp.service.dto.PdfReportRequisitionDTO;

import java.util.Optional;

public interface ReportAttachmentService<RR> {
    RR attachReport(RR one);
}
