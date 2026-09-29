
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
import io.github.erp.domain.Settlement;
import io.github.erp.erp.startUp.index.engine_v1.IndexingServiceChainSingleton;
import io.github.erp.erp.startUp.index.engine_v2.AbstractStartUpBatchedIndexService;
import io.github.erp.erp.startUp.index.kafka.ReindexMessage;
import io.github.erp.erp.startUp.index.kafka.ReindexProducer;
import io.github.erp.internal.IndexProperties;
import io.github.erp.internal.service.payments.InternalSettlementService;
import io.github.erp.repository.search.SettlementSearchRepository;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.mapper.SettlementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional
public class SettlementIndexingService extends AbstractStartUpBatchedIndexService<Settlement> {
    private static final String TAG = "SettlementIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);
    private static final String TOPIC = "erp-reindex-settlement";
    private static final int BATCH_SIZE = 400;
    private final InternalSettlementService service;
    private final SettlementMapper mapper;
    private final SettlementSearchRepository searchRepository;
    private final ReindexProducer reindexProducer;

    public SettlementIndexingService(IndexProperties indexProperties, InternalSettlementService service, SettlementMapper mapper, SettlementSearchRepository searchRepository, ReindexProducer reindexProducer) {
        super(indexProperties, indexProperties.getRebuild());
        this.service = service;
        this.mapper = mapper;
        this.searchRepository = searchRepository;
        this.reindexProducer = reindexProducer;
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

            List<Long> ids = service
                .findAll(Pageable.unpaged())
                .stream()
                .map(SettlementDTO::getId)
                .filter(id -> !searchRepository.existsById(id))
                .collect(ImmutableList.toImmutableList());

            for (int i = 0; i < ids.size(); i += BATCH_SIZE) {
                reindexProducer.sendReindexMessage(TOPIC, ids.subList(i, Math.min(i + BATCH_SIZE, ids.size())));
            }

            log.info("Queued {} {} id(s) for reindexing on topic {}", ids.size(), TAG, TOPIC);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reindexLock.unlock();
        }
    }

    @KafkaListener(topics = TOPIC, containerFactory = "phasedReindexKafkaListenerContainerFactory")
    @Transactional(readOnly = true)
    public void consumeReindexMessage(ReindexMessage message) {
        if (message.getIds() == null || message.getIds().isEmpty()) {
            return;
        }

        if (message.isDeleted()) {
            message.getIds().forEach(searchRepository::deleteById);
            log.debug("Removed {} {} document(s)", message.getIds().size(), TAG);
            return;
        }

        List<Settlement> documents = new ArrayList<>();
        for (Long id : message.getIds()) {
            // prepareForIndexing strips the (potentially large) calculationFile blob before it
            // reaches Elasticsearch - see the override below.
            service.findOne(id).map(mapper::toEntity).map(this::prepareForIndexing).ifPresent(documents::add);
        }

        if (!documents.isEmpty()) {
            searchRepository.saveAll(documents);
            log.debug("Indexed {} {} document(s)", documents.size(), TAG);
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

    @Override
    protected List<Settlement> getItemsForIndexing() {
        return service.findAll(Pageable.unpaged())
                .stream()
                .map(mapper::toEntity)
                .filter(entity -> !searchRepository.existsById(entity.getId()))
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    protected void processBatchIndex(List<Settlement> batch) {

        this.searchRepository.saveAll(batch);
    }

    /**
     * To remove the calculationFile which we have been unable to remove from the index
     *
     * @param entity
     * @return
     */
    protected Settlement prepareForIndexing(Settlement entity) {
        Settlement prepared = super.prepareForIndexing(entity);
        prepared.setCalculationFile(null);
        prepared.setCalculationFileContentType(null);
        return prepared;
    }
}
