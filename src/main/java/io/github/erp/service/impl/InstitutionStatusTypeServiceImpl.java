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

import io.github.erp.domain.InstitutionStatusType;
import io.github.erp.repository.InstitutionStatusTypeRepository;
import io.github.erp.repository.search.InstitutionStatusTypeSearchRepository;
import io.github.erp.service.InstitutionStatusTypeService;
import io.github.erp.service.dto.InstitutionStatusTypeDTO;
import io.github.erp.service.mapper.InstitutionStatusTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InstitutionStatusType}.
 */
@Service
@Transactional
public class InstitutionStatusTypeServiceImpl implements InstitutionStatusTypeService {

    private final Logger log = LoggerFactory.getLogger(InstitutionStatusTypeServiceImpl.class);

    private final InstitutionStatusTypeRepository institutionStatusTypeRepository;

    private final InstitutionStatusTypeMapper institutionStatusTypeMapper;

    private final InstitutionStatusTypeSearchRepository institutionStatusTypeSearchRepository;

    public InstitutionStatusTypeServiceImpl(
        InstitutionStatusTypeRepository institutionStatusTypeRepository,
        InstitutionStatusTypeMapper institutionStatusTypeMapper,
        InstitutionStatusTypeSearchRepository institutionStatusTypeSearchRepository
    ) {
        this.institutionStatusTypeRepository = institutionStatusTypeRepository;
        this.institutionStatusTypeMapper = institutionStatusTypeMapper;
        this.institutionStatusTypeSearchRepository = institutionStatusTypeSearchRepository;
    }

    @Override
    public InstitutionStatusTypeDTO save(InstitutionStatusTypeDTO institutionStatusTypeDTO) {
        log.debug("Request to save InstitutionStatusType : {}", institutionStatusTypeDTO);
        InstitutionStatusType institutionStatusType = institutionStatusTypeMapper.toEntity(institutionStatusTypeDTO);
        institutionStatusType = institutionStatusTypeRepository.save(institutionStatusType);
        InstitutionStatusTypeDTO result = institutionStatusTypeMapper.toDto(institutionStatusType);
        institutionStatusTypeSearchRepository.save(institutionStatusType);
        return result;
    }

    @Override
    public Optional<InstitutionStatusTypeDTO> partialUpdate(InstitutionStatusTypeDTO institutionStatusTypeDTO) {
        log.debug("Request to partially update InstitutionStatusType : {}", institutionStatusTypeDTO);

        return institutionStatusTypeRepository
            .findById(institutionStatusTypeDTO.getId())
            .map(existingInstitutionStatusType -> {
                institutionStatusTypeMapper.partialUpdate(existingInstitutionStatusType, institutionStatusTypeDTO);

                return existingInstitutionStatusType;
            })
            .map(institutionStatusTypeRepository::save)
            .map(savedInstitutionStatusType -> {
                institutionStatusTypeSearchRepository.save(savedInstitutionStatusType);

                return savedInstitutionStatusType;
            })
            .map(institutionStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionStatusTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InstitutionStatusTypes");
        return institutionStatusTypeRepository.findAll(pageable).map(institutionStatusTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstitutionStatusTypeDTO> findOne(Long id) {
        log.debug("Request to get InstitutionStatusType : {}", id);
        return institutionStatusTypeRepository.findById(id).map(institutionStatusTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InstitutionStatusType : {}", id);
        institutionStatusTypeRepository.deleteById(id);
        institutionStatusTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionStatusTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InstitutionStatusTypes for query {}", query);
        return institutionStatusTypeSearchRepository.search(query, pageable).map(institutionStatusTypeMapper::toDto);
    }
}
