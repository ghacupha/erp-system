package io.github.erp.aop.reporting;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.repository.InternalProcessStatusRepository;
import io.github.erp.internal.repository.InternalReportStatusRepository;
import io.github.erp.internal.report.assemblies.ReportAssemblyService;
import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.service.*;
import io.github.erp.service.dto.*;
import io.github.erp.service.mapper.ReportStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
}
