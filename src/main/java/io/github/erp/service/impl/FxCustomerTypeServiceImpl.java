package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.FxCustomerType;
import io.github.erp.repository.FxCustomerTypeRepository;
import io.github.erp.repository.search.FxCustomerTypeSearchRepository;
import io.github.erp.service.FxCustomerTypeService;
import io.github.erp.service.dto.FxCustomerTypeDTO;
import io.github.erp.service.mapper.FxCustomerTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FxCustomerType}.
 */
@Service
@Transactional
public class FxCustomerTypeServiceImpl implements FxCustomerTypeService {

    private final Logger log = LoggerFactory.getLogger(FxCustomerTypeServiceImpl.class);

    private final FxCustomerTypeRepository fxCustomerTypeRepository;

    private final FxCustomerTypeMapper fxCustomerTypeMapper;

    private final FxCustomerTypeSearchRepository fxCustomerTypeSearchRepository;

    public FxCustomerTypeServiceImpl(
        FxCustomerTypeRepository fxCustomerTypeRepository,
        FxCustomerTypeMapper fxCustomerTypeMapper,
        FxCustomerTypeSearchRepository fxCustomerTypeSearchRepository
    ) {
        this.fxCustomerTypeRepository = fxCustomerTypeRepository;
        this.fxCustomerTypeMapper = fxCustomerTypeMapper;
        this.fxCustomerTypeSearchRepository = fxCustomerTypeSearchRepository;
    }

    @Override
    public FxCustomerTypeDTO save(FxCustomerTypeDTO fxCustomerTypeDTO) {
        log.debug("Request to save FxCustomerType : {}", fxCustomerTypeDTO);
        FxCustomerType fxCustomerType = fxCustomerTypeMapper.toEntity(fxCustomerTypeDTO);
        fxCustomerType = fxCustomerTypeRepository.save(fxCustomerType);
        FxCustomerTypeDTO result = fxCustomerTypeMapper.toDto(fxCustomerType);
        fxCustomerTypeSearchRepository.save(fxCustomerType);
        return result;
    }

    @Override
    public Optional<FxCustomerTypeDTO> partialUpdate(FxCustomerTypeDTO fxCustomerTypeDTO) {
        log.debug("Request to partially update FxCustomerType : {}", fxCustomerTypeDTO);

        return fxCustomerTypeRepository
            .findById(fxCustomerTypeDTO.getId())
            .map(existingFxCustomerType -> {
                fxCustomerTypeMapper.partialUpdate(existingFxCustomerType, fxCustomerTypeDTO);

                return existingFxCustomerType;
            })
            .map(fxCustomerTypeRepository::save)
            .map(savedFxCustomerType -> {
                fxCustomerTypeSearchRepository.save(savedFxCustomerType);

                return savedFxCustomerType;
            })
            .map(fxCustomerTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxCustomerTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FxCustomerTypes");
        return fxCustomerTypeRepository.findAll(pageable).map(fxCustomerTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FxCustomerTypeDTO> findOne(Long id) {
        log.debug("Request to get FxCustomerType : {}", id);
        return fxCustomerTypeRepository.findById(id).map(fxCustomerTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FxCustomerType : {}", id);
        fxCustomerTypeRepository.deleteById(id);
        fxCustomerTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxCustomerTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FxCustomerTypes for query {}", query);
        return fxCustomerTypeSearchRepository.search(query, pageable).map(fxCustomerTypeMapper::toDto);
    }
}
