package io.github.erp.internal.report;

import lombok.Data;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Data
@Service
public class SimpleJasperReportFiller {

    private final DataSource dataSource;

    public SimpleJasperReportFiller(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows
    public JasperPrint fillReport(JasperReport compiledReport, Map<String, Object> parameters) {
        return JasperFillManager.fillReport(compiledReport, parameters, dataSource.getConnection());
    }
}
