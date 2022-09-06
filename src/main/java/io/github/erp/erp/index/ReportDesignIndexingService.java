package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.repository.search.ReportDesignSearchRepository;
import io.github.erp.service.ReportDesignService;
import io.github.erp.service.mapper.ReportDesignMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportDesignIndexingService extends AbtractStartUpIndexService {

    private static final String TAG = "ReportDesignIndex";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    private final ReportDesignMapper mapper;
    private final ReportDesignService service;
    private final ReportDesignSearchRepository searchRepository;

    public ReportDesignIndexingService(ReportDesignMapper mapper, ReportDesignService service, ReportDesignSearchRepository searchRepository) {
        this.mapper = mapper;
        this.service = service;
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
