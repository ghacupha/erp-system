package io.github.erp.internal.report;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
