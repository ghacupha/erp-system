package io.github.erp.internal.report;

import java.util.Map;

/**
 * This service is similar to the pdf-reports-service however the report formats
 * namely csv, excel and html do not have direct implementation of password encryption
 * so you will not have those in the description
 */
public interface UnsecuredReportsService {

    /**
     * Creates the PDF report based on the parameters given the most important being the
     * report-template which is also the first argument. The result is a string with the
     * file location
     *
     * @param reportFileLocation Location of the report file i.e. the JRXML or Jasper file
     * @param reportName This is the name of the resulting report after jasper library has generated a PDF report
     * @param parameters Additional parameters for inclusion in the report
     * @return This is the full report path of the generated report
     */
    String generateReport(String reportFileLocation, String reportName, Map<String, Object> parameters);
}
