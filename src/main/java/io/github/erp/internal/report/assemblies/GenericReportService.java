package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GenericReportService {


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${erp.reportsDirectory}")
    private String reportsDirectory;

    private final SimpleJasperReportCompiler compiler;
    private final SimpleJasperReportFiller simpleReportFiller;
    private final SimpleJasperReportExporter simpleExporter;

    public GenericReportService(SimpleJasperReportCompiler compiler, SimpleJasperReportFiller simpleReportFiller, SimpleJasperReportExporter simpleExporter) {
        this.compiler = compiler;
        this.simpleReportFiller = simpleReportFiller;
        this.simpleExporter = simpleExporter;
    }

    public String generateReport(String contentTypeName, String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters){
        String reportPath = "";

        switch (contentTypeName) {
            case "PDF File":
                reportPath = generatePDFReport(reportFileLocation, reportName, ownerPassword, userPassword, parameters);
                break;
            case "XLSX File":
                reportPath = generateXLSXReport(reportFileLocation, reportName, ownerPassword, userPassword, parameters);
                break;
            case "CSV File":
                reportPath = generateCSVReport(reportFileLocation, reportName, ownerPassword, userPassword, parameters, ",");
                break;
            case "HTML File":
                reportPath = generateHTMLReport(reportFileLocation, reportName, ownerPassword, userPassword, parameters);
                break;
            default: {
                reportPath = generatePDFReport(reportFileLocation, reportName, ownerPassword, userPassword, parameters);
            }
        }

        return reportPath;
    }

    public String generateXLSXReport(String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters) {
        // TODO Implement a password protected archive for the resulting file
        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);
        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);
        simpleExporter.setJasperPrint(print);
        simpleExporter.exportToXlsx(reportsDirectory + reportName, applicationName);
        return reportsDirectory + reportName;
    }

    public String generatePDFReport(String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters) {
        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);
        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);
        simpleExporter.setJasperPrint(print);
        simpleExporter.exportToPdf(reportsDirectory + reportName, applicationName, ownerPassword,userPassword);
        return reportsDirectory + reportName;
    }

    public String generateHTMLReport(String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters) {
        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);
        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);
        simpleExporter.setJasperPrint(print);
        simpleExporter.exportToHtml(reportsDirectory + reportName);
        return reportsDirectory + reportName;
    }

    public String generateCSVReport(String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters, String fieldDelimiter) {
        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);
        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);
        simpleExporter.setJasperPrint(print);
        simpleExporter.exportToCsv(reportsDirectory + reportName, fieldDelimiter);
        return reportsDirectory + reportName;
    }
}
