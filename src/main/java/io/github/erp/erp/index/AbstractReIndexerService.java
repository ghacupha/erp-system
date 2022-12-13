package io.github.erp.erp.index;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Implementation of independent cleanup of the index. The simple implementation involves tearing
 * down the index and building it up again using entries from the DB
 */
public abstract class AbstractReIndexerService implements ApplicationIndexingService, ApplicationReindexingService, ApplicationIndexTearDownService {

    @Override
    @Scheduled(fixedDelayString = "${erp.reIndexer.interval}")
    public void reIndex() {
        this.tearDown();
        this.index();
    }

    @Override
    public abstract void index();

    @Override
    public abstract void tearDown();
}
