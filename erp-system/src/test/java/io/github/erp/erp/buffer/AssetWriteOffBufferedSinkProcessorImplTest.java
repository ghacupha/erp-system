package io.github.erp.erp.buffer;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static io.github.erp.erp.assets.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.github.erp.domain.AssetWriteOff;
import io.github.erp.internal.repository.InternalAssetWriteOffRepository;
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
class AssetWriteOffBufferedSinkProcessorImplTest {

    @Mock
    private InternalAssetWriteOffRepository internalAssetWriteOffRepository;

    private AssetWriteOffBufferedSinkProcessorImpl processor;

    @BeforeEach
    void setUp() {
        processor = spy(new AssetWriteOffBufferedSinkProcessorImpl(internalAssetWriteOffRepository));
    }

    @AfterEach
    void tearDown() {
        processor.shutdown();
    }

    @Test
    void thresholdFlushPersistsPreferredBatchSize() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<AssetWriteOff>> savedEntries = new AtomicReference<>();

        doAnswer(invocation -> {
                List<AssetWriteOff> entries = invocation.getArgument(0);
                savedEntries.set(entries);
                latch.countDown();
                return entries;
            })
            .when(internalAssetWriteOffRepository)
            .saveAll(anyList());

        for (int i = 0; i < PREFERRED_BATCH_SIZE; i++) {
            processor.addEntry(new AssetWriteOff());
        }

        assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
        List<AssetWriteOff> persistedEntries = savedEntries.get();
        assertThat(persistedEntries).isNotNull();
        assertThat(persistedEntries).hasSize(PREFERRED_BATCH_SIZE);
    }

    @Test
    void flushRemainingItemsFlushesBufferAndRestartsAfterShutdown() throws Exception {
        CountDownLatch firstLatch = new CountDownLatch(1);
        CountDownLatch secondLatch = new CountDownLatch(1);

        AtomicReference<List<AssetWriteOff>> firstSavedEntries = new AtomicReference<>();
        AtomicReference<List<AssetWriteOff>> secondSavedEntries = new AtomicReference<>();
        AtomicInteger invocationCounter = new AtomicInteger();

        doAnswer(invocation -> {
                List<AssetWriteOff> entries = invocation.getArgument(0);
                if (invocationCounter.getAndIncrement() == 0) {
                    firstSavedEntries.set(entries);
                    firstLatch.countDown();
                } else {
                    secondSavedEntries.set(entries);
                    secondLatch.countDown();
                }
                return entries;
            })
            .when(internalAssetWriteOffRepository)
            .saveAll(anyList());

        AssetWriteOff firstEntry = new AssetWriteOff();
        processor.addEntry(firstEntry);
        processor.flushRemainingItems();

        assertThat(firstLatch.await(5, TimeUnit.SECONDS)).isTrue();
        List<AssetWriteOff> firstPersisted = firstSavedEntries.get();
        assertThat(firstPersisted).isNotNull();
        assertThat(firstPersisted).containsExactly(firstEntry);

        verify(processor, times(1)).shutdown();

        AssetWriteOff secondEntry = new AssetWriteOff();
        processor.addEntry(secondEntry);
        processor.flushRemainingItems();

        assertThat(secondLatch.await(5, TimeUnit.SECONDS)).isTrue();
        List<AssetWriteOff> secondPersisted = secondSavedEntries.get();
        assertThat(secondPersisted).isNotNull();
        assertThat(secondPersisted).containsExactly(secondEntry);
    }
}
