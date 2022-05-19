package io.github.erp.internal.report;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasperReportsSimpleConfiguration {

    @Bean
    public SimpleJasperReportExporter reportExporter() {
        return new SimpleJasperReportExporter();
    }
}
