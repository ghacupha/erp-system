package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.erp.index.engine_v1.AbstractStartupRegisteredIndexService;
import io.github.erp.erp.index.engine_v1.IndexingServiceChainSingleton;
import io.github.erp.internal.IndexProperties;
import io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepository;
import io.github.erp.repository.search.LeaseLiabilitySearchRepository;
import io.github.erp.service.LeaseLiabilityScheduleItemService;
import io.github.erp.service.LeaseLiabilityService;
import io.github.erp.service.mapper.LeaseLiabilityMapper;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
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
public class LeaseLiabilityIndexingService extends AbstractStartupRegisteredIndexService {


    private static final String TAG = "LeaseLiabilityScheduleItemIndex";
    private static final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleItemIndexingService.class);

    private final LeaseLiabilityService service;
    private final LeaseLiabilityMapper mapper;
    private final LeaseLiabilitySearchRepository searchRepository;

    public LeaseLiabilityIndexingService (IndexProperties indexProperties, LeaseLiabilityService service, LeaseLiabilityMapper mapper, LeaseLiabilitySearchRepository searchRepository) {
        super(indexProperties, indexProperties.getRebuild());
        this.service = service;
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

            indexerSequence();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reindexLock.unlock();
        }
    }

    private void indexerSequence() {
        log.info("Initiating {} build sequence", TAG);
        long startup = System.currentTimeMillis();
        this.searchRepository.saveAll(
            service.findAll(Pageable.unpaged())
                .stream()
                .map(mapper::toEntity)
                .filter(entity -> !searchRepository.existsById(entity.getId()))
                .collect(ImmutableList.toImmutableList()));
        log.trace("{} initiated and ready for queries. Index build has taken {} milliseconds", TAG, System.currentTimeMillis() - startup);
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
