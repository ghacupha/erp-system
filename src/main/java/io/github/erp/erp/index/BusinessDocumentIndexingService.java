package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.BusinessDocumentSearchRepository;
import io.github.erp.repository.search.DealerSearchRepository;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.DealerService;
import io.github.erp.service.mapper.BusinessDocumentMapper;
import io.github.erp.service.mapper.DealerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessDocumentIndexingService  extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {
    private static final String TAG = "BusinessDocumentIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final BusinessDocumentService service;
    private final BusinessDocumentMapper mapper;
    private final BusinessDocumentSearchRepository searchRepository;

    public BusinessDocumentIndexingService(BusinessDocumentService service, BusinessDocumentMapper mapper, BusinessDocumentSearchRepository searchRepository) {
        this.service = service;
        this.mapper = mapper;
        this.searchRepository = searchRepository;
    }

    @Async
    @Override
    public void index() {
        log.info("Initiating {} build sequence", TAG);
        long startup = System.currentTimeMillis();
        this.searchRepository.saveAll(
            service.findAll(Pageable.unpaged())
                .stream()
                .map(mapper::toEntity)
                .filter(entity -> !searchRepository.existsById(entity.getId()))
                .collect(ImmutableList.toImmutableList()));
        log.info("{} initiated and ready for queries. Index build has taken {} milliseconds", TAG, System.currentTimeMillis() - startup);
    }
}
