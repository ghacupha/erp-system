package io.github.erp.aop.reporting;

import io.github.erp.internal.report.ReportAssemblyService;
import io.github.erp.internal.report.ReportAttachmentService;
import io.github.erp.service.PdfReportRequisitionService;
import io.github.erp.service.ReportContentTypeService;
import io.github.erp.service.ReportRequisitionService;
import io.github.erp.service.XlsxReportRequisitionService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import io.github.erp.service.dto.ReportRequisitionDTO;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
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
}
