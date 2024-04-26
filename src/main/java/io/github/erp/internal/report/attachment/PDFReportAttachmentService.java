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
import io.github.erp.internal.model.AttachedPdfReportRequisitionDTO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * This services attaches a PDF report to a pdf-report-requisition-dto
 */
@Service
public class PDFReportAttachmentService implements ReportAttachmentService<AttachedPdfReportRequisitionDTO> {

    private final static Logger log = LoggerFactory.getLogger(PDFReportAttachmentService.class);
    private final FileStorageService fileStorageService;

    public PDFReportAttachmentService(@Qualifier("reportsFSStorageService") FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @SneakyThrows
    @Override
    public AttachedPdfReportRequisitionDTO attachReport(AttachedPdfReportRequisitionDTO one) {
        log.debug("Report designation {} has been mapped successfully for attachment. Commencing attachment", one.getReportName());

        long startup = System.currentTimeMillis();

        log.debug("Fetching report name : {}", one.getReportName());

        String reportFileName = one.getReportId().toString().concat(".pdf");

        log.debug("Fetching report named : {}", reportFileName);

        byte[] reportAttachment = fileStorageService.load(reportFileName).getInputStream().readAllBytes();

        log.debug("Attaching report retrieved to DTO designation : {} ", one.getReportName());
        one.setReportAttachment(reportAttachment);

        log.debug("Report attachment completed successfully in {} milliseconds; sending attached report to the client  ", System.currentTimeMillis() - startup);
        return one;
    }
}
