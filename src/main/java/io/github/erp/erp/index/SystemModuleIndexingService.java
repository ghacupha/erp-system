package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.SystemModuleSearchRepository;
import io.github.erp.service.SystemModuleService;
import io.github.erp.service.mapper.SystemModuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemModuleIndexingService extends AbtractStartUpIndexService {

    private static final String TAG = "SystemModuleIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final SystemModuleMapper mapper;
    private final SystemModuleService service;
    private final SystemModuleSearchRepository searchRepository;

    public SystemModuleIndexingService(SystemModuleMapper mapper, SystemModuleService service, SystemModuleSearchRepository searchRepository) {
        this.mapper = mapper;
        this.service = service;
        this.searchRepository = searchRepository;
    }

    @Async
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
