package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.SecurityClearanceSearchRepository;
import io.github.erp.service.SecurityClearanceService;
import io.github.erp.service.mapper.SecurityClearanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecurityClearanceIndexingService  extends AbtractStartUpIndexService {


    private static final String TAG = "SecurityClearanceIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final SecurityClearanceService service;
    private final SecurityClearanceMapper mapper;
    private final SecurityClearanceSearchRepository searchRepository;

    public SecurityClearanceIndexingService(SecurityClearanceService service, SecurityClearanceMapper mapper, SecurityClearanceSearchRepository searchRepository) {
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
