package io.github.erp.erp;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.PaymentLabelRepository;
import io.github.erp.repository.search.PaymentLabelSearchRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentLabelIndexingService extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {


    private final PaymentLabelRepository repository;
    private final PaymentLabelSearchRepository searchRepository;

    public PaymentLabelIndexingService(PaymentLabelRepository repository, PaymentLabelSearchRepository searchRepository) {
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Async
    @Override
    public void index() {
        this.searchRepository.saveAll(
            this.repository.findAll()
                .stream()
                .filter(entity -> !searchRepository.existsById(entity.getId()))
                .collect(ImmutableList.toImmutableList()));
    }
}
