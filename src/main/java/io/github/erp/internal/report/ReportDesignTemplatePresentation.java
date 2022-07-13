package io.github.erp.internal.report;

/*-
 * Erp System - Mark II No 19 (Baruch Series)
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
import io.github.erp.internal.files.JRXMLMultipartFile;
import io.github.erp.service.dto.ReportDesignDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This services checks the repository for the report templated previously saved and creates the same
 * on the file-system using the file-storage-service.
 */
@Service
@Transactional
public class ReportDesignTemplatePresentation implements ReportTemplatePresentation<ReportDesignDTO>  {

    private final FileStorageService fileStorageService;
    private final ReportsProperties reportsProperties;

    public ReportDesignTemplatePresentation(final FileStorageService fileStorageService,
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
