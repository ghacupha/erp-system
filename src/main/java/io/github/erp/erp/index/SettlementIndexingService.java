package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.SettlementSearchRepository;
import io.github.erp.service.SettlementService;
import io.github.erp.service.mapper.SettlementMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SettlementIndexingService extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private final SettlementService service;
    private final SettlementMapper mapper;
    private final SettlementSearchRepository searchRepository;

    public SettlementIndexingService(SettlementService service, SettlementMapper mapper, SettlementSearchRepository searchRepository) {
        this.service = service;
        this.mapper = mapper;
        this.searchRepository = searchRepository;
    }

    @Async
    @Override
    public void index() {
        this.searchRepository.saveAll(
            service.findAll(Pageable.unpaged())
                .stream()
                .map(mapper::toEntity)
                .filter(entity -> !searchRepository.existsById(entity.getId()))
                .collect(ImmutableList.toImmutableList()));
    }
}
