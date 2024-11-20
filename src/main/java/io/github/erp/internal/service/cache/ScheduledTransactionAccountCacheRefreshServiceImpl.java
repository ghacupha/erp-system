package io.github.erp.internal.service.cache;

import io.github.erp.internal.service.ledgers.InternalTransactionAccountService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("scheduledTransactionAccountCacheRefreshService")
public class ScheduledTransactionAccountCacheRefreshServiceImpl  implements ScheduledCacheRefreshService {

    private final InternalTransactionAccountService internalDomainService;

    public ScheduledTransactionAccountCacheRefreshServiceImpl(InternalTransactionAccountService internalDomainService) {
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
