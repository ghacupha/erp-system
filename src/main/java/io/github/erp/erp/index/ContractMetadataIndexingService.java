package io.github.erp.erp.index;

import com.google.common.collect.ImmutableList;
import io.github.erp.erp.index.engine_v1.AbstractStartupRegisteredIndexService;
import io.github.erp.erp.index.engine_v1.IndexingServiceChainSingleton;
import io.github.erp.repository.search.ContractMetadataSearchRepository;
import io.github.erp.service.ContractMetadataService;
import io.github.erp.service.mapper.ContractMetadataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

@IndexingService
public class ContractMetadataIndexingService extends AbstractStartupRegisteredIndexService {

    private static final String TAG = "ContractMetadataIndexingServiceIndex";
    private static final Logger log = LoggerFactory.getLogger(ContractMetadataIndexingService.class);

    private final ContractMetadataService service;
    private final ContractMetadataMapper mapper;
    private final ContractMetadataSearchRepository searchRepository;

    public ContractMetadataIndexingService(ContractMetadataService service, ContractMetadataMapper mapper, ContractMetadataSearchRepository searchRepository) {
        this.service = service;
        this.mapper = mapper;
        this.searchRepository = searchRepository;
    }

    /**
     * This method is called to register a service which is to respond to the callback
     */
    @Override
    public void register() {

        log.info("Registering {} Service", TAG);

        IndexingServiceChainSingleton.getInstance().registerService(this);
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
        log.trace("{} initiated and ready for queries. Index build has taken {} milliseconds", TAG, System.currentTimeMillis() - startup);
    }
}
