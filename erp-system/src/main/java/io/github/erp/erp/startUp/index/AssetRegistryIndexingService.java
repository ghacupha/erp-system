
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
package io.github.erp.erp.startUp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.domain.AssetRegistrationIndex;
import io.github.erp.erp.assets.registrationindex.AssetRegistrationIndexMapper;
import io.github.erp.erp.startUp.index.engine_v1.IndexingServiceChainSingleton;
import io.github.erp.erp.startUp.index.engine_v2.AbstractStartUpBatchedIndexService;
import io.github.erp.internal.IndexProperties;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.repository.search.AssetRegistrationIndexSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Startup / full-reindex population of the flattened {@code AssetRegistrationIndex} document
 * (see its own doc comment for why it exists) - this is the bulk-load counterpart to
 * {@code AssetRegistrationIndexConsumer}, which handles single/few-row updates as they happen.
 * Both ultimately go through the same {@link AssetRegistrationIndexMapper} and land in the same
 * {@link AssetRegistrationIndexSearchRepository}, so a full reindex and an incremental update
 * produce identical documents.
 */
@Service
@Transactional
public class AssetRegistryIndexingService extends AbstractStartUpBatchedIndexService<AssetRegistrationIndex> {

    private static final String TAG = "Asset Registry Index";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final InternalAssetRegistrationRepository assetRegistrationRepository;
    private final AssetRegistrationIndexMapper mapper;
    private final AssetRegistrationIndexSearchRepository searchRepository;

    public AssetRegistryIndexingService(
        IndexProperties indexProperties,
        InternalAssetRegistrationRepository assetRegistrationRepository,
        AssetRegistrationIndexMapper mapper,
        AssetRegistrationIndexSearchRepository searchRepository
    ) {
        super(indexProperties, indexProperties.getRebuild());
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.mapper = mapper;
        this.searchRepository = searchRepository;
    }

    /**
     * This method is called to register a service which is to respond to the callback
     */
    @Override
    public void register() {

        log.info("Registering {} Service", TAG);

        IndexingServiceChainSingleton.getInstance().registerService(this);
    }

    private static final Lock reindexLock = new ReentrantLock();

    @Async
    public void index() {
        try {
            reindexLock.lockInterruptibly();

            int batches = indexerSequence();

            log.info("{} batches processed", batches);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reindexLock.unlock();
        }
    }

    @Override
    public void tearDown() {

        if (reindexLock.tryLock()) {
            this.searchRepository.deleteAll();
        } else {
            log.trace("{} ReIndexer: Concurrent reindexing attempt", TAG);
        }
    }

    private int indexerSequence() {
        log.info("Initiating {} build sequence", TAG);
        long startup = System.currentTimeMillis();

        log.trace("{} initiated and ready for queries. Index build has taken {} milliseconds", TAG, System.currentTimeMillis() - startup);

        return this.processInBatchesOf(100);

    }

    @Override
    protected List<AssetRegistrationIndex> getItemsForIndexing() {
        return assetRegistrationRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(mapper::toIndex)
            .filter(index -> !searchRepository.existsById(index.getId()))
            .collect(ImmutableList.toImmutableList());
    }

    @Override
    protected void processBatchIndex(List<AssetRegistrationIndex> batch) {

        this.searchRepository.saveAll(batch);
    }
}
