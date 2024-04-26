package io.github.erp.aop.reporting;

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
}
