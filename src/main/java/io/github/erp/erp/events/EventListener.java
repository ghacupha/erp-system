package io.github.erp.erp.events;

/**
 * Asynchronous listener
 */
public interface EventListener extends java.util.EventListener {

    /**
     * Returns true upon successful task completion
     */
    String onTrigger();

    /**
     * This method is asynchrnously called by the client
     */
    void respondToTrigger();
}