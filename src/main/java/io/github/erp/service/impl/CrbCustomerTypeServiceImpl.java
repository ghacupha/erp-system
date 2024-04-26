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

import io.github.erp.domain.CrbCustomerType;
import io.github.erp.repository.CrbCustomerTypeRepository;
import io.github.erp.repository.search.CrbCustomerTypeSearchRepository;
import io.github.erp.service.CrbCustomerTypeService;
import io.github.erp.service.dto.CrbCustomerTypeDTO;
import io.github.erp.service.mapper.CrbCustomerTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbCustomerType}.
 */
@Service
@Transactional
public class CrbCustomerTypeServiceImpl implements CrbCustomerTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbCustomerTypeServiceImpl.class);

    private final CrbCustomerTypeRepository crbCustomerTypeRepository;

    private final CrbCustomerTypeMapper crbCustomerTypeMapper;

    private final CrbCustomerTypeSearchRepository crbCustomerTypeSearchRepository;

    public CrbCustomerTypeServiceImpl(
        CrbCustomerTypeRepository crbCustomerTypeRepository,
        CrbCustomerTypeMapper crbCustomerTypeMapper,
        CrbCustomerTypeSearchRepository crbCustomerTypeSearchRepository
    ) {
        this.crbCustomerTypeRepository = crbCustomerTypeRepository;
        this.crbCustomerTypeMapper = crbCustomerTypeMapper;
        this.crbCustomerTypeSearchRepository = crbCustomerTypeSearchRepository;
    }

    @Override
    public CrbCustomerTypeDTO save(CrbCustomerTypeDTO crbCustomerTypeDTO) {
        log.debug("Request to save CrbCustomerType : {}", crbCustomerTypeDTO);
        CrbCustomerType crbCustomerType = crbCustomerTypeMapper.toEntity(crbCustomerTypeDTO);
        crbCustomerType = crbCustomerTypeRepository.save(crbCustomerType);
        CrbCustomerTypeDTO result = crbCustomerTypeMapper.toDto(crbCustomerType);
        crbCustomerTypeSearchRepository.save(crbCustomerType);
        return result;
    }

    @Override
    public Optional<CrbCustomerTypeDTO> partialUpdate(CrbCustomerTypeDTO crbCustomerTypeDTO) {
        log.debug("Request to partially update CrbCustomerType : {}", crbCustomerTypeDTO);

        return crbCustomerTypeRepository
            .findById(crbCustomerTypeDTO.getId())
            .map(existingCrbCustomerType -> {
                crbCustomerTypeMapper.partialUpdate(existingCrbCustomerType, crbCustomerTypeDTO);

                return existingCrbCustomerType;
            })
            .map(crbCustomerTypeRepository::save)
            .map(savedCrbCustomerType -> {
                crbCustomerTypeSearchRepository.save(savedCrbCustomerType);

                return savedCrbCustomerType;
            })
            .map(crbCustomerTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbCustomerTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbCustomerTypes");
        return crbCustomerTypeRepository.findAll(pageable).map(crbCustomerTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbCustomerTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbCustomerType : {}", id);
        return crbCustomerTypeRepository.findById(id).map(crbCustomerTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbCustomerType : {}", id);
        crbCustomerTypeRepository.deleteById(id);
        crbCustomerTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbCustomerTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbCustomerTypes for query {}", query);
        return crbCustomerTypeSearchRepository.search(query, pageable).map(crbCustomerTypeMapper::toDto);
    }
}
