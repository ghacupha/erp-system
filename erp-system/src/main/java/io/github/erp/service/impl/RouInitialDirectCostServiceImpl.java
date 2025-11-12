package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.RouInitialDirectCost;
import io.github.erp.repository.RouInitialDirectCostRepository;
import io.github.erp.repository.search.RouInitialDirectCostSearchRepository;
import io.github.erp.service.RouInitialDirectCostService;
import io.github.erp.service.dto.RouInitialDirectCostDTO;
import io.github.erp.service.mapper.RouInitialDirectCostMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RouInitialDirectCost}.
 */
@Service
@Transactional
public class RouInitialDirectCostServiceImpl implements RouInitialDirectCostService {

    private final Logger log = LoggerFactory.getLogger(RouInitialDirectCostServiceImpl.class);

    private final RouInitialDirectCostRepository rouInitialDirectCostRepository;

    private final RouInitialDirectCostMapper rouInitialDirectCostMapper;

    private final RouInitialDirectCostSearchRepository rouInitialDirectCostSearchRepository;

    public RouInitialDirectCostServiceImpl(
        RouInitialDirectCostRepository rouInitialDirectCostRepository,
        RouInitialDirectCostMapper rouInitialDirectCostMapper,
        RouInitialDirectCostSearchRepository rouInitialDirectCostSearchRepository
    ) {
        this.rouInitialDirectCostRepository = rouInitialDirectCostRepository;
        this.rouInitialDirectCostMapper = rouInitialDirectCostMapper;
        this.rouInitialDirectCostSearchRepository = rouInitialDirectCostSearchRepository;
    }

    @Override
    public RouInitialDirectCostDTO save(RouInitialDirectCostDTO rouInitialDirectCostDTO) {
        log.debug("Request to save RouInitialDirectCost : {}", rouInitialDirectCostDTO);
        RouInitialDirectCost rouInitialDirectCost = rouInitialDirectCostMapper.toEntity(rouInitialDirectCostDTO);
        rouInitialDirectCost = rouInitialDirectCostRepository.save(rouInitialDirectCost);
        RouInitialDirectCostDTO result = rouInitialDirectCostMapper.toDto(rouInitialDirectCost);
        rouInitialDirectCostSearchRepository.save(rouInitialDirectCost);
        return result;
    }

    @Override
    public Optional<RouInitialDirectCostDTO> partialUpdate(RouInitialDirectCostDTO rouInitialDirectCostDTO) {
        log.debug("Request to partially update RouInitialDirectCost : {}", rouInitialDirectCostDTO);

        return rouInitialDirectCostRepository
            .findById(rouInitialDirectCostDTO.getId())
            .map(existingRouInitialDirectCost -> {
                rouInitialDirectCostMapper.partialUpdate(existingRouInitialDirectCost, rouInitialDirectCostDTO);

                return existingRouInitialDirectCost;
            })
            .map(rouInitialDirectCostRepository::save)
            .map(savedRouInitialDirectCost -> {
                rouInitialDirectCostSearchRepository.save(savedRouInitialDirectCost);

                return savedRouInitialDirectCost;
            })
            .map(rouInitialDirectCostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouInitialDirectCostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RouInitialDirectCosts");
        return rouInitialDirectCostRepository.findAll(pageable).map(rouInitialDirectCostMapper::toDto);
    }

    public Page<RouInitialDirectCostDTO> findAllWithEagerRelationships(Pageable pageable) {
        return rouInitialDirectCostRepository.findAllWithEagerRelationships(pageable).map(rouInitialDirectCostMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RouInitialDirectCostDTO> findOne(Long id) {
        log.debug("Request to get RouInitialDirectCost : {}", id);
        return rouInitialDirectCostRepository.findOneWithEagerRelationships(id).map(rouInitialDirectCostMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RouInitialDirectCost : {}", id);
        rouInitialDirectCostRepository.deleteById(id);
        rouInitialDirectCostSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RouInitialDirectCostDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RouInitialDirectCosts for query {}", query);
        return rouInitialDirectCostSearchRepository.search(query, pageable).map(rouInitialDirectCostMapper::toDto);
    }
}
