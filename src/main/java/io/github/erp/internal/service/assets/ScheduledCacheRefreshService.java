package io.github.erp.internal.service.assets;

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
public class ScheduledCacheRefreshService {

    private final InternalAssetRegistrationService assetRegistrationService;

    public ScheduledCacheRefreshService(InternalAssetRegistrationService assetRegistrationService) {
        this.assetRegistrationService = assetRegistrationService;
    }

//    @Scheduled(fixedRate = 43200000) // Refresh cache every hour
//    public void refreshCache() {
//        // Fetch all IDs or a subset of IDs
//        List<Long> ids = assetRegistrationService.findAllIds();
//        for (Long id : ids) {
//            assetRegistrationService.findOne(id); // This does seem to refresh the cache
//        }
//    }

    @Scheduled(cron = "0 0 19 * * *") // Run every day at 19:00 (7:00 PM)
    public void refreshCacheAt1900Hours() {
        // Fetch all IDs or a subset of IDs
        List<Long> ids = assetRegistrationService.findAllIds();
        for (Long id : ids) {
            assetRegistrationService.findOne(id);
        }
    }

    @CacheEvict(cacheNames = "assetRegistrations", allEntries = true)
    public void clearCache() {
        // Manually clear the entire cache if needed
    }
}
