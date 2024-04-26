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

import io.github.erp.domain.CrbSourceOfInformationType;
import io.github.erp.repository.CrbSourceOfInformationTypeRepository;
import io.github.erp.repository.search.CrbSourceOfInformationTypeSearchRepository;
import io.github.erp.service.CrbSourceOfInformationTypeService;
import io.github.erp.service.dto.CrbSourceOfInformationTypeDTO;
import io.github.erp.service.mapper.CrbSourceOfInformationTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbSourceOfInformationType}.
 */
@Service
@Transactional
public class CrbSourceOfInformationTypeServiceImpl implements CrbSourceOfInformationTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbSourceOfInformationTypeServiceImpl.class);

    private final CrbSourceOfInformationTypeRepository crbSourceOfInformationTypeRepository;

    private final CrbSourceOfInformationTypeMapper crbSourceOfInformationTypeMapper;

    private final CrbSourceOfInformationTypeSearchRepository crbSourceOfInformationTypeSearchRepository;

    public CrbSourceOfInformationTypeServiceImpl(
        CrbSourceOfInformationTypeRepository crbSourceOfInformationTypeRepository,
        CrbSourceOfInformationTypeMapper crbSourceOfInformationTypeMapper,
        CrbSourceOfInformationTypeSearchRepository crbSourceOfInformationTypeSearchRepository
    ) {
        this.crbSourceOfInformationTypeRepository = crbSourceOfInformationTypeRepository;
        this.crbSourceOfInformationTypeMapper = crbSourceOfInformationTypeMapper;
        this.crbSourceOfInformationTypeSearchRepository = crbSourceOfInformationTypeSearchRepository;
    }

    @Override
    public CrbSourceOfInformationTypeDTO save(CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO) {
        log.debug("Request to save CrbSourceOfInformationType : {}", crbSourceOfInformationTypeDTO);
        CrbSourceOfInformationType crbSourceOfInformationType = crbSourceOfInformationTypeMapper.toEntity(crbSourceOfInformationTypeDTO);
        crbSourceOfInformationType = crbSourceOfInformationTypeRepository.save(crbSourceOfInformationType);
        CrbSourceOfInformationTypeDTO result = crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationType);
        crbSourceOfInformationTypeSearchRepository.save(crbSourceOfInformationType);
        return result;
    }

    @Override
    public Optional<CrbSourceOfInformationTypeDTO> partialUpdate(CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO) {
        log.debug("Request to partially update CrbSourceOfInformationType : {}", crbSourceOfInformationTypeDTO);

        return crbSourceOfInformationTypeRepository
            .findById(crbSourceOfInformationTypeDTO.getId())
            .map(existingCrbSourceOfInformationType -> {
                crbSourceOfInformationTypeMapper.partialUpdate(existingCrbSourceOfInformationType, crbSourceOfInformationTypeDTO);

                return existingCrbSourceOfInformationType;
            })
            .map(crbSourceOfInformationTypeRepository::save)
            .map(savedCrbSourceOfInformationType -> {
                crbSourceOfInformationTypeSearchRepository.save(savedCrbSourceOfInformationType);

                return savedCrbSourceOfInformationType;
            })
            .map(crbSourceOfInformationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbSourceOfInformationTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbSourceOfInformationTypes");
        return crbSourceOfInformationTypeRepository.findAll(pageable).map(crbSourceOfInformationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbSourceOfInformationTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbSourceOfInformationType : {}", id);
        return crbSourceOfInformationTypeRepository.findById(id).map(crbSourceOfInformationTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbSourceOfInformationType : {}", id);
        crbSourceOfInformationTypeRepository.deleteById(id);
        crbSourceOfInformationTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbSourceOfInformationTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbSourceOfInformationTypes for query {}", query);
        return crbSourceOfInformationTypeSearchRepository.search(query, pageable).map(crbSourceOfInformationTypeMapper::toDto);
    }
}
