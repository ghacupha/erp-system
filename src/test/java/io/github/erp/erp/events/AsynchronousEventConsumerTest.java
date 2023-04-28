package io.github.erp.erp.events;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

public class AsynchronousEventConsumerTest {

    @Test
    public void testEventConsumer() {
        EventListener listener = Mockito.mock(AsynchronousEventListenerImpl.class);
        AsynchronousEventConsumer synchronousEventListenerConsumer = new AsynchronousEventConsumer(listener);
        synchronousEventListenerConsumer.doAsynchronousOperation();

        verify(listener, timeout(1000).times(1)).onTrigger();
    }
}
