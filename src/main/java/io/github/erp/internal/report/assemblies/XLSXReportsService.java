package io.github.erp.internal.report.assemblies;

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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Generates a report in the XLSX format. Please note due to the unavailability of the
 * encryption API, the file is wrapped in a password protected archive instead
 */
@Service
public class XLSXReportsService implements SecuredReportsService, UnsecuredReportsService {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${erp.reportsDirectory}")
    private String reportsDirectory;

    private final SimpleJasperReportCompiler compiler;
    private final SimpleJasperReportFiller simpleReportFiller;
    private final SimpleJasperReportExporter simpleExporter;

    public XLSXReportsService(SimpleJasperReportCompiler compiler, SimpleJasperReportFiller simpleReportFiller, SimpleJasperReportExporter simpleExporter) {
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
        simpleExporter.exportToXlsx(reportsDirectory + reportName, applicationName);
        return reportsDirectory + reportName;
    }

    @Override
    public String generateReport(String reportFileLocation, String reportName, Map<String, Object> parameters) {

        JasperReport compiledReport = compiler.compileReport(reportsDirectory + reportFileLocation);
        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);
        simpleExporter.setJasperPrint(print);
        simpleExporter.exportToXlsx(reportsDirectory + reportName, applicationName);
        return reportsDirectory + reportName;
    }

}
