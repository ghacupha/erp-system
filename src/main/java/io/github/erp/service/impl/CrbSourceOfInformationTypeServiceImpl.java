package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
