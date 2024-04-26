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

import io.github.erp.domain.CrbCreditFacilityType;
import io.github.erp.repository.CrbCreditFacilityTypeRepository;
import io.github.erp.repository.search.CrbCreditFacilityTypeSearchRepository;
import io.github.erp.service.CrbCreditFacilityTypeService;
import io.github.erp.service.dto.CrbCreditFacilityTypeDTO;
import io.github.erp.service.mapper.CrbCreditFacilityTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbCreditFacilityType}.
 */
@Service
@Transactional
public class CrbCreditFacilityTypeServiceImpl implements CrbCreditFacilityTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbCreditFacilityTypeServiceImpl.class);

    private final CrbCreditFacilityTypeRepository crbCreditFacilityTypeRepository;

    private final CrbCreditFacilityTypeMapper crbCreditFacilityTypeMapper;

    private final CrbCreditFacilityTypeSearchRepository crbCreditFacilityTypeSearchRepository;

    public CrbCreditFacilityTypeServiceImpl(
        CrbCreditFacilityTypeRepository crbCreditFacilityTypeRepository,
        CrbCreditFacilityTypeMapper crbCreditFacilityTypeMapper,
        CrbCreditFacilityTypeSearchRepository crbCreditFacilityTypeSearchRepository
    ) {
        this.crbCreditFacilityTypeRepository = crbCreditFacilityTypeRepository;
        this.crbCreditFacilityTypeMapper = crbCreditFacilityTypeMapper;
        this.crbCreditFacilityTypeSearchRepository = crbCreditFacilityTypeSearchRepository;
    }

    @Override
    public CrbCreditFacilityTypeDTO save(CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO) {
        log.debug("Request to save CrbCreditFacilityType : {}", crbCreditFacilityTypeDTO);
        CrbCreditFacilityType crbCreditFacilityType = crbCreditFacilityTypeMapper.toEntity(crbCreditFacilityTypeDTO);
        crbCreditFacilityType = crbCreditFacilityTypeRepository.save(crbCreditFacilityType);
        CrbCreditFacilityTypeDTO result = crbCreditFacilityTypeMapper.toDto(crbCreditFacilityType);
        crbCreditFacilityTypeSearchRepository.save(crbCreditFacilityType);
        return result;
    }

    @Override
    public Optional<CrbCreditFacilityTypeDTO> partialUpdate(CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO) {
        log.debug("Request to partially update CrbCreditFacilityType : {}", crbCreditFacilityTypeDTO);

        return crbCreditFacilityTypeRepository
            .findById(crbCreditFacilityTypeDTO.getId())
            .map(existingCrbCreditFacilityType -> {
                crbCreditFacilityTypeMapper.partialUpdate(existingCrbCreditFacilityType, crbCreditFacilityTypeDTO);

                return existingCrbCreditFacilityType;
            })
            .map(crbCreditFacilityTypeRepository::save)
            .map(savedCrbCreditFacilityType -> {
                crbCreditFacilityTypeSearchRepository.save(savedCrbCreditFacilityType);

                return savedCrbCreditFacilityType;
            })
            .map(crbCreditFacilityTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbCreditFacilityTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbCreditFacilityTypes");
        return crbCreditFacilityTypeRepository.findAll(pageable).map(crbCreditFacilityTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbCreditFacilityTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbCreditFacilityType : {}", id);
        return crbCreditFacilityTypeRepository.findById(id).map(crbCreditFacilityTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbCreditFacilityType : {}", id);
        crbCreditFacilityTypeRepository.deleteById(id);
        crbCreditFacilityTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbCreditFacilityTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbCreditFacilityTypes for query {}", query);
        return crbCreditFacilityTypeSearchRepository.search(query, pageable).map(crbCreditFacilityTypeMapper::toDto);
    }
}
