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

import io.github.erp.domain.AnticipatedMaturityPeriood;
import io.github.erp.repository.AnticipatedMaturityPerioodRepository;
import io.github.erp.repository.search.AnticipatedMaturityPerioodSearchRepository;
import io.github.erp.service.AnticipatedMaturityPerioodService;
import io.github.erp.service.dto.AnticipatedMaturityPerioodDTO;
import io.github.erp.service.mapper.AnticipatedMaturityPerioodMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AnticipatedMaturityPeriood}.
 */
@Service
@Transactional
public class AnticipatedMaturityPerioodServiceImpl implements AnticipatedMaturityPerioodService {

    private final Logger log = LoggerFactory.getLogger(AnticipatedMaturityPerioodServiceImpl.class);

    private final AnticipatedMaturityPerioodRepository anticipatedMaturityPerioodRepository;

    private final AnticipatedMaturityPerioodMapper anticipatedMaturityPerioodMapper;

    private final AnticipatedMaturityPerioodSearchRepository anticipatedMaturityPerioodSearchRepository;

    public AnticipatedMaturityPerioodServiceImpl(
        AnticipatedMaturityPerioodRepository anticipatedMaturityPerioodRepository,
        AnticipatedMaturityPerioodMapper anticipatedMaturityPerioodMapper,
        AnticipatedMaturityPerioodSearchRepository anticipatedMaturityPerioodSearchRepository
    ) {
        this.anticipatedMaturityPerioodRepository = anticipatedMaturityPerioodRepository;
        this.anticipatedMaturityPerioodMapper = anticipatedMaturityPerioodMapper;
        this.anticipatedMaturityPerioodSearchRepository = anticipatedMaturityPerioodSearchRepository;
    }

    @Override
    public AnticipatedMaturityPerioodDTO save(AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO) {
        log.debug("Request to save AnticipatedMaturityPeriood : {}", anticipatedMaturityPerioodDTO);
        AnticipatedMaturityPeriood anticipatedMaturityPeriood = anticipatedMaturityPerioodMapper.toEntity(anticipatedMaturityPerioodDTO);
        anticipatedMaturityPeriood = anticipatedMaturityPerioodRepository.save(anticipatedMaturityPeriood);
        AnticipatedMaturityPerioodDTO result = anticipatedMaturityPerioodMapper.toDto(anticipatedMaturityPeriood);
        anticipatedMaturityPerioodSearchRepository.save(anticipatedMaturityPeriood);
        return result;
    }

    @Override
    public Optional<AnticipatedMaturityPerioodDTO> partialUpdate(AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO) {
        log.debug("Request to partially update AnticipatedMaturityPeriood : {}", anticipatedMaturityPerioodDTO);

        return anticipatedMaturityPerioodRepository
            .findById(anticipatedMaturityPerioodDTO.getId())
            .map(existingAnticipatedMaturityPeriood -> {
                anticipatedMaturityPerioodMapper.partialUpdate(existingAnticipatedMaturityPeriood, anticipatedMaturityPerioodDTO);

                return existingAnticipatedMaturityPeriood;
            })
            .map(anticipatedMaturityPerioodRepository::save)
            .map(savedAnticipatedMaturityPeriood -> {
                anticipatedMaturityPerioodSearchRepository.save(savedAnticipatedMaturityPeriood);

                return savedAnticipatedMaturityPeriood;
            })
            .map(anticipatedMaturityPerioodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnticipatedMaturityPerioodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnticipatedMaturityPerioods");
        return anticipatedMaturityPerioodRepository.findAll(pageable).map(anticipatedMaturityPerioodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnticipatedMaturityPerioodDTO> findOne(Long id) {
        log.debug("Request to get AnticipatedMaturityPeriood : {}", id);
        return anticipatedMaturityPerioodRepository.findById(id).map(anticipatedMaturityPerioodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnticipatedMaturityPeriood : {}", id);
        anticipatedMaturityPerioodRepository.deleteById(id);
        anticipatedMaturityPerioodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnticipatedMaturityPerioodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnticipatedMaturityPerioods for query {}", query);
        return anticipatedMaturityPerioodSearchRepository.search(query, pageable).map(anticipatedMaturityPerioodMapper::toDto);
    }
}
