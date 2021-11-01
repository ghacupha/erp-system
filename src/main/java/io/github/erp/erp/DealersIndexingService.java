package io.github.erp.erp;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.DealerSearchRepository;
import io.github.erp.service.DealerService;
import io.github.erp.service.mapper.DealerMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DealersIndexingService  extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private final DealerService dealerService;
    private final DealerMapper dealerMapper;
    private final DealerSearchRepository searchRepository;

    public DealersIndexingService(DealerService dealerService, DealerMapper dealerMapper, DealerSearchRepository searchRepository) {
        this.dealerService = dealerService;
        this.dealerMapper = dealerMapper;
        this.searchRepository = searchRepository;
    }

    @Async
    @Override
    public void index() {
        this.searchRepository.saveAll(
            dealerService.findAll(Pageable.unpaged())
                .stream()
                .map(dealerMapper::toEntity)
                .filter(entity -> !searchRepository.existsById(entity.getId()))
                .collect(ImmutableList.toImmutableList()));
    }
}
