package io.github.erp.internal.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
