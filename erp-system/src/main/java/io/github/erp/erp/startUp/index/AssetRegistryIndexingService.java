package io.github.erp.erp.startUp.index;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import com.google.common.collect.ImmutableList;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.erp.startUp.index.engine_v1.IndexingServiceChainSingleton;
import io.github.erp.erp.startUp.index.engine_v2.AbstractStartUpBatchedIndexService;
import io.github.erp.internal.IndexProperties;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    /**
     * We are going to remove comments for this one
     *
     * @param entity
     * @return
     */
    @Override
    protected AssetRegistration prepareForIndexing(AssetRegistration entity) {
        entity.setComments(null);
        entity.setCommentsContentType(null);

        return entity;
    }
}
