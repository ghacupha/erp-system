package io.github.erp.internal.report;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SimpleJasperReportsService {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${erp.reportsDirectory}")
    private String reportsDirectory;

    private final SimpleJasperReportCompiler compiler;
    private final SimpleJasperReportFiller simpleReportFiller;
    private final SimpleJasperReportExporter simpleExporter;

    public SimpleJasperReportsService(SimpleJasperReportCompiler compiler, SimpleJasperReportFiller simpleReportFiller, SimpleJasperReportExporter simpleExporter) {
        this.compiler = compiler;
        this.simpleReportFiller = simpleReportFiller;
        this.simpleExporter = simpleExporter;
    }

    public void generateReport() {

        // todo JasperReport compiledReport = compiler.compileReport("generated-reports/Simple_Blue.jrxml");
        JasperReport compiledReport = compiler.compileReport(reportsDirectory + "Simple_Blue.jrxml");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Dealers Report Example");

        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);

        simpleExporter.setJasperPrint(print);

        simpleExporter.exportToPdf(reportsDirectory + "employeeReport.pdf", applicationName, "ownerPassword","userPassword");
        simpleExporter.exportToXlsx(reportsDirectory + "employeeReport.xlsx", "Employee Data");
        simpleExporter.exportToCsv(reportsDirectory + "employeeReport.csv");
        simpleExporter.exportToHtml(reportsDirectory + "employeeReport.html");

    }
}
