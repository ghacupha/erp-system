package io.github.erp.erp.buffer;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.AssetWriteOff;
import io.github.erp.erp.assets.nbv.buffer.BufferedSinkProcessor;
import io.github.erp.internal.repository.InternalAssetWriteOffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static io.github.erp.erp.assets.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;

@Transactional
@Service
public class AssetWriteOffBufferedSinkProcessorImpl implements BufferedSinkProcessor<AssetWriteOff> {

    private final static Logger log = LoggerFactory.getLogger(AssetWriteOffBufferedSinkProcessorImpl.class);

    private final InternalAssetWriteOffRepository internalNetBookValueEntryRepository;

    private final List<AssetWriteOff> buffer = new ArrayList<>();

    private final int batchSizeThreshold = PREFERRED_BATCH_SIZE;


    private final long flushIntervalMillis = 5000;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private ScheduledFuture<?> flushTask;

    // Argh! Additional plumbing was necessary after all
    private ScheduledFuture<?> flushStuckTask;

    // Needed for the "additional plumbing"
    private final long maxFlushDelayMillis = 60000 * 15; // Max delay before forcing flush (15 minutes)

    boolean stuckFlushComplete = false;

    private boolean isShutdown = false;

    public AssetWriteOffBufferedSinkProcessorImpl(InternalAssetWriteOffRepository internalNetBookValueEntryRepository) {
        this.internalNetBookValueEntryRepository = internalNetBookValueEntryRepository;
    }

    public void addEntry(AssetWriteOff entry) {

        if (isShutdown) {
            startup(); // Start the executor if it's shutdown
        }

        buffer.add(entry);

        if (buffer.size() >= batchSizeThreshold) {

            scheduleFlush();

        } else if (flushTask == null) {

            scheduleFlushWithDelay();
        }
    }

    private void scheduleFlush() {

        if (flushTask != null) {
            flushTask.cancel(false);
        }
        flushTask = null;
        flushBufferWithCount();
    }

    public void shutdown() {
        if (!isShutdown) {
            log.warn("The buffer now shutting down; standby...");
            executor.shutdown();
            isShutdown = true;
        }
    }

    public void startup() {
        if (isShutdown) {
            log.info("Starting up the buffer processor...");
            executor = Executors.newScheduledThreadPool(2); // Recreate the executor
            isShutdown = false;
            // Schedule flush tasks again if needed
            if (buffer.size() >= batchSizeThreshold) {
                scheduleFlush();
            } else if (flushTask == null) {
                scheduleFlushWithDelay();
            }
        }
    }

    public void flushRemainingItems() {

        log.info("We have reached the last batch and are attempting to flush the buffer; standby...");

        this.flushBufferWithCount();

        this.shutdown();
    }

    private void scheduleFlushWithDelay() {
        if (flushTask != null) {
            flushTask.cancel(false);
        }
        flushTask = executor.schedule(this::flushBufferWithCount, flushIntervalMillis, TimeUnit.MILLISECONDS);
    }

    private void flushBufferWithCount() {

        log.info("Depreciation buffer flushing {} items to the sink", buffer.size());

        if (!buffer.isEmpty()) {

            int numberOfPersistedEntries = this.flushBuffer();

            log.info("{} entries successfully saved to the data-sink.", numberOfPersistedEntries);
        }
    }

    private int flushBuffer() {

        int flushedItems = buffer.size();

        log.info("NBV buffer flushing {} items to the sink", flushedItems);

        this.saveEntries();

        return flushedItems;
    }

    private void saveEntries() {

        if (!buffer.isEmpty()) {

            List<AssetWriteOff> entriesToPersist = new ArrayList<>(buffer);

            log.info("Clearing buffer...");

            buffer.clear();

            log.info("Saving {} entries to the database", entriesToPersist.size());

            executor.execute(() -> internalNetBookValueEntryRepository.saveAll(entriesToPersist));
        }
    }

    @PostConstruct
    private void scheduleFlushStuckTask() {
        flushStuckTask = executor.scheduleAtFixedRate(this::checkFlushStuck, maxFlushDelayMillis, maxFlushDelayMillis, TimeUnit.MILLISECONDS);
    }

    public void flushStuckTaskComplete() {
        this.stuckFlushComplete = true;
    }

    // Additional plumbing
    @Async
    @Scheduled(fixedRate = 5000)
    void checkFlushStuck() {
        if (!stuckFlushComplete) {
            // Check if the buffer hasn't been flushed for a long time
            long lastFlushTime = System.currentTimeMillis() - flushIntervalMillis;

            if (flushTask != null && flushTask.getDelay(TimeUnit.MILLISECONDS) > maxFlushDelayMillis && flushStuckTask != null && flushStuckTask.getDelay(TimeUnit.MILLISECONDS) > maxFlushDelayMillis) {
                log.error("Forcing flush of the buffer due to long delay in flushing; last flush time: {}", lastFlushTime);
                scheduleFlush();
            }

            stuckFlushComplete = true;
        }
    }
}
