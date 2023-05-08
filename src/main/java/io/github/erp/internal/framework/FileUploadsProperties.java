package io.github.erp.internal.framework;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.1
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
 * Configurations related to file-upload process
 */
@Configuration
@ConfigurationProperties(prefix = "reader")
@PropertySource(value = "classpath:config/fileUploads.yml", factory = AppPropertyFactory.class)
public class FileUploadsProperties {

    private int listSize;

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public int getListSize() {
        return listSize;
    }
}
