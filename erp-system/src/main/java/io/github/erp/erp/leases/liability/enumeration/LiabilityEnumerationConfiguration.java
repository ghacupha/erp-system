package io.github.erp.erp.leases.liability.enumeration;

import io.github.erp.erp.leases.liability.enumeration.batch.LiabilityEnumerationBatchConfiguration;
import io.github.erp.erp.leases.liability.enumeration.batch.LiabilityEnumerationJobLauncher;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Explicit bean registration for the liability enumeration batch entry points
 * to avoid component-scan omissions when the module is started in isolation.
 */
@Configuration
@Import({ LiabilityEnumerationBatchConfiguration.class })
public class LiabilityEnumerationConfiguration {

    @Bean
    public LiabilityEnumerationJobLauncher liabilityEnumerationJobLauncher(
        JobLauncher jobLauncher,
        @Qualifier(LiabilityEnumerationBatchConfiguration.JOB_NAME) Job liabilityEnumerationJob
    ) {
        return new LiabilityEnumerationJobLauncher(jobLauncher, liabilityEnumerationJob);
    }

}
