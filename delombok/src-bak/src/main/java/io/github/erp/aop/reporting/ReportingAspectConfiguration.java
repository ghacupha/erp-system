package io.github.erp.aop.reporting;

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
import io.github.erp.aop.reporting.amortizationPosting.AmortizationPostingReportRequisitionAttachmentInterceptor;
import io.github.erp.aop.reporting.amortizationPosting.AmortizationPostingReportRequisitionInterceptor;
import io.github.erp.aop.reporting.assets.AssetAdditionsReportAttachmentInterceptor;
import io.github.erp.aop.reporting.assets.AssetAdditionsReportInterceptor;
import io.github.erp.aop.reporting.depreciation.DepreciationReportAttachmentInterceptor;
import io.github.erp.aop.reporting.depreciation.DepreciationReportInterceptor;
import io.github.erp.aop.reporting.monthlyPrepaymentReport.MonthlyPrepaymentReportRequisitionIntercept;
import io.github.erp.aop.reporting.monthlyPrepaymentReport.MonthlyPrepaymentReportRequisitionAttachmentInterceptor;
import io.github.erp.aop.reporting.prepaymentByAccount.PrepaymentByAccountReportAttachmentInterceptor;
import io.github.erp.aop.reporting.prepaymentByAccount.PrepaymentByAccountReportRequisitionInterceptor;
import io.github.erp.aop.reporting.prepaymentReport.PrepaymentReportRequisitionAttachmentInterceptor;
import io.github.erp.aop.reporting.prepaymentReport.PrepaymentReportRequisitionInterceptor;
import io.github.erp.aop.reporting.wip.WorkInProgressOutstandingReportAttachmentInterceptor;
import io.github.erp.aop.reporting.wip.WorkInProgressOutstandingReportRequisitionInterceptor;
import io.github.erp.internal.report.service.DepreciationEntryExportReportService;
import io.github.erp.internal.report.service.ExportReportService;
import io.github.erp.internal.repository.InternalProcessStatusRepository;
import io.github.erp.internal.repository.InternalReportStatusRepository;
import io.github.erp.internal.report.assemblies.ReportAssemblyService;
import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.service.*;
import io.github.erp.service.dto.*;
import io.github.erp.service.mapper.ReportStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportingAspectConfiguration {

    @Autowired
    private ReportAssemblyService<ReportRequisitionDTO> reportAssemblyService;

    @Autowired
    private PdfReportRequisitionService pdfReportRequisitionService;

    @Autowired
    private ReportAssemblyService<PdfReportRequisitionDTO> pdfReportAssemblyService;

    @Autowired
    private XlsxReportRequisitionService xlsxReportRequisitionService;

    @Autowired
    private ReportAssemblyService<XlsxReportRequisitionDTO> xlsxReportAssemblyService;

    @Autowired
    private ReportContentTypeService reportContentTypeService;

    @Autowired
    private ReportRequisitionService reportRequisitionService;

    @Autowired
    private ReportAttachmentService<ReportRequisitionDTO> reportAttachmentService;

    @Autowired
    private ReportStatusMapper reportStatusMapper;

    @Autowired
    private InternalProcessStatusRepository processStatusRepository;

    @Autowired
    private InternalReportStatusRepository reportStatusRepository;

    @Autowired
    private ExcelReportExportService excelReportExportService;

    @Autowired
    private ReportAssemblyService<ExcelReportExportDTO> excelReportExportAssemblyService;

    @Autowired
    private ReportAttachmentService<ExcelReportExportDTO> excelReportExportReportAttachmentService;

    @Autowired
    private ReportAttachmentService<AutonomousReportDTO> autonomousReportAttachmentService;

    @Autowired
    private DepreciationEntryExportReportService depreciationEntryExportReportService;


    @Autowired
    private ReportAttachmentService<DepreciationReportDTO> depreciationReportReportAttachmentService;

    @Autowired
    private ExportReportService<AssetAdditionsReportDTO> assetReportExportReportService;

    @Autowired
    private ReportAttachmentService<AssetAdditionsReportDTO> assetAdditionsReportDTOReportAttachmentService;

    @Autowired
    @Qualifier("prepaymentReportUserInitiatedExportService")
    private ExportReportService<PrepaymentReportRequisitionDTO> prepaymentReportExportReportService;

    @Autowired
    private ReportAttachmentService<PrepaymentReportRequisitionDTO> prepaymentReportRequisitionAttachmentService;

    @Autowired
    @Qualifier("amortizationPostingRequisitionUserInitiatedReportExport")
    private ExportReportService<AmortizationPostingReportRequisitionDTO> amortizationPostingReportRequisitionExportReportService;

    @Autowired
    private ReportAttachmentService<AmortizationPostingReportRequisitionDTO> amortizationPostingReportRequisitionAttachmentService;

    @Autowired
    private ExportReportService<PrepaymentByAccountReportRequisitionDTO> prepaymentByAccountReportRequisitionDTOExportReportService;

    @Autowired
    private ExportReportService<MonthlyPrepaymentReportRequisitionDTO> prepaymentByAccountReportRequisitionExportReportService;

    @Autowired
    private ReportAttachmentService<PrepaymentByAccountReportRequisitionDTO> prepaymentByAccountReportRequisitionReportAttachmentService;

    @Autowired
    private ReportAttachmentService<MonthlyPrepaymentReportRequisitionDTO> monthlyPrepaymentReportRequisitionReportAttachmentService;

    @Autowired
    private ExportReportService<WorkInProgressOutstandingReportRequisitionDTO> workInProgressOutstandingReportRequisitionExportReportService;

    @Autowired
    private ReportAttachmentService<WorkInProgressOutstandingReportRequisitionDTO> workInProgressOutstandingReportRequisitionReportAttachmentService;

    @Bean
    public ReportRequisitionInterceptor reportRequisitionInterceptor() {

        return new ReportRequisitionInterceptor(reportContentTypeService, reportRequisitionService, reportAssemblyService);
    }

    @Bean
    public PDFReportRequisitionInterceptor pdfReportRequisitionInterceptor() {

        return new PDFReportRequisitionInterceptor(pdfReportAssemblyService, pdfReportRequisitionService);
    }

    @Bean
    public XLSXReportRequisitionInterceptor xlsxReportRequisitionInterceptor() {

        return new XLSXReportRequisitionInterceptor(xlsxReportAssemblyService, xlsxReportRequisitionService);
    }

    @Bean
    public ReportRequisitionAttachmentInterceptor reportRequisitionAttachmentInterceptor() {
        return new ReportRequisitionAttachmentInterceptor(reportAttachmentService);
    }

    @Bean
    public ExcelReportExportInterceptor excelReportExportInterceptor() {
        return new ExcelReportExportInterceptor(reportStatusMapper, processStatusRepository, reportStatusRepository, excelReportExportService, excelReportExportAssemblyService);
    }

    @Bean
    public ExcelReportExportAttachmentInterceptor excelReportExportAttachmentInterceptor() {
        return new ExcelReportExportAttachmentInterceptor(excelReportExportReportAttachmentService);
    }

    @Bean
    public AutonomousReportAttachmentInterceptor autonomousReportAttachmentInterceptor() {
        return new AutonomousReportAttachmentInterceptor(autonomousReportAttachmentService);
    }

    @Bean
    public DepreciationReportInterceptor depreciationReportInterceptor() {
        return new DepreciationReportInterceptor(depreciationEntryExportReportService);
    }

    @Bean
    public DepreciationReportAttachmentInterceptor depreciationReportAttachmentInterceptor() {
        return new DepreciationReportAttachmentInterceptor(depreciationReportReportAttachmentService);
    }

    @Bean
    public AssetAdditionsReportInterceptor assetAdditionsReportInterceptor() {
        return new AssetAdditionsReportInterceptor(assetReportExportReportService);
    }

    @Bean
    public AssetAdditionsReportAttachmentInterceptor assetAdditionsReportAttachmentInterceptor() {
        return new AssetAdditionsReportAttachmentInterceptor(assetAdditionsReportDTOReportAttachmentService);
    }

    @Bean
    public PrepaymentReportRequisitionInterceptor prepaymentReportRequisitionInterceptor() {
        return new PrepaymentReportRequisitionInterceptor(prepaymentReportExportReportService);
    }

    @Bean
    public PrepaymentReportRequisitionAttachmentInterceptor prepaymentReportRequisitionAttachmentInterceptor() {
        return new PrepaymentReportRequisitionAttachmentInterceptor(prepaymentReportRequisitionAttachmentService);
    }

    @Bean
    public AmortizationPostingReportRequisitionInterceptor amortizationPostingReportRequisitionInterceptor() {
        return new AmortizationPostingReportRequisitionInterceptor(amortizationPostingReportRequisitionExportReportService);
    }

    @Bean
    public AmortizationPostingReportRequisitionAttachmentInterceptor amortizationPostingReportRequisitionAttachmentInterceptor() {
        return new AmortizationPostingReportRequisitionAttachmentInterceptor(amortizationPostingReportRequisitionAttachmentService);
    }

    @Bean
    public PrepaymentByAccountReportRequisitionInterceptor prepaymentByAccountReportRequisitionInterceptor() {

        return new PrepaymentByAccountReportRequisitionInterceptor(prepaymentByAccountReportRequisitionDTOExportReportService);
    }


    @Bean
    public MonthlyPrepaymentReportRequisitionIntercept monthlyPrepaymentReportRequisitionIntercept() {

        return new MonthlyPrepaymentReportRequisitionIntercept(prepaymentByAccountReportRequisitionExportReportService);
    }

    @Bean
    public PrepaymentByAccountReportAttachmentInterceptor prepaymentByAccountReportAttachmentInterceptor() {

        return new PrepaymentByAccountReportAttachmentInterceptor(prepaymentByAccountReportRequisitionReportAttachmentService);
    }

    @Bean
    public MonthlyPrepaymentReportRequisitionAttachmentInterceptor prepaymentByMonthReportAttachmentInterceptor() {

        return new MonthlyPrepaymentReportRequisitionAttachmentInterceptor(monthlyPrepaymentReportRequisitionReportAttachmentService);
    }

    @Bean
    public WorkInProgressOutstandingReportRequisitionInterceptor workInProgressOutstandingReportRequisitionInterceptor() {

        return new WorkInProgressOutstandingReportRequisitionInterceptor(workInProgressOutstandingReportRequisitionExportReportService);
    }

    @Bean
    public WorkInProgressOutstandingReportAttachmentInterceptor workInProgressOutstandingReportAttachmentInterceptor() {

        return new WorkInProgressOutstandingReportAttachmentInterceptor(workInProgressOutstandingReportRequisitionReportAttachmentService);
    }
}
