package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.DeliveryNoteSearchRepository;
import io.github.erp.service.DeliveryNoteService;
import io.github.erp.service.mapper.DeliveryNoteMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryNotesIndexingService extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private final DeliveryNoteMapper mapper;
    private final DeliveryNoteService service;
    private final DeliveryNoteSearchRepository searchRepository;

    public DeliveryNotesIndexingService(DeliveryNoteMapper mapper, DeliveryNoteService service, DeliveryNoteSearchRepository searchRepository) {
        this.mapper = mapper;
        this.service = service;
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
