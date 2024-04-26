package io.github.erp.service.impl;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
