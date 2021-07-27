package io.github.erp.service.impl;

import io.github.erp.service.FixedAssetNetBookValueService;
import io.github.erp.domain.FixedAssetNetBookValue;
import io.github.erp.repository.FixedAssetNetBookValueRepository;
import io.github.erp.repository.search.FixedAssetNetBookValueSearchRepository;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.service.mapper.FixedAssetNetBookValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link FixedAssetNetBookValue}.
 */
@Service
@Transactional
public class FixedAssetNetBookValueServiceImpl implements FixedAssetNetBookValueService {

    private final Logger log = LoggerFactory.getLogger(FixedAssetNetBookValueServiceImpl.class);

    private final FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository;

    private final FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper;

    private final FixedAssetNetBookValueSearchRepository fixedAssetNetBookValueSearchRepository;

    public FixedAssetNetBookValueServiceImpl(FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository, FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper, FixedAssetNetBookValueSearchRepository fixedAssetNetBookValueSearchRepository) {
        this.fixedAssetNetBookValueRepository = fixedAssetNetBookValueRepository;
        this.fixedAssetNetBookValueMapper = fixedAssetNetBookValueMapper;
        this.fixedAssetNetBookValueSearchRepository = fixedAssetNetBookValueSearchRepository;
    }

    @Override
    public FixedAssetNetBookValueDTO save(FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO) {
        log.debug("Request to save FixedAssetNetBookValue : {}", fixedAssetNetBookValueDTO);
        FixedAssetNetBookValue fixedAssetNetBookValue = fixedAssetNetBookValueMapper.toEntity(fixedAssetNetBookValueDTO);
        fixedAssetNetBookValue = fixedAssetNetBookValueRepository.save(fixedAssetNetBookValue);
        FixedAssetNetBookValueDTO result = fixedAssetNetBookValueMapper.toDto(fixedAssetNetBookValue);
        fixedAssetNetBookValueSearchRepository.save(fixedAssetNetBookValue);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetNetBookValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FixedAssetNetBookValues");
        return fixedAssetNetBookValueRepository.findAll(pageable)
            .map(fixedAssetNetBookValueMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FixedAssetNetBookValueDTO> findOne(Long id) {
        log.debug("Request to get FixedAssetNetBookValue : {}", id);
        return fixedAssetNetBookValueRepository.findById(id)
            .map(fixedAssetNetBookValueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FixedAssetNetBookValue : {}", id);
        fixedAssetNetBookValueRepository.deleteById(id);
        fixedAssetNetBookValueSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FixedAssetNetBookValueDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FixedAssetNetBookValues for query {}", query);
        return fixedAssetNetBookValueSearchRepository.search(queryStringQuery(query), pageable)
            .map(fixedAssetNetBookValueMapper::toDto);
    }
}
