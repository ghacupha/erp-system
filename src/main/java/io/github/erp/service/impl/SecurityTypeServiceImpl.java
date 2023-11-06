package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
