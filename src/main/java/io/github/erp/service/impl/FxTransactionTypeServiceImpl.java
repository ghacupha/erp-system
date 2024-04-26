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

import io.github.erp.domain.FxTransactionType;
import io.github.erp.repository.FxTransactionTypeRepository;
import io.github.erp.repository.search.FxTransactionTypeSearchRepository;
import io.github.erp.service.FxTransactionTypeService;
import io.github.erp.service.dto.FxTransactionTypeDTO;
import io.github.erp.service.mapper.FxTransactionTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FxTransactionType}.
 */
@Service
@Transactional
public class FxTransactionTypeServiceImpl implements FxTransactionTypeService {

    private final Logger log = LoggerFactory.getLogger(FxTransactionTypeServiceImpl.class);

    private final FxTransactionTypeRepository fxTransactionTypeRepository;

    private final FxTransactionTypeMapper fxTransactionTypeMapper;

    private final FxTransactionTypeSearchRepository fxTransactionTypeSearchRepository;

    public FxTransactionTypeServiceImpl(
        FxTransactionTypeRepository fxTransactionTypeRepository,
        FxTransactionTypeMapper fxTransactionTypeMapper,
        FxTransactionTypeSearchRepository fxTransactionTypeSearchRepository
    ) {
        this.fxTransactionTypeRepository = fxTransactionTypeRepository;
        this.fxTransactionTypeMapper = fxTransactionTypeMapper;
        this.fxTransactionTypeSearchRepository = fxTransactionTypeSearchRepository;
    }

    @Override
    public FxTransactionTypeDTO save(FxTransactionTypeDTO fxTransactionTypeDTO) {
        log.debug("Request to save FxTransactionType : {}", fxTransactionTypeDTO);
        FxTransactionType fxTransactionType = fxTransactionTypeMapper.toEntity(fxTransactionTypeDTO);
        fxTransactionType = fxTransactionTypeRepository.save(fxTransactionType);
        FxTransactionTypeDTO result = fxTransactionTypeMapper.toDto(fxTransactionType);
        fxTransactionTypeSearchRepository.save(fxTransactionType);
        return result;
    }

    @Override
    public Optional<FxTransactionTypeDTO> partialUpdate(FxTransactionTypeDTO fxTransactionTypeDTO) {
        log.debug("Request to partially update FxTransactionType : {}", fxTransactionTypeDTO);

        return fxTransactionTypeRepository
            .findById(fxTransactionTypeDTO.getId())
            .map(existingFxTransactionType -> {
                fxTransactionTypeMapper.partialUpdate(existingFxTransactionType, fxTransactionTypeDTO);

                return existingFxTransactionType;
            })
            .map(fxTransactionTypeRepository::save)
            .map(savedFxTransactionType -> {
                fxTransactionTypeSearchRepository.save(savedFxTransactionType);

                return savedFxTransactionType;
            })
            .map(fxTransactionTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxTransactionTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FxTransactionTypes");
        return fxTransactionTypeRepository.findAll(pageable).map(fxTransactionTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FxTransactionTypeDTO> findOne(Long id) {
        log.debug("Request to get FxTransactionType : {}", id);
        return fxTransactionTypeRepository.findById(id).map(fxTransactionTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FxTransactionType : {}", id);
        fxTransactionTypeRepository.deleteById(id);
        fxTransactionTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxTransactionTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FxTransactionTypes for query {}", query);
        return fxTransactionTypeSearchRepository.search(query, pageable).map(fxTransactionTypeMapper::toDto);
    }
}
