package io.github.erp.erp.index.engine_v1;

import org.springframework.stereotype.Service;

/**
 * Once this service is triggered it runs the indexing method on all registered services
 */
@Service("AsynchronousIndexingService")
public class AsynchronousIndexingService {

    public void startAsynchronousIndex(){
        System.out.println("Performing operation in Asynchronous Task");

        IndexingServiceChainSingleton.getInstance().getRegisteredIndexingServiceList().forEach(service -> {
            new Thread(service::index).start();
        });
    }

}
