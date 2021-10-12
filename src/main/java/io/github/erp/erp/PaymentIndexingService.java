package io.github.erp.erp;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.service.PaymentService;
import io.github.erp.service.mapper.PaymentMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentIndexingService   extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private final PaymentService service;
    private final PaymentMapper mapper;
    private final PaymentSearchRepository searchRepository;

    public PaymentIndexingService(PaymentService service, PaymentMapper mapper, PaymentSearchRepository searchRepository) {
        this.service = service;
        this.mapper = mapper;
        this.searchRepository = searchRepository;
    }

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
