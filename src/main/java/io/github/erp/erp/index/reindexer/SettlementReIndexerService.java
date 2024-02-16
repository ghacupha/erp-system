package io.github.erp.erp.index.reindexer;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.repository.search.SettlementSearchRepository;
import io.github.erp.service.SettlementService;
import io.github.erp.service.mapper.SettlementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//@IndexingService
@Service
@Transactional
public class SettlementReIndexerService extends AbstractReIndexerService {

    private static final Lock reindexLock = new ReentrantLock();
    private static final String TAG = "SettlementReIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);
    private final SettlementService service;
    private final SettlementMapper mapper;
    private final SettlementSearchRepository searchRepository;

    public SettlementReIndexerService(SettlementService service, SettlementMapper mapper, SettlementSearchRepository searchRepository) {
        this.service = service;
        this.mapper = mapper;
        this.searchRepository = searchRepository;
    }

    @Async
    @Override
    public void index() {
        try {
            log.info("Initiating {} index deduplication sequence", TAG);
            long startup = System.currentTimeMillis();
            this.searchRepository.saveAll(
                service.findAll(Pageable.unpaged())
                    .stream()
                    .map(mapper::toEntity)
                    .filter(entity -> !searchRepository.existsById(entity.getId()))
                    .collect(ImmutableList.toImmutableList()));
            log.trace("{} cleanup complete. Index build has taken {} milliseconds", TAG, System.currentTimeMillis() - startup);
        } finally {
            log.trace("{} ReIndexer: ReIndexing completed successfully, unlocking the index", TAG);
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
}
