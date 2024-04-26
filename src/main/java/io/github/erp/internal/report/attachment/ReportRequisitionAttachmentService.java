package io.github.erp.internal.report.attachment;

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
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.service.ReportContentTypeService;
import io.github.erp.service.SystemContentTypeService;
import io.github.erp.service.dto.ReportRequisitionDTO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportRequisitionAttachmentService implements ReportAttachmentService<ReportRequisitionDTO> {

    private final ReportContentTypeService reportContentTypeService;
    private final SystemContentTypeService systemContentTypeService;

    private final static Logger log = LoggerFactory.getLogger(PDFReportAttachmentService.class);
    private final FileStorageService fileStorageService;

    public ReportRequisitionAttachmentService(
        ReportContentTypeService reportContentTypeService,
        SystemContentTypeService systemContentTypeService,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService) {
        this.reportContentTypeService = reportContentTypeService;
        this.systemContentTypeService = systemContentTypeService;
        this.fileStorageService = fileStorageService;
    }

    @SneakyThrows
    @Override
    public ReportRequisitionDTO attachReport(ReportRequisitionDTO one) {
        log.debug("Report designation {} has been mapped successfully for attachment. Commencing attachment", one.getReportName());

        long startup = System.currentTimeMillis();

        log.debug("Fetching report name : {}", one.getReportName());

        AtomicReference<String> reportFileName = new AtomicReference<>("");
        AtomicReference<String> fileAttachmentContentType = new AtomicReference<>("");

        reportContentTypeService.findOne(one.getReportContentType().getId()).ifPresent(type -> {
            reportFileName.set(one.getReportId().toString().concat(type.getReportFileExtension()));
            systemContentTypeService.findOne(type.getSystemContentType().getId()).ifPresent(sType -> {
                fileAttachmentContentType.set(sType.getContentTypeHeader());
            });
        });

        log.debug("Fetching report named : {}", reportFileName.get());

        byte[] reportAttachment = fileStorageService.load(reportFileName.get()).getInputStream().readAllBytes();
        String md5CheckSum = fileStorageService.calculateMD5CheckSum(reportFileName.get());

        log.debug("Attaching report retrieved to DTO designation : {} ", one.getReportName());
        one.setReportFileAttachment(reportAttachment);
        one.setReportFileAttachmentContentType(fileAttachmentContentType.get());
        one.setReportFileCheckSum("MD5 :" + md5CheckSum);

        log.debug("Report attachment completed successfully in {} milliseconds; sending attached report to the client  ", System.currentTimeMillis() - startup);
        return one;
    }
}
