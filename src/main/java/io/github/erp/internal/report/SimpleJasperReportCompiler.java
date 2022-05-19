package io.github.erp.internal.report;

import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class SimpleJasperReportCompiler {

    @SneakyThrows
    public JasperReport compileReport(String reportFileName) {
        // InputStream reportStream = getClass().getResourceAsStream(reportFileName);
        File reportFile = new File(reportFileName);

        InputStream reportStream = new FileInputStream(reportFile);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        return saveReport(jasperReport, reportFileName);
    }

    private JasperReport saveReport(JasperReport jasperReport, String reportFileName) throws JRException {
        JRSaver.saveObject(jasperReport, reportFileName.replace(".jrxml", ".jasper"));

        return jasperReport;
    }
}
