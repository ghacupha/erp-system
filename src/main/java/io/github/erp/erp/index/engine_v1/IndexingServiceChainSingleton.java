package io.github.erp.erp.index.engine_v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains the list of indexing services registered
 */
public class IndexingServiceChainSingleton {

    private static IndexingServiceChainSingleton instance;

    private IndexingServiceChainSingleton(){}

    private static final Logger log = LoggerFactory.getLogger(IndexingServiceChainSingleton.class);

    private int serviceCount = 1;

    private final List<ControllerIndexingService> indexingServices = new ArrayList<>();

    public static IndexingServiceChainSingleton getInstance() {
        if (instance == null) {
            synchronized (IndexingServiceChainSingleton.class) {
                if (instance == null) {
                    instance = new IndexingServiceChainSingleton();
                }
            }
        }
        return instance;
    }

    public void registerService(ControllerIndexingService service) {

        log.info("Registering service # {}", this.serviceCount++);

        this.indexingServices.add(service);
    }


    List<ControllerIndexingService> getRegisteredIndexingServiceList() {

        log.info("Deploying {} registered services", indexingServices.size());

        return indexingServices;
    }
}
