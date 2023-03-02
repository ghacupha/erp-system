package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark III No 11 (Caleb Series) Server ver 0.7.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractReportAttachmentService<DTO> {

    private static final Logger log = LoggerFactory.getLogger("ReportAttachmentService");

    private final FileStorageService fileStorageService;

    public AbstractReportAttachmentService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @NotNull
    @SneakyThrows
    public AttachedReport<DTO> attachReport(AttachedReport<DTO> one, String fileExtension) {
        log.debug("Report designation {} has been mapped successfully for attachment. Commencing attachment", one.getReportName());

        long startup = System.currentTimeMillis();

        log.debug("Fetching report name : {}", one.getReportName());

        String reportFileName = one.getReportId().toString().concat(fileExtension);

        log.debug("Fetching report named : {}", reportFileName);

        byte[] reportAttachment = fileStorageService.load(reportFileName).getInputStream().readAllBytes();

        log.debug("Attaching report retrieved to DTO designation : {} ", one.getReportName());
        one.setReportAttachment(reportAttachment);
        one.setChecksum(fileStorageService.calculateMD5CheckSum(reportFileName));

        log.debug("Report attachment completed successfully in {} milliseconds; sending attached report to the client  ", System.currentTimeMillis() - startup);
        return one;
    }
}
