package io.github.erp.erp.assets.nbv.buffer;

import static io.github.erp.erp.assets.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.internal.repository.InternalNetBookValueEntryRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NBVEntryBufferedSinkProcessorImplTest {

    @Mock
    private InternalNetBookValueEntryRepository internalNetBookValueEntryRepository;

    private NBVEntryBufferedSinkProcessorImpl processor;

    @BeforeEach
    void setUp() {
        processor = spy(new NBVEntryBufferedSinkProcessorImpl(internalNetBookValueEntryRepository));
    }

    @AfterEach
    void tearDown() {
        processor.shutdown();
    }

    @Test
    void thresholdFlushPersistsPreferredBatchSize() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<NetBookValueEntry>> savedEntries = new AtomicReference<>();

        doAnswer(invocation -> {
                List<NetBookValueEntry> entries = invocation.getArgument(0);
                savedEntries.set(entries);
                latch.countDown();
                return entries;
            })
            .when(internalNetBookValueEntryRepository)
            .saveAll(anyList());

        for (int i = 0; i < PREFERRED_BATCH_SIZE; i++) {
            processor.addEntry(new NetBookValueEntry());
        }

        assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
        List<NetBookValueEntry> persistedEntries = savedEntries.get();
        assertThat(persistedEntries).isNotNull();
        assertThat(persistedEntries).hasSize(PREFERRED_BATCH_SIZE);
    }

    @Test
    void flushRemainingItemsFlushesBufferAndRestartsAfterShutdown() throws Exception {
        CountDownLatch firstLatch = new CountDownLatch(1);
        CountDownLatch secondLatch = new CountDownLatch(1);

        AtomicReference<List<NetBookValueEntry>> firstSavedEntries = new AtomicReference<>();
        AtomicReference<List<NetBookValueEntry>> secondSavedEntries = new AtomicReference<>();
        AtomicInteger invocationCounter = new AtomicInteger();

        doAnswer(invocation -> {
                List<NetBookValueEntry> entries = invocation.getArgument(0);
                if (invocationCounter.getAndIncrement() == 0) {
                    firstSavedEntries.set(entries);
                    firstLatch.countDown();
                } else {
                    secondSavedEntries.set(entries);
                    secondLatch.countDown();
                }
                return entries;
            })
            .when(internalNetBookValueEntryRepository)
            .saveAll(anyList());

        NetBookValueEntry firstEntry = new NetBookValueEntry();
        processor.addEntry(firstEntry);
        processor.flushRemainingItems();

        assertThat(firstLatch.await(5, TimeUnit.SECONDS)).isTrue();
        List<NetBookValueEntry> firstPersisted = firstSavedEntries.get();
        assertThat(firstPersisted).isNotNull();
        assertThat(firstPersisted).containsExactly(firstEntry);

        verify(processor, times(1)).shutdown();

        NetBookValueEntry secondEntry = new NetBookValueEntry();
        processor.addEntry(secondEntry);
        processor.flushRemainingItems();

        assertThat(secondLatch.await(5, TimeUnit.SECONDS)).isTrue();
        List<NetBookValueEntry> secondPersisted = secondSavedEntries.get();
        assertThat(secondPersisted).isNotNull();
        assertThat(secondPersisted).containsExactly(secondEntry);
    }
}
