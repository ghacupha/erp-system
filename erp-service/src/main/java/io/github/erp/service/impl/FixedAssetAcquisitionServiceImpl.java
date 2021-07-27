package io.github.erp.service.impl;

import io.github.erp.service.FixedAssetAcquisitionService;
import io.github.erp.domain.FixedAssetAcquisition;
import io.github.erp.repository.FixedAssetAcquisitionRepository;
import io.github.erp.repository.search.FixedAssetAcquisitionSearchRepository;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
import io.github.erp.service.mapper.FixedAssetAcquisitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link FixedAssetAcquisition}.
 */
@Service
@Transactional
public class FixedAssetAcquisitionServiceImpl implements FixedAssetAcquisitionService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetAcquisitionServiceImpl.class);

    private final FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository;

    private final FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper;

    private final FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository;

    public FixedAssetAcquisitionServiceImpl(FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository, FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper, FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository) {
        this.fixedAssetAcquisitionRepository = fixedAssetAcquisitionRepository;
        this.fixedAssetAcquisitionMapper = fixedAssetAcquisitionMapper;
        this.fixedAssetAcquisitionSearchRepository = fixedAssetAcquisitionSearchRepository;
    }

    @Override
    public FixedAssetAcquisitionDTO save(FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO) {
        log.debug("Request to save FixedAssetAcquisition : {}", fixedAssetAcquisitionDTO);
        FixedAssetAcquisition fixedAssetAcquisition = fixedAssetAcquisitionMapper.toEntity(fixedAssetAcquisitionDTO);
        fixedAssetAcquisition = fixedAssetAcquisitionRepository.save(fixedAssetAcquisition);
        FixedAssetAcquisitionDTO result = fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisition);
        fixedAssetAcquisitionSearchRepository.save(fixedAssetAcquisition);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetAcquisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetAcquisitions");
        return fixedAssetAcquisitionRepository.findAll(pageable)
            .map(fixedAssetAcquisitionMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetAcquisitionDTO> findOne(Long id) {
        log.debug("Request to get FixedAssetAcquisition : {}", id);
        return fixedAssetAcquisitionRepository.findById(id)
            .map(fixedAssetAcquisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FixedAssetAcquisition : {}", id);
        fixedAssetAcquisitionRepository.deleteById(id);
        fixedAssetAcquisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetAcquisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FixedAssetAcquisitions for query {}", query);
        return fixedAssetAcquisitionSearchRepository.search(queryStringQuery(query), pageable)
            .map(fixedAssetAcquisitionMapper::toDto);
    }
}
