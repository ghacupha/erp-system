package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.DepreciationMethod;
import io.github.erp.repository.DepreciationMethodRepository;
import io.github.erp.repository.search.DepreciationMethodSearchRepository;
import io.github.erp.service.DepreciationMethodService;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.mapper.DepreciationMethodMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DepreciationMethod}.
 */
@Service
@Transactional
public class DepreciationMethodServiceImpl implements DepreciationMethodService {

    private final Logger log = LoggerFactory.getLogger(DepreciationMethodServiceImpl.class);

    private final DepreciationMethodRepository depreciationMethodRepository;

    private final DepreciationMethodMapper depreciationMethodMapper;

    private final DepreciationMethodSearchRepository depreciationMethodSearchRepository;

    public DepreciationMethodServiceImpl(
        DepreciationMethodRepository depreciationMethodRepository,
        DepreciationMethodMapper depreciationMethodMapper,
        DepreciationMethodSearchRepository depreciationMethodSearchRepository
    ) {
        this.depreciationMethodRepository = depreciationMethodRepository;
        this.depreciationMethodMapper = depreciationMethodMapper;
        this.depreciationMethodSearchRepository = depreciationMethodSearchRepository;
    }

    @Override
    public DepreciationMethodDTO save(DepreciationMethodDTO depreciationMethodDTO) {
        log.debug("Request to save DepreciationMethod : {}", depreciationMethodDTO);
        DepreciationMethod depreciationMethod = depreciationMethodMapper.toEntity(depreciationMethodDTO);
        depreciationMethod = depreciationMethodRepository.save(depreciationMethod);
        DepreciationMethodDTO result = depreciationMethodMapper.toDto(depreciationMethod);
        depreciationMethodSearchRepository.save(depreciationMethod);
        return result;
    }

    @Override
    public Optional<DepreciationMethodDTO> partialUpdate(DepreciationMethodDTO depreciationMethodDTO) {
        log.debug("Request to partially update DepreciationMethod : {}", depreciationMethodDTO);

        return depreciationMethodRepository
            .findById(depreciationMethodDTO.getId())
            .map(existingDepreciationMethod -> {
                depreciationMethodMapper.partialUpdate(existingDepreciationMethod, depreciationMethodDTO);

                return existingDepreciationMethod;
            })
            .map(depreciationMethodRepository::save)
            .map(savedDepreciationMethod -> {
                depreciationMethodSearchRepository.save(savedDepreciationMethod);

                return savedDepreciationMethod;
            })
            .map(depreciationMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationMethods");
        return depreciationMethodRepository.findAll(pageable).map(depreciationMethodMapper::toDto);
    }

    public Page<DepreciationMethodDTO> findAllWithEagerRelationships(Pageable pageable) {
        return depreciationMethodRepository.findAllWithEagerRelationships(pageable).map(depreciationMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationMethodDTO> findOne(Long id) {
        log.debug("Request to get DepreciationMethod : {}", id);
        return depreciationMethodRepository.findOneWithEagerRelationships(id).map(depreciationMethodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationMethod : {}", id);
        depreciationMethodRepository.deleteById(id);
        depreciationMethodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationMethodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationMethods for query {}", query);
        return depreciationMethodSearchRepository.search(query, pageable).map(depreciationMethodMapper::toDto);
    }
}
