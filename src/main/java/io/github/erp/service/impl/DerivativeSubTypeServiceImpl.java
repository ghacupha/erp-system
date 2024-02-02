package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.DerivativeSubType;
import io.github.erp.repository.DerivativeSubTypeRepository;
import io.github.erp.repository.search.DerivativeSubTypeSearchRepository;
import io.github.erp.service.DerivativeSubTypeService;
import io.github.erp.service.dto.DerivativeSubTypeDTO;
import io.github.erp.service.mapper.DerivativeSubTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DerivativeSubType}.
 */
@Service
@Transactional
public class DerivativeSubTypeServiceImpl implements DerivativeSubTypeService {

    private final Logger log = LoggerFactory.getLogger(DerivativeSubTypeServiceImpl.class);

    private final DerivativeSubTypeRepository derivativeSubTypeRepository;

    private final DerivativeSubTypeMapper derivativeSubTypeMapper;

    private final DerivativeSubTypeSearchRepository derivativeSubTypeSearchRepository;

    public DerivativeSubTypeServiceImpl(
        DerivativeSubTypeRepository derivativeSubTypeRepository,
        DerivativeSubTypeMapper derivativeSubTypeMapper,
        DerivativeSubTypeSearchRepository derivativeSubTypeSearchRepository
    ) {
        this.derivativeSubTypeRepository = derivativeSubTypeRepository;
        this.derivativeSubTypeMapper = derivativeSubTypeMapper;
        this.derivativeSubTypeSearchRepository = derivativeSubTypeSearchRepository;
    }

    @Override
    public DerivativeSubTypeDTO save(DerivativeSubTypeDTO derivativeSubTypeDTO) {
        log.debug("Request to save DerivativeSubType : {}", derivativeSubTypeDTO);
        DerivativeSubType derivativeSubType = derivativeSubTypeMapper.toEntity(derivativeSubTypeDTO);
        derivativeSubType = derivativeSubTypeRepository.save(derivativeSubType);
        DerivativeSubTypeDTO result = derivativeSubTypeMapper.toDto(derivativeSubType);
        derivativeSubTypeSearchRepository.save(derivativeSubType);
        return result;
    }

    @Override
    public Optional<DerivativeSubTypeDTO> partialUpdate(DerivativeSubTypeDTO derivativeSubTypeDTO) {
        log.debug("Request to partially update DerivativeSubType : {}", derivativeSubTypeDTO);

        return derivativeSubTypeRepository
            .findById(derivativeSubTypeDTO.getId())
            .map(existingDerivativeSubType -> {
                derivativeSubTypeMapper.partialUpdate(existingDerivativeSubType, derivativeSubTypeDTO);

                return existingDerivativeSubType;
            })
            .map(derivativeSubTypeRepository::save)
            .map(savedDerivativeSubType -> {
                derivativeSubTypeSearchRepository.save(savedDerivativeSubType);

                return savedDerivativeSubType;
            })
            .map(derivativeSubTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DerivativeSubTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DerivativeSubTypes");
        return derivativeSubTypeRepository.findAll(pageable).map(derivativeSubTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DerivativeSubTypeDTO> findOne(Long id) {
        log.debug("Request to get DerivativeSubType : {}", id);
        return derivativeSubTypeRepository.findById(id).map(derivativeSubTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DerivativeSubType : {}", id);
        derivativeSubTypeRepository.deleteById(id);
        derivativeSubTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DerivativeSubTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DerivativeSubTypes for query {}", query);
        return derivativeSubTypeSearchRepository.search(query, pageable).map(derivativeSubTypeMapper::toDto);
    }
}
