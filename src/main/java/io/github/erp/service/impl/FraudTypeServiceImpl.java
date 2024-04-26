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

import io.github.erp.domain.FraudType;
import io.github.erp.repository.FraudTypeRepository;
import io.github.erp.repository.search.FraudTypeSearchRepository;
import io.github.erp.service.FraudTypeService;
import io.github.erp.service.dto.FraudTypeDTO;
import io.github.erp.service.mapper.FraudTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FraudType}.
 */
@Service
@Transactional
public class FraudTypeServiceImpl implements FraudTypeService {

    private final Logger log = LoggerFactory.getLogger(FraudTypeServiceImpl.class);

    private final FraudTypeRepository fraudTypeRepository;

    private final FraudTypeMapper fraudTypeMapper;

    private final FraudTypeSearchRepository fraudTypeSearchRepository;

    public FraudTypeServiceImpl(
        FraudTypeRepository fraudTypeRepository,
        FraudTypeMapper fraudTypeMapper,
        FraudTypeSearchRepository fraudTypeSearchRepository
    ) {
        this.fraudTypeRepository = fraudTypeRepository;
        this.fraudTypeMapper = fraudTypeMapper;
        this.fraudTypeSearchRepository = fraudTypeSearchRepository;
    }

    @Override
    public FraudTypeDTO save(FraudTypeDTO fraudTypeDTO) {
        log.debug("Request to save FraudType : {}", fraudTypeDTO);
        FraudType fraudType = fraudTypeMapper.toEntity(fraudTypeDTO);
        fraudType = fraudTypeRepository.save(fraudType);
        FraudTypeDTO result = fraudTypeMapper.toDto(fraudType);
        fraudTypeSearchRepository.save(fraudType);
        return result;
    }

    @Override
    public Optional<FraudTypeDTO> partialUpdate(FraudTypeDTO fraudTypeDTO) {
        log.debug("Request to partially update FraudType : {}", fraudTypeDTO);

        return fraudTypeRepository
            .findById(fraudTypeDTO.getId())
            .map(existingFraudType -> {
                fraudTypeMapper.partialUpdate(existingFraudType, fraudTypeDTO);

                return existingFraudType;
            })
            .map(fraudTypeRepository::save)
            .map(savedFraudType -> {
                fraudTypeSearchRepository.save(savedFraudType);

                return savedFraudType;
            })
            .map(fraudTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraudTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FraudTypes");
        return fraudTypeRepository.findAll(pageable).map(fraudTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FraudTypeDTO> findOne(Long id) {
        log.debug("Request to get FraudType : {}", id);
        return fraudTypeRepository.findById(id).map(fraudTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FraudType : {}", id);
        fraudTypeRepository.deleteById(id);
        fraudTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraudTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FraudTypes for query {}", query);
        return fraudTypeSearchRepository.search(query, pageable).map(fraudTypeMapper::toDto);
    }
}
