package io.github.erp.internal.service;

import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.DealerRepository;
import io.github.erp.repository.search.DealerSearchRepository;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.mapper.DealerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class DealerBatchService implements BatchService<DealerDTO> {

    private final DealerMapper mapper;
    private final DealerRepository repository;
    private final DealerSearchRepository searchRepository;

    public DealerBatchService(DealerMapper mapper, DealerRepository repository, DealerSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<DealerDTO> save(List<DealerDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<DealerDTO> entities) {
        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
