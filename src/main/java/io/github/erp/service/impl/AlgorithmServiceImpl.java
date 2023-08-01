package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.Algorithm;
import io.github.erp.repository.AlgorithmRepository;
import io.github.erp.repository.search.AlgorithmSearchRepository;
import io.github.erp.service.AlgorithmService;
import io.github.erp.service.dto.AlgorithmDTO;
import io.github.erp.service.mapper.AlgorithmMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Algorithm}.
 */
@Service
@Transactional
public class AlgorithmServiceImpl implements AlgorithmService {

    private final Logger log = LoggerFactory.getLogger(AlgorithmServiceImpl.class);

    private final AlgorithmRepository algorithmRepository;

    private final AlgorithmMapper algorithmMapper;

    private final AlgorithmSearchRepository algorithmSearchRepository;

    public AlgorithmServiceImpl(
        AlgorithmRepository algorithmRepository,
        AlgorithmMapper algorithmMapper,
        AlgorithmSearchRepository algorithmSearchRepository
    ) {
        this.algorithmRepository = algorithmRepository;
        this.algorithmMapper = algorithmMapper;
        this.algorithmSearchRepository = algorithmSearchRepository;
    }

    @Override
    public AlgorithmDTO save(AlgorithmDTO algorithmDTO) {
        log.debug("Request to save Algorithm : {}", algorithmDTO);
        Algorithm algorithm = algorithmMapper.toEntity(algorithmDTO);
        algorithm = algorithmRepository.save(algorithm);
        AlgorithmDTO result = algorithmMapper.toDto(algorithm);
        algorithmSearchRepository.save(algorithm);
        return result;
    }

    @Override
    public Optional<AlgorithmDTO> partialUpdate(AlgorithmDTO algorithmDTO) {
        log.debug("Request to partially update Algorithm : {}", algorithmDTO);

        return algorithmRepository
            .findById(algorithmDTO.getId())
            .map(existingAlgorithm -> {
                algorithmMapper.partialUpdate(existingAlgorithm, algorithmDTO);

                return existingAlgorithm;
            })
            .map(algorithmRepository::save)
            .map(savedAlgorithm -> {
                algorithmSearchRepository.save(savedAlgorithm);

                return savedAlgorithm;
            })
            .map(algorithmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlgorithmDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Algorithms");
        return algorithmRepository.findAll(pageable).map(algorithmMapper::toDto);
    }

    public Page<AlgorithmDTO> findAllWithEagerRelationships(Pageable pageable) {
        return algorithmRepository.findAllWithEagerRelationships(pageable).map(algorithmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlgorithmDTO> findOne(Long id) {
        log.debug("Request to get Algorithm : {}", id);
        return algorithmRepository.findOneWithEagerRelationships(id).map(algorithmMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Algorithm : {}", id);
        algorithmRepository.deleteById(id);
        algorithmSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlgorithmDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Algorithms for query {}", query);
        return algorithmSearchRepository.search(query, pageable).map(algorithmMapper::toDto);
    }
}
