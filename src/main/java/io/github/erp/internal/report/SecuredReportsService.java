package io.github.erp.internal.report;

import java.util.Map;

/**
 * Creates reports in PDF format, authenticated by both user password and owner password
 */
public interface SecuredReportsService {

    /**
     * Creates the PDF report based on the parameters given the most important being the
     * report-template which is also the first argument. The result is a string with the
     * file location
     *
     * @param reportFileLocation Location of the report file i.e. the JRXML or Jasper file
     * @param reportName This is the name of the resulting report after jasper library has generated a PDF report
     * @param ownerPassword Owner password on the report
     * @param userPassword User password on the report
     * @param parameters Additional parameters for inclusion in the report
     * @return This is the full report path of the generated report
     */
    String generateReport(String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters);
}
