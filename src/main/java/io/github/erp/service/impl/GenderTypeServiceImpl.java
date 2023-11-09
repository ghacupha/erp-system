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

import io.github.erp.domain.GenderType;
import io.github.erp.repository.GenderTypeRepository;
import io.github.erp.repository.search.GenderTypeSearchRepository;
import io.github.erp.service.GenderTypeService;
import io.github.erp.service.dto.GenderTypeDTO;
import io.github.erp.service.mapper.GenderTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GenderType}.
 */
@Service
@Transactional
public class GenderTypeServiceImpl implements GenderTypeService {

    private final Logger log = LoggerFactory.getLogger(GenderTypeServiceImpl.class);

    private final GenderTypeRepository genderTypeRepository;

    private final GenderTypeMapper genderTypeMapper;

    private final GenderTypeSearchRepository genderTypeSearchRepository;

    public GenderTypeServiceImpl(
        GenderTypeRepository genderTypeRepository,
        GenderTypeMapper genderTypeMapper,
        GenderTypeSearchRepository genderTypeSearchRepository
    ) {
        this.genderTypeRepository = genderTypeRepository;
        this.genderTypeMapper = genderTypeMapper;
        this.genderTypeSearchRepository = genderTypeSearchRepository;
    }

    @Override
    public GenderTypeDTO save(GenderTypeDTO genderTypeDTO) {
        log.debug("Request to save GenderType : {}", genderTypeDTO);
        GenderType genderType = genderTypeMapper.toEntity(genderTypeDTO);
        genderType = genderTypeRepository.save(genderType);
        GenderTypeDTO result = genderTypeMapper.toDto(genderType);
        genderTypeSearchRepository.save(genderType);
        return result;
    }

    @Override
    public Optional<GenderTypeDTO> partialUpdate(GenderTypeDTO genderTypeDTO) {
        log.debug("Request to partially update GenderType : {}", genderTypeDTO);

        return genderTypeRepository
            .findById(genderTypeDTO.getId())
            .map(existingGenderType -> {
                genderTypeMapper.partialUpdate(existingGenderType, genderTypeDTO);

                return existingGenderType;
            })
            .map(genderTypeRepository::save)
            .map(savedGenderType -> {
                genderTypeSearchRepository.save(savedGenderType);

                return savedGenderType;
            })
            .map(genderTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenderTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GenderTypes");
        return genderTypeRepository.findAll(pageable).map(genderTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GenderTypeDTO> findOne(Long id) {
        log.debug("Request to get GenderType : {}", id);
        return genderTypeRepository.findById(id).map(genderTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GenderType : {}", id);
        genderTypeRepository.deleteById(id);
        genderTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenderTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GenderTypes for query {}", query);
        return genderTypeSearchRepository.search(query, pageable).map(genderTypeMapper::toDto);
    }
}
