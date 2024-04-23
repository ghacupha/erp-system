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

/**
 * So yeah, it's what it is, this is probably overkill, the jasper-reports guys have said so themselves
 * that the API is there, but it's like they don't actually expect anyone to use it, and I tend to agree.
 * We really can apply some technology that is more native to Java, which means it will be stupid fast. And
 * fast is a good thing. So we are deprecating this one as well
 */
@Deprecated
@Service
public class CSVReportsService implements SecuredReportsService, UnsecuredReportsService {
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${erp.reportsDirectory}")
    private String reportsDirectory;

    private final SimpleJasperReportCompiler compiler;
    private final SimpleJasperReportFiller simpleReportFiller;
    private final SimpleJasperReportExporter simpleExporter;

    public CSVReportsService(SimpleJasperReportCompiler compiler, SimpleJasperReportFiller simpleReportFiller, SimpleJasperReportExporter simpleExporter) {
        this.compiler = compiler;
        this.simpleReportFiller = simpleReportFiller;
        this.simpleExporter = simpleExporter;
    }

    @Override
    public String generateReport(String reportFileLocation, String reportName, String ownerPassword, String userPassword, Map<String, Object> parameters) {

        // TODO Implement a password protected archive for the resulting file
        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);
        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);
        simpleExporter.setJasperPrint(print);
        simpleExporter.exportToCsv(reportsDirectory + reportName, applicationName);
        return reportsDirectory + reportName;
    }

    @Override
    public String generateReport(String reportFileLocation, String reportName, Map<String, Object> parameters) {

        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);
        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);
        simpleExporter.setJasperPrint(print);
        simpleExporter.exportToCsv(reportsDirectory + reportName, applicationName);
        return reportsDirectory + reportName;
    }
}
