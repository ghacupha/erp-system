package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.SecurityClassificationType;
import io.github.erp.repository.SecurityClassificationTypeRepository;
import io.github.erp.repository.search.SecurityClassificationTypeSearchRepository;
import io.github.erp.service.SecurityClassificationTypeService;
import io.github.erp.service.dto.SecurityClassificationTypeDTO;
import io.github.erp.service.mapper.SecurityClassificationTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SecurityClassificationType}.
 */
@Service
@Transactional
public class SecurityClassificationTypeServiceImpl implements SecurityClassificationTypeService {

    private final Logger log = LoggerFactory.getLogger(SecurityClassificationTypeServiceImpl.class);

    private final SecurityClassificationTypeRepository securityClassificationTypeRepository;

    private final SecurityClassificationTypeMapper securityClassificationTypeMapper;

    private final SecurityClassificationTypeSearchRepository securityClassificationTypeSearchRepository;

    public SecurityClassificationTypeServiceImpl(
        SecurityClassificationTypeRepository securityClassificationTypeRepository,
        SecurityClassificationTypeMapper securityClassificationTypeMapper,
        SecurityClassificationTypeSearchRepository securityClassificationTypeSearchRepository
    ) {
        this.securityClassificationTypeRepository = securityClassificationTypeRepository;
        this.securityClassificationTypeMapper = securityClassificationTypeMapper;
        this.securityClassificationTypeSearchRepository = securityClassificationTypeSearchRepository;
    }

    @Override
    public SecurityClassificationTypeDTO save(SecurityClassificationTypeDTO securityClassificationTypeDTO) {
        log.debug("Request to save SecurityClassificationType : {}", securityClassificationTypeDTO);
        SecurityClassificationType securityClassificationType = securityClassificationTypeMapper.toEntity(securityClassificationTypeDTO);
        securityClassificationType = securityClassificationTypeRepository.save(securityClassificationType);
        SecurityClassificationTypeDTO result = securityClassificationTypeMapper.toDto(securityClassificationType);
        securityClassificationTypeSearchRepository.save(securityClassificationType);
        return result;
    }

    @Override
    public Optional<SecurityClassificationTypeDTO> partialUpdate(SecurityClassificationTypeDTO securityClassificationTypeDTO) {
        log.debug("Request to partially update SecurityClassificationType : {}", securityClassificationTypeDTO);

        return securityClassificationTypeRepository
            .findById(securityClassificationTypeDTO.getId())
            .map(existingSecurityClassificationType -> {
                securityClassificationTypeMapper.partialUpdate(existingSecurityClassificationType, securityClassificationTypeDTO);

                return existingSecurityClassificationType;
            })
            .map(securityClassificationTypeRepository::save)
            .map(savedSecurityClassificationType -> {
                securityClassificationTypeSearchRepository.save(savedSecurityClassificationType);

                return savedSecurityClassificationType;
            })
            .map(securityClassificationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityClassificationTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityClassificationTypes");
        return securityClassificationTypeRepository.findAll(pageable).map(securityClassificationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecurityClassificationTypeDTO> findOne(Long id) {
        log.debug("Request to get SecurityClassificationType : {}", id);
        return securityClassificationTypeRepository.findById(id).map(securityClassificationTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SecurityClassificationType : {}", id);
        securityClassificationTypeRepository.deleteById(id);
        securityClassificationTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityClassificationTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityClassificationTypes for query {}", query);
        return securityClassificationTypeSearchRepository.search(query, pageable).map(securityClassificationTypeMapper::toDto);
    }
}
