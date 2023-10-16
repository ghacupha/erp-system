package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.domain.OutletType;
import io.github.erp.repository.OutletTypeRepository;
import io.github.erp.repository.search.OutletTypeSearchRepository;
import io.github.erp.service.OutletTypeService;
import io.github.erp.service.dto.OutletTypeDTO;
import io.github.erp.service.mapper.OutletTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OutletType}.
 */
@Service
@Transactional
public class OutletTypeServiceImpl implements OutletTypeService {

    private final Logger log = LoggerFactory.getLogger(OutletTypeServiceImpl.class);

    private final OutletTypeRepository outletTypeRepository;

    private final OutletTypeMapper outletTypeMapper;

    private final OutletTypeSearchRepository outletTypeSearchRepository;

    public OutletTypeServiceImpl(
        OutletTypeRepository outletTypeRepository,
        OutletTypeMapper outletTypeMapper,
        OutletTypeSearchRepository outletTypeSearchRepository
    ) {
        this.outletTypeRepository = outletTypeRepository;
        this.outletTypeMapper = outletTypeMapper;
        this.outletTypeSearchRepository = outletTypeSearchRepository;
    }

    @Override
    public OutletTypeDTO save(OutletTypeDTO outletTypeDTO) {
        log.debug("Request to save OutletType : {}", outletTypeDTO);
        OutletType outletType = outletTypeMapper.toEntity(outletTypeDTO);
        outletType = outletTypeRepository.save(outletType);
        OutletTypeDTO result = outletTypeMapper.toDto(outletType);
        outletTypeSearchRepository.save(outletType);
        return result;
    }

    @Override
    public Optional<OutletTypeDTO> partialUpdate(OutletTypeDTO outletTypeDTO) {
        log.debug("Request to partially update OutletType : {}", outletTypeDTO);

        return outletTypeRepository
            .findById(outletTypeDTO.getId())
            .map(existingOutletType -> {
                outletTypeMapper.partialUpdate(existingOutletType, outletTypeDTO);

                return existingOutletType;
            })
            .map(outletTypeRepository::save)
            .map(savedOutletType -> {
                outletTypeSearchRepository.save(savedOutletType);

                return savedOutletType;
            })
            .map(outletTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutletTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OutletTypes");
        return outletTypeRepository.findAll(pageable).map(outletTypeMapper::toDto);
    }

    public Page<OutletTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return outletTypeRepository.findAllWithEagerRelationships(pageable).map(outletTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutletTypeDTO> findOne(Long id) {
        log.debug("Request to get OutletType : {}", id);
        return outletTypeRepository.findOneWithEagerRelationships(id).map(outletTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OutletType : {}", id);
        outletTypeRepository.deleteById(id);
        outletTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutletTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OutletTypes for query {}", query);
        return outletTypeSearchRepository.search(query, pageable).map(outletTypeMapper::toDto);
    }
}
