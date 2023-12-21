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

import io.github.erp.domain.IsicEconomicActivity;
import io.github.erp.repository.IsicEconomicActivityRepository;
import io.github.erp.repository.search.IsicEconomicActivitySearchRepository;
import io.github.erp.service.IsicEconomicActivityService;
import io.github.erp.service.dto.IsicEconomicActivityDTO;
import io.github.erp.service.mapper.IsicEconomicActivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IsicEconomicActivity}.
 */
@Service
@Transactional
public class IsicEconomicActivityServiceImpl implements IsicEconomicActivityService {

    private final Logger log = LoggerFactory.getLogger(IsicEconomicActivityServiceImpl.class);

    private final IsicEconomicActivityRepository isicEconomicActivityRepository;

    private final IsicEconomicActivityMapper isicEconomicActivityMapper;

    private final IsicEconomicActivitySearchRepository isicEconomicActivitySearchRepository;

    public IsicEconomicActivityServiceImpl(
        IsicEconomicActivityRepository isicEconomicActivityRepository,
        IsicEconomicActivityMapper isicEconomicActivityMapper,
        IsicEconomicActivitySearchRepository isicEconomicActivitySearchRepository
    ) {
        this.isicEconomicActivityRepository = isicEconomicActivityRepository;
        this.isicEconomicActivityMapper = isicEconomicActivityMapper;
        this.isicEconomicActivitySearchRepository = isicEconomicActivitySearchRepository;
    }

    @Override
    public IsicEconomicActivityDTO save(IsicEconomicActivityDTO isicEconomicActivityDTO) {
        log.debug("Request to save IsicEconomicActivity : {}", isicEconomicActivityDTO);
        IsicEconomicActivity isicEconomicActivity = isicEconomicActivityMapper.toEntity(isicEconomicActivityDTO);
        isicEconomicActivity = isicEconomicActivityRepository.save(isicEconomicActivity);
        IsicEconomicActivityDTO result = isicEconomicActivityMapper.toDto(isicEconomicActivity);
        isicEconomicActivitySearchRepository.save(isicEconomicActivity);
        return result;
    }

    @Override
    public Optional<IsicEconomicActivityDTO> partialUpdate(IsicEconomicActivityDTO isicEconomicActivityDTO) {
        log.debug("Request to partially update IsicEconomicActivity : {}", isicEconomicActivityDTO);

        return isicEconomicActivityRepository
            .findById(isicEconomicActivityDTO.getId())
            .map(existingIsicEconomicActivity -> {
                isicEconomicActivityMapper.partialUpdate(existingIsicEconomicActivity, isicEconomicActivityDTO);

                return existingIsicEconomicActivity;
            })
            .map(isicEconomicActivityRepository::save)
            .map(savedIsicEconomicActivity -> {
                isicEconomicActivitySearchRepository.save(savedIsicEconomicActivity);

                return savedIsicEconomicActivity;
            })
            .map(isicEconomicActivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsicEconomicActivityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IsicEconomicActivities");
        return isicEconomicActivityRepository.findAll(pageable).map(isicEconomicActivityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IsicEconomicActivityDTO> findOne(Long id) {
        log.debug("Request to get IsicEconomicActivity : {}", id);
        return isicEconomicActivityRepository.findById(id).map(isicEconomicActivityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IsicEconomicActivity : {}", id);
        isicEconomicActivityRepository.deleteById(id);
        isicEconomicActivitySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IsicEconomicActivityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IsicEconomicActivities for query {}", query);
        return isicEconomicActivitySearchRepository.search(query, pageable).map(isicEconomicActivityMapper::toDto);
    }
}
