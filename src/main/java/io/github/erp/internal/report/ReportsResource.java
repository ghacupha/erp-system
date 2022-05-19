package io.github.erp.internal.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/reports")
public class ReportsResource {
    private static final String TAG = "ReportsResource";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StringMarshalledReport stringMarshalledReport;

    private final SimpleJasperReportsService simpleJasperReportsService;

    public ReportsResource(StringMarshalledReport stringMarshalledReport, SimpleJasperReportsService simpleJasperReportsService) {
        this.stringMarshalledReport = stringMarshalledReport;
        this.simpleJasperReportsService = simpleJasperReportsService;
    }

    @PostMapping("/dealers-listing")
    public ResponseEntity<Void> createDealersReport() {
        log.debug("REST request to create report {}", "dealers");

        // String reportPath = stringMarshalledReport.createReport("dealers");

        simpleJasperReportsService.generateReport();

        //log.debug("Report generated on the path {}", reportPath);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "The report has been created successfully", null))
            .build();
    }
}
