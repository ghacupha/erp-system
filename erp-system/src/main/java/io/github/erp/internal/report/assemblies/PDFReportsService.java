package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import java.util.HashMap;
import java.util.Map;

/**
 * This is also an early prototype for the implementation of jasper reports that will be
 * removed in a future update
 */
@Deprecated
@Service
public class PDFReportsService implements SecuredReportsService, UnsecuredReportsService {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${erp.reportsDirectory}")
    private String reportsDirectory;

    private final SimpleJasperReportCompiler compiler;
    private final SimpleJasperReportFiller simpleReportFiller;
    private final SimpleJasperReportExporter simpleExporter;

    public PDFReportsService(SimpleJasperReportCompiler compiler, SimpleJasperReportFiller simpleReportFiller, SimpleJasperReportExporter simpleExporter) {
        this.compiler = compiler;
        this.simpleReportFiller = simpleReportFiller;
        this.simpleExporter = simpleExporter;
    }

    public void generateReport(String resourceLocation) {

        JasperReport compiledReport = compiler.compileReport(reportsDirectory + resourceLocation);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Dealers Report Example");

        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);

        simpleExporter.setJasperPrint(print);

        simpleExporter.exportToPdf(reportsDirectory + "employeeReport.pdf", applicationName, "ownerPassword","userPassword");
        simpleExporter.exportToXlsx(reportsDirectory + "employeeReport.xlsx", "Employee Data");
        simpleExporter.exportToCsv(reportsDirectory + "employeeReport.csv");
        simpleExporter.exportToHtml(reportsDirectory + "employeeReport.html");

    }

    @Override
    public String generateReport(String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters) {

        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);

        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);

        simpleExporter.setJasperPrint(print);

        simpleExporter.exportToPdf(reportsDirectory + reportName, applicationName, ownerPassword,userPassword);

        return reportsDirectory + reportName;
    }

    @Override
    public String generateReport(String reportFileLocation, String reportName, Map<String, Object> parameters) {
        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);

        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);

        simpleExporter.setJasperPrint(print);

        simpleExporter.exportToPdf(reportsDirectory + reportName, applicationName);

        return reportsDirectory + reportName;
    }
}
