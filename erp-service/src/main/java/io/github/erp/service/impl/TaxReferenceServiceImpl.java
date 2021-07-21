package io.github.erp.service.impl;

import io.github.erp.service.TaxReferenceService;
import io.github.erp.domain.TaxReference;
import io.github.erp.repository.TaxReferenceRepository;
import io.github.erp.repository.search.TaxReferenceSearchRepository;
import io.github.erp.service.dto.TaxReferenceDTO;
import io.github.erp.service.mapper.TaxReferenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link TaxReference}.
 */
@Service
@Transactional
public class TaxReferenceServiceImpl implements TaxReferenceService {

    private final Logger log = LoggerFactory.getLogger(TaxReferenceServiceImpl.class);

    private final TaxReferenceRepository taxReferenceRepository;

    private final TaxReferenceMapper taxReferenceMapper;

    private final TaxReferenceSearchRepository taxReferenceSearchRepository;

    public TaxReferenceServiceImpl(TaxReferenceRepository taxReferenceRepository, TaxReferenceMapper taxReferenceMapper, TaxReferenceSearchRepository taxReferenceSearchRepository) {
        this.taxReferenceRepository = taxReferenceRepository;
        this.taxReferenceMapper = taxReferenceMapper;
        this.taxReferenceSearchRepository = taxReferenceSearchRepository;
    }

    @Override
    public TaxReferenceDTO save(TaxReferenceDTO taxReferenceDTO) {
        log.debug("Request to save TaxReference : {}", taxReferenceDTO);
        TaxReference taxReference = taxReferenceMapper.toEntity(taxReferenceDTO);
        taxReference = taxReferenceRepository.save(taxReference);
        TaxReferenceDTO result = taxReferenceMapper.toDto(taxReference);
        taxReferenceSearchRepository.save(taxReference);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxReferenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxReferences");
        return taxReferenceRepository.findAll(pageable)
            .map(taxReferenceMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TaxReferenceDTO> findOne(Long id) {
        log.debug("Request to get TaxReference : {}", id);
        return taxReferenceRepository.findById(id)
            .map(taxReferenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaxReference : {}", id);
        taxReferenceRepository.deleteById(id);
        taxReferenceSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxReferenceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaxReferences for query {}", query);
        return taxReferenceSearchRepository.search(queryStringQuery(query), pageable)
            .map(taxReferenceMapper::toDto);
    }
}
