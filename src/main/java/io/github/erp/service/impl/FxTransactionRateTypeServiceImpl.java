package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.domain.FxTransactionRateType;
import io.github.erp.repository.FxTransactionRateTypeRepository;
import io.github.erp.repository.search.FxTransactionRateTypeSearchRepository;
import io.github.erp.service.FxTransactionRateTypeService;
import io.github.erp.service.dto.FxTransactionRateTypeDTO;
import io.github.erp.service.mapper.FxTransactionRateTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FxTransactionRateType}.
 */
@Service
@Transactional
public class FxTransactionRateTypeServiceImpl implements FxTransactionRateTypeService {

    private final Logger log = LoggerFactory.getLogger(FxTransactionRateTypeServiceImpl.class);

    private final FxTransactionRateTypeRepository fxTransactionRateTypeRepository;

    private final FxTransactionRateTypeMapper fxTransactionRateTypeMapper;

    private final FxTransactionRateTypeSearchRepository fxTransactionRateTypeSearchRepository;

    public FxTransactionRateTypeServiceImpl(
        FxTransactionRateTypeRepository fxTransactionRateTypeRepository,
        FxTransactionRateTypeMapper fxTransactionRateTypeMapper,
        FxTransactionRateTypeSearchRepository fxTransactionRateTypeSearchRepository
    ) {
        this.fxTransactionRateTypeRepository = fxTransactionRateTypeRepository;
        this.fxTransactionRateTypeMapper = fxTransactionRateTypeMapper;
        this.fxTransactionRateTypeSearchRepository = fxTransactionRateTypeSearchRepository;
    }

    @Override
    public FxTransactionRateTypeDTO save(FxTransactionRateTypeDTO fxTransactionRateTypeDTO) {
        log.debug("Request to save FxTransactionRateType : {}", fxTransactionRateTypeDTO);
        FxTransactionRateType fxTransactionRateType = fxTransactionRateTypeMapper.toEntity(fxTransactionRateTypeDTO);
        fxTransactionRateType = fxTransactionRateTypeRepository.save(fxTransactionRateType);
        FxTransactionRateTypeDTO result = fxTransactionRateTypeMapper.toDto(fxTransactionRateType);
        fxTransactionRateTypeSearchRepository.save(fxTransactionRateType);
        return result;
    }

    @Override
    public Optional<FxTransactionRateTypeDTO> partialUpdate(FxTransactionRateTypeDTO fxTransactionRateTypeDTO) {
        log.debug("Request to partially update FxTransactionRateType : {}", fxTransactionRateTypeDTO);

        return fxTransactionRateTypeRepository
            .findById(fxTransactionRateTypeDTO.getId())
            .map(existingFxTransactionRateType -> {
                fxTransactionRateTypeMapper.partialUpdate(existingFxTransactionRateType, fxTransactionRateTypeDTO);

                return existingFxTransactionRateType;
            })
            .map(fxTransactionRateTypeRepository::save)
            .map(savedFxTransactionRateType -> {
                fxTransactionRateTypeSearchRepository.save(savedFxTransactionRateType);

                return savedFxTransactionRateType;
            })
            .map(fxTransactionRateTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxTransactionRateTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FxTransactionRateTypes");
        return fxTransactionRateTypeRepository.findAll(pageable).map(fxTransactionRateTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FxTransactionRateTypeDTO> findOne(Long id) {
        log.debug("Request to get FxTransactionRateType : {}", id);
        return fxTransactionRateTypeRepository.findById(id).map(fxTransactionRateTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FxTransactionRateType : {}", id);
        fxTransactionRateTypeRepository.deleteById(id);
        fxTransactionRateTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxTransactionRateTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FxTransactionRateTypes for query {}", query);
        return fxTransactionRateTypeSearchRepository.search(query, pageable).map(fxTransactionRateTypeMapper::toDto);
    }
}
