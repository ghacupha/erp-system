package io.github.erp.internal.files;

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

import io.github.erp.internal.report.PDFReportRequisitionService;
import io.github.erp.service.PdfReportRequisitionService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FilesSystemInit implements ApplicationListener<ApplicationReadyEvent> {

    private final FileStorageService storageService;
    private final PdfReportRequisitionService pdfReportRequisitionService;

    public FilesSystemInit(FileStorageService storageService, PdfReportRequisitionService pdfReportRequisitionService) {
        this.storageService = storageService;
        this.pdfReportRequisitionService = pdfReportRequisitionService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Delete all report entities from the system
        pdfReportRequisitionService.findAll(Pageable.unpaged())
            .forEach((report) -> pdfReportRequisitionService.delete(report.getId()));

        // delete the report files from the system
        storageService.deleteAll();

        // initialize storage
        storageService.init();
    }
}
