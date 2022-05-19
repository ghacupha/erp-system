package io.github.erp.internal.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/reports")
public class ReportsResource {
    private static final String TAG = "ReportsResource";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private static int reportCounter = 0;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SimpleJasperReportsService simpleJasperReportsService;

    public ReportsResource(SimpleJasperReportsService simpleJasperReportsService) {
        this.simpleJasperReportsService = simpleJasperReportsService;
    }

    @PostMapping("/dealers-listing")
    public ResponseEntity<Void> createDealersReport() {
        log.debug("REST request to create report {}", "dealers");

        // TODO REPLACE THESE PARAMETERS WITH VALUES FROM FRONT-END REQUEST
        String reportPath = simpleJasperReportsService.generatePDFReport("Simple_Blue.jrxml", "dealers-report.pdf", "ownerPassword","userPassword");

        log.debug("Report generated on the path {}", reportPath);

        reportCounter++;

        return ResponseEntity
            .noContent()
            .headers(createAlert(applicationName, "The report has been created successfully @ " + reportPath, String.valueOf(reportCounter++)))
            .build();
    }

    // TODO Implement report-headers-util
    public HttpHeaders createAlert(String applicationName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);

        try {
            headers.add("X-" + applicationName + "-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException var5) {
        }

        return headers;
    }
}
