package io.github.erp.internal.report;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
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
