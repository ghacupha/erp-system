package io.github.erp.erp;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.DealerRepository;
import io.github.erp.repository.SignedPaymentRepository;
import io.github.erp.repository.search.DealerSearchRepository;
import io.github.erp.repository.search.SignedPaymentSearchRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO Fix lazy init
//@Service
//@Transactional
public class DealersIndexingService  extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private final DealerRepository repository;
    private final DealerSearchRepository searchRepository;

    public DealersIndexingService(DealerRepository repository, DealerSearchRepository searchRepository) {
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
