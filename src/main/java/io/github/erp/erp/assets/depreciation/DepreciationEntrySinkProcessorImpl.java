package io.github.erp.erp.assets.depreciation;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.erp.assets.depreciation.context.DepreciationJobContext;
import io.github.erp.internal.service.assets.InternalDepreciationEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static io.github.erp.erp.assets.depreciation.DepreciationJobSequenceServiceImpl.PREFERRED_BATCH_SIZE;

@Transactional
@Service
public class DepreciationEntrySinkProcessorImpl implements DepreciationEntrySinkProcessor {

    private final static Logger log = LoggerFactory.getLogger(DepreciationEntrySinkProcessorImpl.class);

    private final InternalDepreciationEntryService depreciationEntryService;

    private List<DepreciationEntry> buffer = new ArrayList<>();
    private final int batchSizeThreshold = PREFERRED_BATCH_SIZE;


    private final long flushIntervalMillis = 5000;

    // private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private DepreciationJobContext contextManager = DepreciationJobContext.getInstance();

    private ScheduledFuture<?> flushTask;

    // Argh! Additional plumbing was necessary after all
    private ScheduledFuture<?> flushStuckTask;

    // Needed for the "additional plumbing"
    private final long maxFlushDelayMillis = 60000; // Max delay before forcing flush (1 minute)

    private boolean isShutdown = false;

    public DepreciationEntrySinkProcessorImpl(InternalDepreciationEntryService depreciationEntryService) {
        this.depreciationEntryService = depreciationEntryService;
    }

    public void addDepreciationEntry(DepreciationEntry entry, UUID depreciationJobCountDownContextId) {

        if (isShutdown) {
            startup(); // Start the executor if it's shutdown
        }

        buffer.add(entry);

        if (buffer.size() >= batchSizeThreshold) {

            scheduleFlush(depreciationJobCountDownContextId);

        } else if (flushTask == null) {

            scheduleFlushWithDelay(depreciationJobCountDownContextId);
        }
    }

    private void scheduleFlush(UUID depreciationJobCountDownContextId) {

        if (flushTask != null) {
            flushTask.cancel(false);
        }
        flushTask = null;
        flushBufferWithCount(depreciationJobCountDownContextId);
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
                scheduleFlush(null);
            } else if (flushTask == null) {
                scheduleFlushWithDelay(null);
            }
        }
    }

    @Override
    public void flushRemainingItems(UUID depreciationJobCountDownContextId) {

        log.info("We have reached the last batch and are attempting to flush the buffer; standby...");

        this.flushBufferWithCount(depreciationJobCountDownContextId);

        this.shutdown();
    }

    private void scheduleFlushWithDelay(UUID depreciationJobCountDownContextId) {
        if (flushTask != null) {
            flushTask.cancel(false);
        }
        flushTask = executor.schedule(() -> flushBufferWithCount(depreciationJobCountDownContextId), flushIntervalMillis, TimeUnit.MILLISECONDS);
    }

    private void flushBufferWithCount(UUID depreciationJobCountDownContextId) {

        log.info("Depreciation buffer flushing {} items to the sink", buffer.size());

        if (!buffer.isEmpty()) {

            int numberOfPersistedEntries = this.flushBuffer();

            int pendingItems = contextManager.getNumberOfProcessedItems(depreciationJobCountDownContextId);

            contextManager.updateNumberOfProcessedItems(depreciationJobCountDownContextId, -numberOfPersistedEntries);

            log.info("{} entries successfully saved to the data-sink. {} pending for persistence", numberOfPersistedEntries, pendingItems);
        }
    }

    private int flushBuffer() {

        int flushedItems = buffer.size();

        log.info("Depreciation buffer flushing {} items to the sink", flushedItems);

        this.saveEntries();

        return flushedItems;
    }

    private void saveEntries() {

        if (!buffer.isEmpty()) {

            List<DepreciationEntry> entriesToPersist = new ArrayList<>(buffer);

            log.info("Clearing buffer...");

            buffer.clear();

            log.info("Saving {} entries to the database", entriesToPersist.size());

            executor.execute(() -> depreciationEntryService.saveAll(entriesToPersist));
        }
    }

    @PostConstruct
    private void scheduleFlushStuckTask() {
        flushStuckTask = executor.scheduleAtFixedRate(this::checkFlushStuck, maxFlushDelayMillis, maxFlushDelayMillis, TimeUnit.MILLISECONDS);
    }

    // Additional plumbing
    private void checkFlushStuck() {
        // Check if the buffer hasn't been flushed for a long time
        long lastFlushTime = System.currentTimeMillis() - flushIntervalMillis;
        if (flushTask != null && flushTask.getDelay(TimeUnit.MILLISECONDS) > maxFlushDelayMillis && flushStuckTask != null && flushStuckTask.getDelay(TimeUnit.MILLISECONDS) > maxFlushDelayMillis) {
            log.warn("Forcing flush of the buffer due to long delay in flushing");
            // scheduleFlush(null);
            flushBuffer();
        }
    }
}
