package io.github.erp.internal.report;

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
import io.github.erp.internal.report.assemblies.PDFReportsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * This is one of the original designs of the reports system that was deemed inadequate
 * in favor of full-blown entities with authentication and parameters for metadata and that's
 * why this is going in a future update
 */
@Deprecated
@RestController
@RequestMapping("/api/reports")
public class ReportsResource {
    private static final String TAG = "ReportsResource";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private static int reportCounter = 0;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PDFReportsService simpleJasperReportsService;

    public ReportsResource(PDFReportsService simpleJasperReportsService) {
        this.simpleJasperReportsService = simpleJasperReportsService;
    }

    @PostMapping("/dealers-listing")
    public ResponseEntity<Void> createDealersReport() {
        log.debug("REST request to create report {}", "dealers");

        // TODO REPLACE THESE PARAMETERS WITH VALUES FROM FRONT-END REQUEST
        String reportPath =
            simpleJasperReportsService.generateReport(
                "Simple_Blue.jrxml",
                "dealers-report.pdf",
                "ownerPassword",
                "userPassword",
            Map.of("title", "Sample report"));

        log.debug("Report generated on the path {}", reportPath);

        reportCounter++;

        return ResponseEntity
            .ok()
            .build();
    }
}
