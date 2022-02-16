package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.PaymentInvoiceSearchRepository;
import io.github.erp.service.PaymentInvoiceService;
import io.github.erp.service.mapper.PaymentInvoiceMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentInvoiceIndexingService  extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private final PaymentInvoiceService service;
    private final PaymentInvoiceMapper mapper;
    private final PaymentInvoiceSearchRepository searchRepository;

    public PaymentInvoiceIndexingService(PaymentInvoiceService service, PaymentInvoiceMapper mapper, PaymentInvoiceSearchRepository searchRepository) {
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
