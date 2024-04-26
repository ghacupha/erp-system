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

import io.github.erp.domain.SecurityType;
import io.github.erp.repository.SecurityTypeRepository;
import io.github.erp.repository.search.SecurityTypeSearchRepository;
import io.github.erp.service.SecurityTypeService;
import io.github.erp.service.dto.SecurityTypeDTO;
import io.github.erp.service.mapper.SecurityTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SecurityType}.
 */
@Service
@Transactional
public class SecurityTypeServiceImpl implements SecurityTypeService {

    private final Logger log = LoggerFactory.getLogger(SecurityTypeServiceImpl.class);

    private final SecurityTypeRepository securityTypeRepository;

    private final SecurityTypeMapper securityTypeMapper;

    private final SecurityTypeSearchRepository securityTypeSearchRepository;

    public SecurityTypeServiceImpl(
        SecurityTypeRepository securityTypeRepository,
        SecurityTypeMapper securityTypeMapper,
        SecurityTypeSearchRepository securityTypeSearchRepository
    ) {
        this.securityTypeRepository = securityTypeRepository;
        this.securityTypeMapper = securityTypeMapper;
        this.securityTypeSearchRepository = securityTypeSearchRepository;
    }

    @Override
    public SecurityTypeDTO save(SecurityTypeDTO securityTypeDTO) {
        log.debug("Request to save SecurityType : {}", securityTypeDTO);
        SecurityType securityType = securityTypeMapper.toEntity(securityTypeDTO);
        securityType = securityTypeRepository.save(securityType);
        SecurityTypeDTO result = securityTypeMapper.toDto(securityType);
        securityTypeSearchRepository.save(securityType);
        return result;
    }

    @Override
    public Optional<SecurityTypeDTO> partialUpdate(SecurityTypeDTO securityTypeDTO) {
        log.debug("Request to partially update SecurityType : {}", securityTypeDTO);

        return securityTypeRepository
            .findById(securityTypeDTO.getId())
            .map(existingSecurityType -> {
                securityTypeMapper.partialUpdate(existingSecurityType, securityTypeDTO);

                return existingSecurityType;
            })
            .map(securityTypeRepository::save)
            .map(savedSecurityType -> {
                securityTypeSearchRepository.save(savedSecurityType);

                return savedSecurityType;
            })
            .map(securityTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityTypes");
        return securityTypeRepository.findAll(pageable).map(securityTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecurityTypeDTO> findOne(Long id) {
        log.debug("Request to get SecurityType : {}", id);
        return securityTypeRepository.findById(id).map(securityTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SecurityType : {}", id);
        securityTypeRepository.deleteById(id);
        securityTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityTypes for query {}", query);
        return securityTypeSearchRepository.search(query, pageable).map(securityTypeMapper::toDto);
    }
}
