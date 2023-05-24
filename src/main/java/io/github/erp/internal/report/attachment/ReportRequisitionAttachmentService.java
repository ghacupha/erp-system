package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
