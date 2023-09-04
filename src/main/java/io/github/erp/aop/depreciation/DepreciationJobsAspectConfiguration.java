package io.github.erp.aop.depreciation;

import io.github.erp.erp.depreciation.DepreciationJobSequenceService;
import io.github.erp.service.dto.DepreciationJobDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepreciationJobsAspectConfiguration {

    @Autowired
    private DepreciationJobSequenceService<DepreciationJobDTO> depreciationJobSequenceService;

    @Bean
    public DepreciationRunInterceptor depreciationRunInterceptor() {

        return new DepreciationRunInterceptor(depreciationJobSequenceService);
    }
}
