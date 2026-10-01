package io.github.erp.erp.leases.liability.enumeration;

/*-
 * Erp System - Mark X No 12 (Kadar Series) Server ver 1.9.0
 * Copyright © 2021 - 2026 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
