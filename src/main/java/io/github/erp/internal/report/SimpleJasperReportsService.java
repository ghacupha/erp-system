package io.github.erp.internal.report;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SimpleJasperReportsService {

    private final SimpleJasperReportCompiler compiler;
    private SimpleJasperReportFiller simpleReportFiller;
    private SimpleJasperReportExporter simpleExporter;

    public SimpleJasperReportsService(SimpleJasperReportCompiler compiler, SimpleJasperReportFiller simpleReportFiller, SimpleJasperReportExporter simpleExporter) {
        this.compiler = compiler;
        this.simpleReportFiller = simpleReportFiller;
        this.simpleExporter = simpleExporter;
    }

    public void generateReport() {

        JasperReport compiledReport = compiler.compileReport("templates/reports/Simple_Blue.jrxml");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Dealers Report Example");

        JasperPrint print = simpleReportFiller.fillReport(compiledReport, parameters);

        simpleExporter.setJasperPrint(print);

        simpleExporter.exportToPdf("generated-reports/employeeReport.pdf", "baeldung");
        simpleExporter.exportToXlsx("generated-reports/employeeReport.xlsx", "Employee Data");
        simpleExporter.exportToCsv("generated-reports/employeeReport.csv");
        simpleExporter.exportToHtml("generated-reports/employeeReport.html");

    }
}
