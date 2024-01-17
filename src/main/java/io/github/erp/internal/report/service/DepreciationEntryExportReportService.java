package io.github.erp.internal.report.service;

import io.github.erp.service.dto.DepreciationReportDTO;

public interface DepreciationEntryExportReportService {

    void exportDepreciationEntryReport(DepreciationReportDTO depreciationReportDTO);
}
