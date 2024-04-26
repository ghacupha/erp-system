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

import io.github.erp.domain.FxRateType;
import io.github.erp.repository.FxRateTypeRepository;
import io.github.erp.repository.search.FxRateTypeSearchRepository;
import io.github.erp.service.FxRateTypeService;
import io.github.erp.service.dto.FxRateTypeDTO;
import io.github.erp.service.mapper.FxRateTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FxRateType}.
 */
@Service
@Transactional
public class FxRateTypeServiceImpl implements FxRateTypeService {

    private final Logger log = LoggerFactory.getLogger(FxRateTypeServiceImpl.class);

    private final FxRateTypeRepository fxRateTypeRepository;

    private final FxRateTypeMapper fxRateTypeMapper;

    private final FxRateTypeSearchRepository fxRateTypeSearchRepository;

    public FxRateTypeServiceImpl(
        FxRateTypeRepository fxRateTypeRepository,
        FxRateTypeMapper fxRateTypeMapper,
        FxRateTypeSearchRepository fxRateTypeSearchRepository
    ) {
        this.fxRateTypeRepository = fxRateTypeRepository;
        this.fxRateTypeMapper = fxRateTypeMapper;
        this.fxRateTypeSearchRepository = fxRateTypeSearchRepository;
    }

    @Override
    public FxRateTypeDTO save(FxRateTypeDTO fxRateTypeDTO) {
        log.debug("Request to save FxRateType : {}", fxRateTypeDTO);
        FxRateType fxRateType = fxRateTypeMapper.toEntity(fxRateTypeDTO);
        fxRateType = fxRateTypeRepository.save(fxRateType);
        FxRateTypeDTO result = fxRateTypeMapper.toDto(fxRateType);
        fxRateTypeSearchRepository.save(fxRateType);
        return result;
    }

    @Override
    public Optional<FxRateTypeDTO> partialUpdate(FxRateTypeDTO fxRateTypeDTO) {
        log.debug("Request to partially update FxRateType : {}", fxRateTypeDTO);

        return fxRateTypeRepository
            .findById(fxRateTypeDTO.getId())
            .map(existingFxRateType -> {
                fxRateTypeMapper.partialUpdate(existingFxRateType, fxRateTypeDTO);

                return existingFxRateType;
            })
            .map(fxRateTypeRepository::save)
            .map(savedFxRateType -> {
                fxRateTypeSearchRepository.save(savedFxRateType);

                return savedFxRateType;
            })
            .map(fxRateTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxRateTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FxRateTypes");
        return fxRateTypeRepository.findAll(pageable).map(fxRateTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FxRateTypeDTO> findOne(Long id) {
        log.debug("Request to get FxRateType : {}", id);
        return fxRateTypeRepository.findById(id).map(fxRateTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FxRateType : {}", id);
        fxRateTypeRepository.deleteById(id);
        fxRateTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FxRateTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FxRateTypes for query {}", query);
        return fxRateTypeSearchRepository.search(query, pageable).map(fxRateTypeMapper::toDto);
    }
}
