package io.github.erp.erp.events;

/**
 * This is an illustration's sample implementation
 */
public class AsynchronousEventConsumer {

    private EventListener listener;

    public AsynchronousEventConsumer(EventListener listener) {
        this.listener = listener;
    }

    public void doAsynchronousOperation(){
        System.out.println("Performing operation in Asynchronous Task");

        new Thread(() -> listener.onTrigger()).start();
    }
}
