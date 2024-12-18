
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
package io.github.erp.erp.startUp.cache;

import io.github.erp.internal.ErpCacheProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public abstract class AbstractStartupCacheUpdateService implements ApplicationListener<ApplicationReadyEvent> {

    private final boolean teardownRebuildCache;

    private final boolean rebuildCache;

    public AbstractStartupCacheUpdateService(
        ErpCacheProperties cacheProperties
    ) {
        this.rebuildCache = cacheProperties.isRebuildCache();
        this.teardownRebuildCache = cacheProperties.isTeardownRebuildCache();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        if (teardownRebuildCache && rebuildCache) {
            clearCache();

            refreshCache();
        }

        if (rebuildCache) {
            refreshCache();
        }

    }

    public abstract void refreshCache();

    public abstract void clearCache();
}
