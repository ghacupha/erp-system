package io.github.erp.internal.service.cache;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.erp.startUp.cache.AbstractStartupCacheUpdateService;
import io.github.erp.internal.ErpCacheProperties;
import io.github.erp.internal.service.ledgers.InternalTransactionAccountService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("scheduledTransactionAccountCacheRefreshService")
public class ScheduledTransactionAccountCacheRefreshServiceImpl extends AbstractStartupCacheUpdateService implements ScheduledCacheRefreshService {

    private final InternalTransactionAccountService internalDomainService;

    public ScheduledTransactionAccountCacheRefreshServiceImpl(ErpCacheProperties cacheProperties, InternalTransactionAccountService internalDomainService) {
        super(cacheProperties);
        this.internalDomainService = internalDomainService;
    }

    public void refreshCache() {
        // Fetch all IDs or a subset of IDs
        List<Long> ids = internalDomainService.findAllIds();
        for (Long id : ids) {
            internalDomainService.findOne(id); // This does seem to refresh the cache
        }
    }

    @Scheduled(cron = "0 0 19 * * *") // Run every day at 19:00 (7:00 PM)
    public void refreshCacheAt1900Hours() {
        // Fetch all IDs or a subset of IDs
        List<Long> ids = internalDomainService.findAllIds();
        for (Long id : ids) {
            internalDomainService.findOne(id);
        }
    }

    public void refreshDefinedCacheItems(List<Long> ids) {
        // Fetch all IDs or a subset of IDs
        for (Long id : ids) {
            internalDomainService.findOne(id);
        }
    }

    @CacheEvict(cacheNames = "transactionAccounts", allEntries = true)
    public void clearCache() {
        // Manually clear the entire cache if needed
    }
}
