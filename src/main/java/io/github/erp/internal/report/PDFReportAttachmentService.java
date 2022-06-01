package io.github.erp.internal.report;

/*-
 * Erp System - Mark II No 4 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * This services attaches a PDF report to a pdf-report-requisition-dto
 */
@Service
public class PDFReportAttachmentService implements ReportAttachmentService<Optional<PdfReportRequisitionDTO>> {

    private final static Logger log = LoggerFactory.getLogger(PDFReportAttachmentService.class);
    private final FileStorageService fileStorageService;

    public PDFReportAttachmentService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @SneakyThrows
    @Override
    public Optional<PdfReportRequisitionDTO> attachReport(Optional<PdfReportRequisitionDTO> one) {

        one.ifPresent(
            dto -> {
                log.debug("Fetching report name : {}", dto.getReportName());
                byte[] reportResource = new byte[0];
                try {
                    reportResource = fileStorageService.load(dto.getReportId().toString().concat(".pdf")).getInputStream().readAllBytes();
                } catch (IOException e) {
                    log.error("We were unable to find the generated report with id: " + dto.getReportId().toString().concat(".pdf"), e);
                }
                dto.setReportAttachment(reportResource);}
        );

        return one;
    }
}
