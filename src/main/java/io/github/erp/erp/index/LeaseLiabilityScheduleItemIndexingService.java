package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.LeaseLiabilityScheduleItemSearchRepository;
import io.github.erp.service.LeaseLiabilityScheduleItemService;
import io.github.erp.service.mapper.LeaseLiabilityScheduleItemMapper;
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
public class LeaseLiabilityScheduleItemIndexingService extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private static final String TAG = "LeaseLiabilityScheduleItemIndex";
    private static final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleItemIndexingService.class);

    private final LeaseLiabilityScheduleItemService service;
    private final LeaseLiabilityScheduleItemMapper mapper;
    private final LeaseLiabilityScheduleItemSearchRepository searchRepository;

    public LeaseLiabilityScheduleItemIndexingService(LeaseLiabilityScheduleItemService service, LeaseLiabilityScheduleItemMapper mapper, LeaseLiabilityScheduleItemSearchRepository searchRepository) {
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
