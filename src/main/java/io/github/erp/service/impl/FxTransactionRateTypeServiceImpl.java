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
