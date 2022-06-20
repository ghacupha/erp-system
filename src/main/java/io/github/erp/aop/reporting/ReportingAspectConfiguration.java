package io.github.erp.aop.reporting;

import io.github.erp.internal.report.ReportAssemblyService;
import io.github.erp.service.PdfReportRequisitionService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import io.github.erp.service.dto.ReportRequisitionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportingAspectConfiguration {

    @Autowired
    private ReportAssemblyService<ReportRequisitionDTO> reportRequisitionService;

    @Autowired
    private PdfReportRequisitionService pdfReportRequisitionService;

    @Autowired
    private ReportAssemblyService<PdfReportRequisitionDTO> pdfReportAssemblyService;

    @Bean
    public ReportRequisitionInterceptor reportRequisitionInterceptor() {

        return new ReportRequisitionInterceptor(reportRequisitionService);
    }

    @Bean
    public PDFReportRequisitionInterceptor pdfReportRequisitionInterceptor() {

        return new PDFReportRequisitionInterceptor(pdfReportAssemblyService, pdfReportRequisitionService);
    }
}
