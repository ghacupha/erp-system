package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
