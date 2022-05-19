package io.github.erp.internal.report;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class SimpleJasperReportFiller {

    private JasperReport jasperReport;
    private JasperPrint jasperPrint;

    private final DataSource dataSource;

    private Map<String, Object> parameters;

    public SimpleJasperReportFiller(DataSource dataSource) {
        parameters = new HashMap<>();
        this.dataSource = dataSource;
    }

    @SneakyThrows
    public void compileReport(String reportFileName) {
        InputStream reportStream = getClass().getResourceAsStream("/".concat(reportFileName));
        jasperReport = JasperCompileManager.compileReport(reportStream);
        // TODO JRSaver.saveObject(jasperReport, reportFileName.replace(".jrxml", ".jasper"));
    }

    @SneakyThrows
    public void fillReport() {
        jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
    }
}
