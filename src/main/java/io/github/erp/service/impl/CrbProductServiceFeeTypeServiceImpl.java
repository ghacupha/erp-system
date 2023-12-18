package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.domain.CrbProductServiceFeeType;
import io.github.erp.repository.CrbProductServiceFeeTypeRepository;
import io.github.erp.repository.search.CrbProductServiceFeeTypeSearchRepository;
import io.github.erp.service.CrbProductServiceFeeTypeService;
import io.github.erp.service.dto.CrbProductServiceFeeTypeDTO;
import io.github.erp.service.mapper.CrbProductServiceFeeTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbProductServiceFeeType}.
 */
@Service
@Transactional
public class CrbProductServiceFeeTypeServiceImpl implements CrbProductServiceFeeTypeService {

    private final Logger log = LoggerFactory.getLogger(CrbProductServiceFeeTypeServiceImpl.class);

    private final CrbProductServiceFeeTypeRepository crbProductServiceFeeTypeRepository;

    private final CrbProductServiceFeeTypeMapper crbProductServiceFeeTypeMapper;

    private final CrbProductServiceFeeTypeSearchRepository crbProductServiceFeeTypeSearchRepository;

    public CrbProductServiceFeeTypeServiceImpl(
        CrbProductServiceFeeTypeRepository crbProductServiceFeeTypeRepository,
        CrbProductServiceFeeTypeMapper crbProductServiceFeeTypeMapper,
        CrbProductServiceFeeTypeSearchRepository crbProductServiceFeeTypeSearchRepository
    ) {
        this.crbProductServiceFeeTypeRepository = crbProductServiceFeeTypeRepository;
        this.crbProductServiceFeeTypeMapper = crbProductServiceFeeTypeMapper;
        this.crbProductServiceFeeTypeSearchRepository = crbProductServiceFeeTypeSearchRepository;
    }

    @Override
    public CrbProductServiceFeeTypeDTO save(CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO) {
        log.debug("Request to save CrbProductServiceFeeType : {}", crbProductServiceFeeTypeDTO);
        CrbProductServiceFeeType crbProductServiceFeeType = crbProductServiceFeeTypeMapper.toEntity(crbProductServiceFeeTypeDTO);
        crbProductServiceFeeType = crbProductServiceFeeTypeRepository.save(crbProductServiceFeeType);
        CrbProductServiceFeeTypeDTO result = crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeType);
        crbProductServiceFeeTypeSearchRepository.save(crbProductServiceFeeType);
        return result;
    }

    @Override
    public Optional<CrbProductServiceFeeTypeDTO> partialUpdate(CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO) {
        log.debug("Request to partially update CrbProductServiceFeeType : {}", crbProductServiceFeeTypeDTO);

        return crbProductServiceFeeTypeRepository
            .findById(crbProductServiceFeeTypeDTO.getId())
            .map(existingCrbProductServiceFeeType -> {
                crbProductServiceFeeTypeMapper.partialUpdate(existingCrbProductServiceFeeType, crbProductServiceFeeTypeDTO);

                return existingCrbProductServiceFeeType;
            })
            .map(crbProductServiceFeeTypeRepository::save)
            .map(savedCrbProductServiceFeeType -> {
                crbProductServiceFeeTypeSearchRepository.save(savedCrbProductServiceFeeType);

                return savedCrbProductServiceFeeType;
            })
            .map(crbProductServiceFeeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbProductServiceFeeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbProductServiceFeeTypes");
        return crbProductServiceFeeTypeRepository.findAll(pageable).map(crbProductServiceFeeTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbProductServiceFeeTypeDTO> findOne(Long id) {
        log.debug("Request to get CrbProductServiceFeeType : {}", id);
        return crbProductServiceFeeTypeRepository.findById(id).map(crbProductServiceFeeTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbProductServiceFeeType : {}", id);
        crbProductServiceFeeTypeRepository.deleteById(id);
        crbProductServiceFeeTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbProductServiceFeeTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbProductServiceFeeTypes for query {}", query);
        return crbProductServiceFeeTypeSearchRepository.search(query, pageable).map(crbProductServiceFeeTypeMapper::toDto);
    }
}
