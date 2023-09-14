package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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

import io.github.erp.domain.CrbAgentServiceType;
import io.github.erp.repository.CrbAgentServiceTypeRepository;
import io.github.erp.repository.search.CrbAgentServiceTypeSearchRepository;
import io.github.erp.service.CrbAgentServiceTypeService;
import io.github.erp.service.dto.CrbAgentServiceTypeDTO;
import io.github.erp.service.mapper.CrbAgentServiceTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbAgentServiceType}.
 */
@Service
@Transactional
public class CrbAgentServiceTypeServiceImpl implements CrbAgentServiceTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbAgentServiceTypeServiceImpl.class);

    private final CrbAgentServiceTypeRepository crbAgentServiceTypeRepository;

    private final CrbAgentServiceTypeMapper crbAgentServiceTypeMapper;

    private final CrbAgentServiceTypeSearchRepository crbAgentServiceTypeSearchRepository;

    public CrbAgentServiceTypeServiceImpl(
        CrbAgentServiceTypeRepository crbAgentServiceTypeRepository,
        CrbAgentServiceTypeMapper crbAgentServiceTypeMapper,
        CrbAgentServiceTypeSearchRepository crbAgentServiceTypeSearchRepository
    ) {
        this.crbAgentServiceTypeRepository = crbAgentServiceTypeRepository;
        this.crbAgentServiceTypeMapper = crbAgentServiceTypeMapper;
        this.crbAgentServiceTypeSearchRepository = crbAgentServiceTypeSearchRepository;
    }

    @Override
    public CrbAgentServiceTypeDTO save(CrbAgentServiceTypeDTO crbAgentServiceTypeDTO) {
        log.debug("Request to save CrbAgentServiceType : {}", crbAgentServiceTypeDTO);
        CrbAgentServiceType crbAgentServiceType = crbAgentServiceTypeMapper.toEntity(crbAgentServiceTypeDTO);
        crbAgentServiceType = crbAgentServiceTypeRepository.save(crbAgentServiceType);
        CrbAgentServiceTypeDTO result = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);
        crbAgentServiceTypeSearchRepository.save(crbAgentServiceType);
        return result;
    }

    @Override
    public Optional<CrbAgentServiceTypeDTO> partialUpdate(CrbAgentServiceTypeDTO crbAgentServiceTypeDTO) {
        log.debug("Request to partially update CrbAgentServiceType : {}", crbAgentServiceTypeDTO);

        return crbAgentServiceTypeRepository
            .findById(crbAgentServiceTypeDTO.getId())
            .map(existingCrbAgentServiceType -> {
                crbAgentServiceTypeMapper.partialUpdate(existingCrbAgentServiceType, crbAgentServiceTypeDTO);

                return existingCrbAgentServiceType;
            })
            .map(crbAgentServiceTypeRepository::save)
            .map(savedCrbAgentServiceType -> {
                crbAgentServiceTypeSearchRepository.save(savedCrbAgentServiceType);

                return savedCrbAgentServiceType;
            })
            .map(crbAgentServiceTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAgentServiceTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbAgentServiceTypes");
        return crbAgentServiceTypeRepository.findAll(pageable).map(crbAgentServiceTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbAgentServiceTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbAgentServiceType : {}", id);
        return crbAgentServiceTypeRepository.findById(id).map(crbAgentServiceTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbAgentServiceType : {}", id);
        crbAgentServiceTypeRepository.deleteById(id);
        crbAgentServiceTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAgentServiceTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbAgentServiceTypes for query {}", query);
        return crbAgentServiceTypeSearchRepository.search(query, pageable).map(crbAgentServiceTypeMapper::toDto);
    }
}
