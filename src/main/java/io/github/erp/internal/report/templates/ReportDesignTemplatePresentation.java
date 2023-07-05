package io.github.erp.internal.report.templates;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.0
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
import io.github.erp.internal.files.JRXMLMultipartFile;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.service.dto.ReportDesignDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This services checks the repository for the report templated previously saved and creates the same
 * on the file-system using the file-storage-service.
 * This interface exists in anticipation of a future interface in which instead of dumping the files on
 * the host's file system, the system might need to do so on some remote drive, probably using native
 * tools like git or FTP. The current implementation is a simple dump-to-fs implementation and works for now.
 * So that puts a ding on the cloud-readiness theme huh?
 */
@Service
@Transactional
public class ReportDesignTemplatePresentation implements ReportTemplatePresentation<ReportDesignDTO>  {

    private final FileStorageService fileStorageService;
    private final ReportsProperties reportsProperties;

    public ReportDesignTemplatePresentation(@Qualifier("reportsFSStorageService") final FileStorageService fileStorageService,
                                            final ReportsProperties reportsProperties) {
        this.fileStorageService = fileStorageService;
        this.reportsProperties = reportsProperties;
    }

    public String presentTemplate(ReportDesignDTO reportTemplate) {

            fileStorageService.save(
                new JRXMLMultipartFile(
                    reportTemplate.getReportFile(),
                    reportsProperties.getReportsDirectory(),
                    reportTemplate.getCatalogueNumber().toString().concat(".jrxml")
                )
            );


        return reportTemplate.getCatalogueNumber().toString().concat(".jrxml");
    }
}
