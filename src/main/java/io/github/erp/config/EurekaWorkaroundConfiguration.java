// This is a workaround for
// https://github.com/jhipster/jhipster-registry/issues/537
// https://github.com/jhipster/generator-jhipster/issues/18533
// The original issue will be fixed with spring cloud 2021.0.4
// https://github.com/spring-cloud/spring-cloud-netflix/issues/3941
package io.github.erp.config;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaWorkaroundConfiguration implements HealthIndicator {

    private boolean applicationIsUp = false;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        this.applicationIsUp = true;
    }

    @Override
    public Health health() {
        if (!applicationIsUp) {
            return Health.down().build();
        }
        return Health.up().build();
    }
}
