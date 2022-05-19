package io.github.erp.internal.report;

import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class SimpleJasperReportCompiler {

    @SneakyThrows
    public JasperReport compileReport(String reportFileName) {
        InputStream reportStream = getClass().getResourceAsStream("/".concat(reportFileName));
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        return saveReport(jasperReport);
    }

    private JasperReport saveReport(JasperReport jasperReport) {
        // TODO JRSaver.saveObject(jasperReport, reportFileName.replace(".jrxml", ".jasper"));

        return jasperReport;
    }
}
