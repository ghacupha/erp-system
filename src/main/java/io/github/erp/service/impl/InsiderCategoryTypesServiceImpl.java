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

import io.github.erp.domain.InsiderCategoryTypes;
import io.github.erp.repository.InsiderCategoryTypesRepository;
import io.github.erp.repository.search.InsiderCategoryTypesSearchRepository;
import io.github.erp.service.InsiderCategoryTypesService;
import io.github.erp.service.dto.InsiderCategoryTypesDTO;
import io.github.erp.service.mapper.InsiderCategoryTypesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InsiderCategoryTypes}.
 */
@Service
@Transactional
public class InsiderCategoryTypesServiceImpl implements InsiderCategoryTypesService {

    private final Logger log = LoggerFactory.getLogger(InsiderCategoryTypesServiceImpl.class);

    private final InsiderCategoryTypesRepository insiderCategoryTypesRepository;

    private final InsiderCategoryTypesMapper insiderCategoryTypesMapper;

    private final InsiderCategoryTypesSearchRepository insiderCategoryTypesSearchRepository;

    public InsiderCategoryTypesServiceImpl(
        InsiderCategoryTypesRepository insiderCategoryTypesRepository,
        InsiderCategoryTypesMapper insiderCategoryTypesMapper,
        InsiderCategoryTypesSearchRepository insiderCategoryTypesSearchRepository
    ) {
        this.insiderCategoryTypesRepository = insiderCategoryTypesRepository;
        this.insiderCategoryTypesMapper = insiderCategoryTypesMapper;
        this.insiderCategoryTypesSearchRepository = insiderCategoryTypesSearchRepository;
    }

    @Override
    public InsiderCategoryTypesDTO save(InsiderCategoryTypesDTO insiderCategoryTypesDTO) {
        log.debug("Request to save InsiderCategoryTypes : {}", insiderCategoryTypesDTO);
        InsiderCategoryTypes insiderCategoryTypes = insiderCategoryTypesMapper.toEntity(insiderCategoryTypesDTO);
        insiderCategoryTypes = insiderCategoryTypesRepository.save(insiderCategoryTypes);
        InsiderCategoryTypesDTO result = insiderCategoryTypesMapper.toDto(insiderCategoryTypes);
        insiderCategoryTypesSearchRepository.save(insiderCategoryTypes);
        return result;
    }

    @Override
    public Optional<InsiderCategoryTypesDTO> partialUpdate(InsiderCategoryTypesDTO insiderCategoryTypesDTO) {
        log.debug("Request to partially update InsiderCategoryTypes : {}", insiderCategoryTypesDTO);

        return insiderCategoryTypesRepository
            .findById(insiderCategoryTypesDTO.getId())
            .map(existingInsiderCategoryTypes -> {
                insiderCategoryTypesMapper.partialUpdate(existingInsiderCategoryTypes, insiderCategoryTypesDTO);

                return existingInsiderCategoryTypes;
            })
            .map(insiderCategoryTypesRepository::save)
            .map(savedInsiderCategoryTypes -> {
                insiderCategoryTypesSearchRepository.save(savedInsiderCategoryTypes);

                return savedInsiderCategoryTypes;
            })
            .map(insiderCategoryTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsiderCategoryTypesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InsiderCategoryTypes");
        return insiderCategoryTypesRepository.findAll(pageable).map(insiderCategoryTypesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsiderCategoryTypesDTO> findOne(Long id) {
        log.debug("Request to get InsiderCategoryTypes : {}", id);
        return insiderCategoryTypesRepository.findById(id).map(insiderCategoryTypesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsiderCategoryTypes : {}", id);
        insiderCategoryTypesRepository.deleteById(id);
        insiderCategoryTypesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsiderCategoryTypesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InsiderCategoryTypes for query {}", query);
        return insiderCategoryTypesSearchRepository.search(query, pageable).map(insiderCategoryTypesMapper::toDto);
    }
}
