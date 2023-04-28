package io.github.erp.erp.index.engine_v1;

import org.springframework.boot.context.event.ApplicationReadyEvent;

public interface ControllerIndexingService {

    /**
     * Method called back when the index sequence is being invoked
     */
    void index();

    /**
     * This method is called to register a service which is to respond to the callback
     */
    void register();

}
