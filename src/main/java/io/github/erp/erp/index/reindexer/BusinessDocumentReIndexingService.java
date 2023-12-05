package io.github.erp.erp.index.reindexer;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.erp.index.reindexer.AbstractReIndexerService;
import io.github.erp.repository.search.BusinessDocumentSearchRepository;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.mapper.BusinessDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional
public class BusinessDocumentReIndexingService extends AbstractReIndexerService {

    private static final Lock reindexLock = new ReentrantLock();
    private static final String TAG = "BusinessDocumentReIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);
    private final BusinessDocumentService service;
    private final BusinessDocumentMapper mapper;
    private final BusinessDocumentSearchRepository searchRepository;

    public BusinessDocumentReIndexingService(BusinessDocumentService service, BusinessDocumentMapper mapper, BusinessDocumentSearchRepository searchRepository) {
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
            log.trace("{} ReIndexer: ReIndexing has been completed successfully; unlocking the index", TAG);
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
