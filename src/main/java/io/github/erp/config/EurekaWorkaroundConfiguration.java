// This is a workaround for
// https://github.com/jhipster/jhipster-registry/issues/537
// https://github.com/jhipster/generator-jhipster/issues/18533
// The original issue will be fixed with spring cloud 2021.0.4
// https://github.com/spring-cloud/spring-cloud-netflix/issues/3941
package io.github.erp.config;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
