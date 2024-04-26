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

import io.github.erp.domain.FxTransactionChannelType;
import io.github.erp.repository.FxTransactionChannelTypeRepository;
import io.github.erp.repository.search.FxTransactionChannelTypeSearchRepository;
import io.github.erp.service.FxTransactionChannelTypeService;
import io.github.erp.service.dto.FxTransactionChannelTypeDTO;
import io.github.erp.service.mapper.FxTransactionChannelTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FxTransactionChannelType}.
 */
@Service
@Transactional
public class FxTransactionChannelTypeServiceImpl implements FxTransactionChannelTypeService {

    private final Logger log = LoggerFactory.getLogger(FxTransactionChannelTypeServiceImpl.class);

    private final FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository;

    private final FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper;

    private final FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository;

    public FxTransactionChannelTypeServiceImpl(
        FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository,
        FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper,
        FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository
    ) {
        this.fxTransactionChannelTypeRepository = fxTransactionChannelTypeRepository;
        this.fxTransactionChannelTypeMapper = fxTransactionChannelTypeMapper;
        this.fxTransactionChannelTypeSearchRepository = fxTransactionChannelTypeSearchRepository;
    }

    @Override
    public FxTransactionChannelTypeDTO save(FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO) {
        log.debug("Request to save FxTransactionChannelType : {}", fxTransactionChannelTypeDTO);
        FxTransactionChannelType fxTransactionChannelType = fxTransactionChannelTypeMapper.toEntity(fxTransactionChannelTypeDTO);
        fxTransactionChannelType = fxTransactionChannelTypeRepository.save(fxTransactionChannelType);
        FxTransactionChannelTypeDTO result = fxTransactionChannelTypeMapper.toDto(fxTransactionChannelType);
        fxTransactionChannelTypeSearchRepository.save(fxTransactionChannelType);
        return result;
    }

    @Override
    public Optional<FxTransactionChannelTypeDTO> partialUpdate(FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO) {
        log.debug("Request to partially update FxTransactionChannelType : {}", fxTransactionChannelTypeDTO);

        return fxTransactionChannelTypeRepository
            .findById(fxTransactionChannelTypeDTO.getId())
            .map(existingFxTransactionChannelType -> {
                fxTransactionChannelTypeMapper.partialUpdate(existingFxTransactionChannelType, fxTransactionChannelTypeDTO);

                return existingFxTransactionChannelType;
            })
            .map(fxTransactionChannelTypeRepository::save)
            .map(savedFxTransactionChannelType -> {
                fxTransactionChannelTypeSearchRepository.save(savedFxTransactionChannelType);

                return savedFxTransactionChannelType;
            })
            .map(fxTransactionChannelTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxTransactionChannelTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FxTransactionChannelTypes");
        return fxTransactionChannelTypeRepository.findAll(pageable).map(fxTransactionChannelTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FxTransactionChannelTypeDTO> findOne(Long id) {
        log.debug("Request to get FxTransactionChannelType : {}", id);
        return fxTransactionChannelTypeRepository.findById(id).map(fxTransactionChannelTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FxTransactionChannelType : {}", id);
        fxTransactionChannelTypeRepository.deleteById(id);
        fxTransactionChannelTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxTransactionChannelTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FxTransactionChannelTypes for query {}", query);
        return fxTransactionChannelTypeSearchRepository.search(query, pageable).map(fxTransactionChannelTypeMapper::toDto);
    }
}
