package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.DepreciationJob;
import io.github.erp.repository.DepreciationJobRepository;
import io.github.erp.repository.search.DepreciationJobSearchRepository;
import io.github.erp.service.DepreciationJobService;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.mapper.DepreciationJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepreciationJob}.
 */
@Service
@Transactional
public class DepreciationJobServiceImpl implements DepreciationJobService {

    private final Logger log = LoggerFactory.getLogger(DepreciationJobServiceImpl.class);

    private final DepreciationJobRepository depreciationJobRepository;

    private final DepreciationJobMapper depreciationJobMapper;

    private final DepreciationJobSearchRepository depreciationJobSearchRepository;

    public DepreciationJobServiceImpl(
        DepreciationJobRepository depreciationJobRepository,
        DepreciationJobMapper depreciationJobMapper,
        DepreciationJobSearchRepository depreciationJobSearchRepository
    ) {
        this.depreciationJobRepository = depreciationJobRepository;
        this.depreciationJobMapper = depreciationJobMapper;
        this.depreciationJobSearchRepository = depreciationJobSearchRepository;
    }

    @Override
    public DepreciationJobDTO save(DepreciationJobDTO depreciationJobDTO) {
        log.debug("Request to save DepreciationJob : {}", depreciationJobDTO);
        DepreciationJob depreciationJob = depreciationJobMapper.toEntity(depreciationJobDTO);
        depreciationJob = depreciationJobRepository.save(depreciationJob);
        DepreciationJobDTO result = depreciationJobMapper.toDto(depreciationJob);
        depreciationJobSearchRepository.save(depreciationJob);
        return result;
    }

    @Override
    public Optional<DepreciationJobDTO> partialUpdate(DepreciationJobDTO depreciationJobDTO) {
        log.debug("Request to partially update DepreciationJob : {}", depreciationJobDTO);

        return depreciationJobRepository
            .findById(depreciationJobDTO.getId())
            .map(existingDepreciationJob -> {
                depreciationJobMapper.partialUpdate(existingDepreciationJob, depreciationJobDTO);

                return existingDepreciationJob;
            })
            .map(depreciationJobRepository::save)
            .map(savedDepreciationJob -> {
                depreciationJobSearchRepository.save(savedDepreciationJob);

                return savedDepreciationJob;
            })
            .map(depreciationJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationJobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationJobs");
        return depreciationJobRepository.findAll(pageable).map(depreciationJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationJobDTO> findOne(Long id) {
        log.debug("Request to get DepreciationJob : {}", id);
        return depreciationJobRepository.findById(id).map(depreciationJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationJob : {}", id);
        depreciationJobRepository.deleteById(id);
        depreciationJobSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationJobDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationJobs for query {}", query);
        return depreciationJobSearchRepository.search(query, pageable).map(depreciationJobMapper::toDto);
    }
}
