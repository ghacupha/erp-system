package io.github.erp.internal.report;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SimpleJasperReportsService {

    private SimpleJasperReportFiller simpleReportFiller;
    private SimpleJasperReportExporter simpleExporter;

    public SimpleJasperReportsService(SimpleJasperReportFiller simpleReportFiller, SimpleJasperReportExporter simpleExporter) {
        this.simpleReportFiller = simpleReportFiller;
        this.simpleExporter = simpleExporter;
    }

    public void generateReport() {

        simpleReportFiller.compileReport("templates/reports/Simple_Blue.jrxml");

        // todo simpleReportFiller.compileReport("templates/reports/employeeReport.jrxml");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Employee Report Example");
        parameters.put("minSalary", 15000.0);
        parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");

        simpleReportFiller.setParameters(parameters);
        simpleReportFiller.fillReport();

        simpleExporter.setJasperPrint(simpleReportFiller.getJasperPrint());

        simpleExporter.exportToPdf("generated-reports/employeeReport.pdf", "baeldung");
        simpleExporter.exportToXlsx("generated-reports/employeeReport.xlsx", "Employee Data");
        simpleExporter.exportToCsv("generated-reports/employeeReport.csv");
        simpleExporter.exportToHtml("generated-reports/employeeReport.html");

    }
}
