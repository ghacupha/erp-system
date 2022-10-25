package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 2 (Caleb Series) Server ver 0.1.2-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.TaxReference;
import io.github.erp.repository.TaxReferenceRepository;
import io.github.erp.repository.search.TaxReferenceSearchRepository;
import io.github.erp.service.TaxReferenceService;
import io.github.erp.service.dto.TaxReferenceDTO;
import io.github.erp.service.mapper.TaxReferenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public TaxReferenceServiceImpl(
        TaxReferenceRepository taxReferenceRepository,
        TaxReferenceMapper taxReferenceMapper,
        TaxReferenceSearchRepository taxReferenceSearchRepository
    ) {
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
    public Optional<TaxReferenceDTO> partialUpdate(TaxReferenceDTO taxReferenceDTO) {
        log.debug("Request to partially update TaxReference : {}", taxReferenceDTO);

        return taxReferenceRepository
            .findById(taxReferenceDTO.getId())
            .map(existingTaxReference -> {
                taxReferenceMapper.partialUpdate(existingTaxReference, taxReferenceDTO);

                return existingTaxReference;
            })
            .map(taxReferenceRepository::save)
            .map(savedTaxReference -> {
                taxReferenceSearchRepository.save(savedTaxReference);

                return savedTaxReference;
            })
            .map(taxReferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaxReferenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxReferences");
        return taxReferenceRepository.findAll(pageable).map(taxReferenceMapper::toDto);
    }

    public Page<TaxReferenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return taxReferenceRepository.findAllWithEagerRelationships(pageable).map(taxReferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaxReferenceDTO> findOne(Long id) {
        log.debug("Request to get TaxReference : {}", id);
        return taxReferenceRepository.findOneWithEagerRelationships(id).map(taxReferenceMapper::toDto);
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
        return taxReferenceSearchRepository.search(query, pageable).map(taxReferenceMapper::toDto);
    }
}
