package io.github.erp.aop.nbv;

import io.github.erp.erp.assets.nbv.NBVJobSequenceService;
import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.internal.report.service.NetBookValueEntryExportReportService;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import io.github.erp.service.dto.NbvReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NBVJobInterceptorConfiguration {

    @Autowired
    private NBVJobSequenceService<NbvCompilationJobDTO> nbvJobSequenceService;

    @Autowired
    private NetBookValueEntryExportReportService<NbvReportDTO> netBookValueEntryExportReportService;

    @Autowired
    private ReportAttachmentService<NbvReportDTO> reportAttachmentService;

    @Bean
    public NBVJobInterceptor nbvJobInterceptor() {

        return new NBVJobInterceptor(nbvJobSequenceService);
    }

    @Bean
    public NBVReportInterceptor nbvReportInterceptor() {

        return new NBVReportInterceptor(netBookValueEntryExportReportService);
    }

    @Bean
    public NBVReportAttachmentInterceptor nbvReportAttachmentInterceptor() {

        return new NBVReportAttachmentInterceptor(reportAttachmentService);
    }

}
