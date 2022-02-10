package io.github.erp.erp;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.SettlementCurrencySearchRepository;
import io.github.erp.service.SettlementCurrencyService;
import io.github.erp.service.mapper.SettlementCurrencyMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SettlementCurrencyIndexingService extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {


    private final SettlementCurrencyService service;
    private final SettlementCurrencyMapper mapper;
    private final SettlementCurrencySearchRepository searchRepository;

    public SettlementCurrencyIndexingService(SettlementCurrencyService service, SettlementCurrencyMapper mapper, SettlementCurrencySearchRepository searchRepository) {
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
