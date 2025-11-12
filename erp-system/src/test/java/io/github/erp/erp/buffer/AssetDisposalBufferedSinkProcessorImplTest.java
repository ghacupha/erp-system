package io.github.erp.erp.buffer;

import static io.github.erp.erp.assets.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.github.erp.domain.AssetDisposal;
import io.github.erp.internal.repository.InternalAssetDisposalRepository;
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
class AssetDisposalBufferedSinkProcessorImplTest {

    @Mock
    private InternalAssetDisposalRepository internalAssetDisposalRepository;

    private AssetDisposalBufferedSinkProcessorImpl processor;

    @BeforeEach
    void setUp() {
        processor = spy(new AssetDisposalBufferedSinkProcessorImpl(internalAssetDisposalRepository));
    }

    @AfterEach
    void tearDown() {
        processor.shutdown();
    }

    @Test
    void thresholdFlushPersistsPreferredBatchSize() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<AssetDisposal>> savedEntries = new AtomicReference<>();

        doAnswer(invocation -> {
                List<AssetDisposal> entries = invocation.getArgument(0);
                savedEntries.set(entries);
                latch.countDown();
                return entries;
            })
            .when(internalAssetDisposalRepository)
            .saveAll(anyList());

        for (int i = 0; i < PREFERRED_BATCH_SIZE; i++) {
            processor.addEntry(new AssetDisposal());
        }

        assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
        List<AssetDisposal> persistedEntries = savedEntries.get();
        assertThat(persistedEntries).isNotNull();
        assertThat(persistedEntries).hasSize(PREFERRED_BATCH_SIZE);
    }

    @Test
    void flushRemainingItemsFlushesBufferAndRestartsAfterShutdown() throws Exception {
        CountDownLatch firstLatch = new CountDownLatch(1);
        CountDownLatch secondLatch = new CountDownLatch(1);

        AtomicReference<List<AssetDisposal>> firstSavedEntries = new AtomicReference<>();
        AtomicReference<List<AssetDisposal>> secondSavedEntries = new AtomicReference<>();
        AtomicInteger invocationCounter = new AtomicInteger();

        doAnswer(invocation -> {
                List<AssetDisposal> entries = invocation.getArgument(0);
                if (invocationCounter.getAndIncrement() == 0) {
                    firstSavedEntries.set(entries);
                    firstLatch.countDown();
                } else {
                    secondSavedEntries.set(entries);
                    secondLatch.countDown();
                }
                return entries;
            })
            .when(internalAssetDisposalRepository)
            .saveAll(anyList());

        AssetDisposal firstEntry = new AssetDisposal();
        processor.addEntry(firstEntry);
        processor.flushRemainingItems();

        assertThat(firstLatch.await(5, TimeUnit.SECONDS)).isTrue();
        List<AssetDisposal> firstPersisted = firstSavedEntries.get();
        assertThat(firstPersisted).isNotNull();
        assertThat(firstPersisted).containsExactly(firstEntry);

        verify(processor, times(1)).shutdown();

        AssetDisposal secondEntry = new AssetDisposal();
        processor.addEntry(secondEntry);
        processor.flushRemainingItems();

        assertThat(secondLatch.await(5, TimeUnit.SECONDS)).isTrue();
        List<AssetDisposal> secondPersisted = secondSavedEntries.get();
        assertThat(secondPersisted).isNotNull();
        assertThat(secondPersisted).containsExactly(secondEntry);
    }
}
