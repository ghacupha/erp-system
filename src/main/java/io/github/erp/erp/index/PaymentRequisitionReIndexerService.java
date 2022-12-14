package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.PaymentRequisitionSearchRepository;
import io.github.erp.service.PaymentRequisitionService;
import io.github.erp.service.mapper.PaymentRequisitionMapper;
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
public class PaymentRequisitionReIndexerService extends AbstractReIndexerService {

    private static final Lock reindexLock = new ReentrantLock();
    private static final String TAG = "DealerIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);
    private final PaymentRequisitionService service;
    private final PaymentRequisitionMapper mapper;
    private final PaymentRequisitionSearchRepository searchRepository;

    public PaymentRequisitionReIndexerService(PaymentRequisitionService service, PaymentRequisitionMapper mapper, PaymentRequisitionSearchRepository searchRepository) {
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
            log.info("{} cleanup complete. Index build has taken {} milliseconds", TAG, System.currentTimeMillis() - startup);
        } finally {
            log.info("{} ReIndexer: ReIndexing has been completed successfully; unlocking the index", TAG);
            reindexLock.unlock();
        }
    }

    @Override
    public void tearDown() {

        if (reindexLock.tryLock()) {
            this.searchRepository.deleteAll();
        } else {
            log.info("{} ReIndexer: Concurrent reindexing attempt", TAG);
        }
    }
}
