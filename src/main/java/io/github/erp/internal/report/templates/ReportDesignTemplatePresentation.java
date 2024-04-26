package io.github.erp.internal.report.templates;

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
