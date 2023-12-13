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

import io.github.erp.domain.CollateralType;
import io.github.erp.repository.CollateralTypeRepository;
import io.github.erp.repository.search.CollateralTypeSearchRepository;
import io.github.erp.service.CollateralTypeService;
import io.github.erp.service.dto.CollateralTypeDTO;
import io.github.erp.service.mapper.CollateralTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CollateralType}.
 */
@Service
@Transactional
public class CollateralTypeServiceImpl implements CollateralTypeService {

    private final Logger log = LoggerFactory.getLogger(CollateralTypeServiceImpl.class);

    private final CollateralTypeRepository collateralTypeRepository;

    private final CollateralTypeMapper collateralTypeMapper;

    private final CollateralTypeSearchRepository collateralTypeSearchRepository;

    public CollateralTypeServiceImpl(
        CollateralTypeRepository collateralTypeRepository,
        CollateralTypeMapper collateralTypeMapper,
        CollateralTypeSearchRepository collateralTypeSearchRepository
    ) {
        this.collateralTypeRepository = collateralTypeRepository;
        this.collateralTypeMapper = collateralTypeMapper;
        this.collateralTypeSearchRepository = collateralTypeSearchRepository;
    }

    @Override
    public CollateralTypeDTO save(CollateralTypeDTO collateralTypeDTO) {
        log.debug("Request to save CollateralType : {}", collateralTypeDTO);
        CollateralType collateralType = collateralTypeMapper.toEntity(collateralTypeDTO);
        collateralType = collateralTypeRepository.save(collateralType);
        CollateralTypeDTO result = collateralTypeMapper.toDto(collateralType);
        collateralTypeSearchRepository.save(collateralType);
        return result;
    }

    @Override
    public Optional<CollateralTypeDTO> partialUpdate(CollateralTypeDTO collateralTypeDTO) {
        log.debug("Request to partially update CollateralType : {}", collateralTypeDTO);

        return collateralTypeRepository
            .findById(collateralTypeDTO.getId())
            .map(existingCollateralType -> {
                collateralTypeMapper.partialUpdate(existingCollateralType, collateralTypeDTO);

                return existingCollateralType;
            })
            .map(collateralTypeRepository::save)
            .map(savedCollateralType -> {
                collateralTypeSearchRepository.save(savedCollateralType);

                return savedCollateralType;
            })
            .map(collateralTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CollateralTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CollateralTypes");
        return collateralTypeRepository.findAll(pageable).map(collateralTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollateralTypeDTO> findOne(Long id) {
        log.debug("Request to get CollateralType : {}", id);
        return collateralTypeRepository.findById(id).map(collateralTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CollateralType : {}", id);
        collateralTypeRepository.deleteById(id);
        collateralTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CollateralTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CollateralTypes for query {}", query);
        return collateralTypeSearchRepository.search(query, pageable).map(collateralTypeMapper::toDto);
    }
}
