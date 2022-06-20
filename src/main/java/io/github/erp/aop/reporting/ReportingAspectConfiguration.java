package io.github.erp.aop.reporting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportingAspectConfiguration {

    @Bean
    public ReportRequisitionInterceptor reportRequisitionInterceptor() {

        return new ReportRequisitionInterceptor();
    }
}
