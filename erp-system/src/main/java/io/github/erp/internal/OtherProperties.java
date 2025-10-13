package io.github.erp.internal;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.config.AppPropertyFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app")
@PropertySources({
    @PropertySource(value = "classpath:config/application.yml", factory = AppPropertyFactory.class),
    @PropertySource(value = "classpath:config/application-dev.yml", factory = AppPropertyFactory.class),
    @PropertySource(value = "classpath:config/application-prod.yml", factory = AppPropertyFactory.class),
})
public class OtherProperties {

    private String title;

    @Data
    public static class SandboxInstance {
        private String urls;
    }

    @Data
    public static class ReIndexer {
        private String interval;
    }
}
