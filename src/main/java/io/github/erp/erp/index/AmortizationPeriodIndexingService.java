package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.domain.AmortizationPeriod;
import io.github.erp.erp.index.engine_v1.IndexingServiceChainSingleton;
import io.github.erp.erp.index.engine_v2.AbstractStartUpBatchedIndexService;
import io.github.erp.internal.IndexProperties;
import io.github.erp.repository.search.AmortizationPeriodSearchRepository;
import io.github.erp.service.AmortizationPeriodService;
import io.github.erp.service.mapper.AmortizationPeriodMapper;
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
public class AmortizationPeriodIndexingService extends AbstractStartUpBatchedIndexService<AmortizationPeriod> {


    private static final String TAG = "AmortizationPeriodIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final AmortizationPeriodMapper mapper;
    private final AmortizationPeriodService service;
    private final AmortizationPeriodSearchRepository searchRepository;

    public AmortizationPeriodIndexingService(
        IndexProperties indexProperties,
        AmortizationPeriodMapper mapper,
        AmortizationPeriodService service,
        AmortizationPeriodSearchRepository searchRepository) {
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

    private int indexerSequence() {
        log.info("Initiating {} build sequence", TAG);
        long startup = System.currentTimeMillis();
        log.trace("{} initiated and ready for queries. Index build has taken {} milliseconds", TAG, System.currentTimeMillis() - startup);

        return processInBatchesOf(300);
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
    protected List<AmortizationPeriod> getItemsForIndexing() {
        return service.findAll(Pageable.unpaged())
            .stream()
            .map(mapper::toEntity)
            .filter(entity -> !searchRepository.existsById(entity.getId()))
            .collect(ImmutableList.toImmutableList());
    }

    @Override
    protected void processBatchIndex(List<AmortizationPeriod> batch) {

        this.searchRepository.saveAll(batch);
    }
}
