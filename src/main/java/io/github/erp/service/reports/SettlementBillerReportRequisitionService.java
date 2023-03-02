package io.github.erp.service.reports;

import io.github.erp.erp.reports.SettlementBillerReportDTO;
import io.github.erp.erp.reports.SettlementBillerReportRequisitionDTO;
import io.github.erp.internal.report.ReportModel;

import java.util.List;

public interface SettlementBillerReportRequisitionService {

    ReportModel<List<SettlementBillerReportDTO>> generateReport(SettlementBillerReportRequisitionDTO settlementBillerReportDto);
}
