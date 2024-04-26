package io.github.erp.aop.nbv;

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
