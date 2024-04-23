
/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
package io.github.erp.erp.index.engine_v2;

import io.github.erp.erp.index.engine_v1.AbstractStartupRegisteredIndexService;
import io.github.erp.internal.IndexProperties;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runs index sequence applying batch process execution method
 */
public abstract class AbstractStartUpBatchedIndexService<T> extends AbstractStartupRegisteredIndexService {

    private static final Logger log = LoggerFactory.getLogger(AbstractStartUpBatchedIndexService.class);

    public AbstractStartUpBatchedIndexService(IndexProperties indexProperties, IndexProperties.Rebuild rebuildIndex) {
        super(indexProperties, rebuildIndex);
    }

    protected abstract List<T> getItemsForIndexing();

    protected abstract void processBatchIndex(List<T> batch);

    public int processInBatchesOf(int preferredBatchSize) {
        List<T> allItems = getItemsForIndexing();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger processedItems = new AtomicInteger(0);

        int numberOfBatches = allItems.size() / preferredBatchSize + (allItems.size() % preferredBatchSize == 0 ? 0 : 1);

        Disposable disposableBatchProcess = Observable.fromIterable(allItems)
            .buffer(preferredBatchSize)
            .subscribe(batch -> {
                count.incrementAndGet();
                processedItems.addAndGet(batch.size());
                boolean isLastBatch = processedItems.get() >= allItems.size();

                try {
                    processBatchIndex(batch);
                } catch (Exception e) {
                    log.error("Exception encountered during batch processing: {}", e.getMessage(), e);
                    throw new RuntimeException("Error while processing batch # " + count.get(), e);
                }

                log.info("{} out of {} batches processed", count.get(), numberOfBatches);

                if (isLastBatch) {
                    log.info("Final batch of {} items has been processed", processedItems.get());
                }
            });

        if (count.get() >= numberOfBatches) {
            log.info("Disposing the batch sequence...");
            disposableBatchProcess.dispose();
        }

        return count.get();
    }

}
