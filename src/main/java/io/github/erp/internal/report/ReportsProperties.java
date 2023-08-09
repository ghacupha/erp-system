package io.github.erp.internal.report;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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
import io.github.erp.config.AppPropertyFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * This configuration gives us the exact location where we can dump the jasper templates and
 * compiled reports
 */
@Configuration
@ConfigurationProperties(prefix = "erp")
@PropertySource(value = "classpath:config/erpConfigs.yml", factory = AppPropertyFactory.class)
public class ReportsProperties {

    private String reportsDirectory;

    private String businessDocsDirectory;

    public String getReportsDirectory() {
        return reportsDirectory;
    }

    public void setReportsDirectory(String reportsDirectory) {
        this.reportsDirectory = reportsDirectory;
    }

    public String getBusinessDocsDirectory() {
        return businessDocsDirectory;
    }

    public void setBusinessDocsDirectory(String businessDocsDirectory) {
        this.businessDocsDirectory = businessDocsDirectory;
    }
}
