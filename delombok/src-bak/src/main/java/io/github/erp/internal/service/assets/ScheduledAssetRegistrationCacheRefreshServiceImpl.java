package io.github.erp.internal.service.assets;

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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is our hail-mary on the asset-registration challenge that is really affecting our
 * asset relationships. We just hope this improves on our current situtation
 */
@Service
public class ScheduledAssetRegistrationCacheRefreshServiceImpl implements ScheduledAssetRegistrationCacheRefreshService {

    private final InternalAssetRegistrationService assetRegistrationService;

    public ScheduledAssetRegistrationCacheRefreshServiceImpl(InternalAssetRegistrationService assetRegistrationService) {
        this.assetRegistrationService = assetRegistrationService;
    }

    public void refreshCache() {
        // Fetch all IDs or a subset of IDs
        List<Long> ids = assetRegistrationService.findAllIds();
        for (Long id : ids) {
            assetRegistrationService.findOne(id); // This does seem to refresh the cache
        }
    }

    // @Scheduled(cron = "0 0 19 * * *") // Run every day at 19:00 (7:00 PM)
    public void refreshCacheAt1900Hours() {
        // Fetch all IDs or a subset of IDs
        List<Long> ids = assetRegistrationService.findAllIds();
        for (Long id : ids) {
            assetRegistrationService.findOne(id);
        }
    }

    public void refreshDefinedCacheItems(List<Long> ids) {
        // Fetch all IDs or a subset of IDs
        for (Long id : ids) {
            assetRegistrationService.findOne(id);
        }
    }

    @CacheEvict(cacheNames = "assetRegistrations", allEntries = true)
    public void clearCache() {
        // Manually clear the entire cache if needed
    }
}
