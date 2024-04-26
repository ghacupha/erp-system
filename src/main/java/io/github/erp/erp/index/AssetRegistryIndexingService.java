package io.github.erp.erp.index;

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
import com.google.common.collect.ImmutableList;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.erp.index.engine_v1.AbstractStartupRegisteredIndexService;
import io.github.erp.erp.index.engine_v1.IndexingServiceChainSingleton;
import io.github.erp.erp.index.engine_v2.AbstractStartUpBatchedIndexService;
import io.github.erp.internal.IndexProperties;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional
public class AssetRegistryIndexingService extends AbstractStartUpBatchedIndexService<AssetRegistration> {

    private static final String TAG = "Asset Registry Index";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final AssetRegistrationMapper mapper;
    private final AssetRegistrationService service;
    private final AssetRegistrationSearchRepository searchRepository;

    public AssetRegistryIndexingService(IndexProperties indexProperties, AssetRegistrationMapper mapper, AssetRegistrationService service, AssetRegistrationSearchRepository searchRepository) {
        super(indexProperties, indexProperties.getRebuild());
        this.mapper = mapper;
        this.service = service;
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
    protected List<AssetRegistration> getItemsForIndexing() {
        return service.findAll(Pageable.unpaged())
            .stream()
            .map(mapper::toEntity)
            .filter(entity -> !searchRepository.existsById(entity.getId()))
            .collect(ImmutableList.toImmutableList());
    }

    @Override
    protected void processBatchIndex(List<AssetRegistration> batch) {

        this.searchRepository.saveAll(batch);
    }
}
