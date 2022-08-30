package io.github.erp.erp.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

import io.github.erp.repository.search.DepreciationMethodSearchRepository;
import io.github.erp.service.DepreciationMethodService;
import io.github.erp.service.mapper.DepreciationMethodMapper;

@Service
@Transactional
public class DepreciationMethodsIndexingService extends AbtractStartUpIndexService {

    private static final String TAG = "PrepaymentAccountIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final DepreciationMethodMapper mapper;
    private final DepreciationMethodService service;
    private final DepreciationMethodSearchRepository searchRepository;

    public DepreciationMethodsIndexingService (
        DepreciationMethodMapper mapper,
        DepreciationMethodService service,
        DepreciationMethodSearchRepository searchRepository) {
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
