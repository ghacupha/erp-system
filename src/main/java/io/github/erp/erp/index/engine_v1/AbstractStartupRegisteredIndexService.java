package io.github.erp.erp.index.engine_v1;

import io.github.erp.erp.index.api.ApplicationIndexingService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * This objects updates registration of index services on startup. The registered services can then be invoked
 * at runtime to recreate or update the existing index
 */
public abstract class AbstractStartupRegisteredIndexService implements ControllerIndexingService, ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger("IndexingServiceRegistrationSequence");

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent applicationReadyEvent) {
        log.info("Index Service registration initiated. Standby for next sequence");

        register();
    }

    public abstract void register();
}
