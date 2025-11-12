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

import io.github.erp.domain.LeasePeriod;
import io.github.erp.repository.LeasePeriodRepository;
import io.github.erp.repository.search.LeasePeriodSearchRepository;
import io.github.erp.service.LeasePeriodService;
import io.github.erp.service.dto.LeasePeriodDTO;
import io.github.erp.service.mapper.LeasePeriodMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeasePeriod}.
 */
@Service
@Transactional
public class LeasePeriodServiceImpl implements LeasePeriodService {

    private final Logger log = LoggerFactory.getLogger(LeasePeriodServiceImpl.class);

    private final LeasePeriodRepository leasePeriodRepository;

    private final LeasePeriodMapper leasePeriodMapper;

    private final LeasePeriodSearchRepository leasePeriodSearchRepository;

    public LeasePeriodServiceImpl(
        LeasePeriodRepository leasePeriodRepository,
        LeasePeriodMapper leasePeriodMapper,
        LeasePeriodSearchRepository leasePeriodSearchRepository
    ) {
        this.leasePeriodRepository = leasePeriodRepository;
        this.leasePeriodMapper = leasePeriodMapper;
        this.leasePeriodSearchRepository = leasePeriodSearchRepository;
    }

    @Override
    public LeasePeriodDTO save(LeasePeriodDTO leasePeriodDTO) {
        log.debug("Request to save LeasePeriod : {}", leasePeriodDTO);
        LeasePeriod leasePeriod = leasePeriodMapper.toEntity(leasePeriodDTO);
        leasePeriod = leasePeriodRepository.save(leasePeriod);
        LeasePeriodDTO result = leasePeriodMapper.toDto(leasePeriod);
        leasePeriodSearchRepository.save(leasePeriod);
        return result;
    }

    @Override
    public Optional<LeasePeriodDTO> partialUpdate(LeasePeriodDTO leasePeriodDTO) {
        log.debug("Request to partially update LeasePeriod : {}", leasePeriodDTO);

        return leasePeriodRepository
            .findById(leasePeriodDTO.getId())
            .map(existingLeasePeriod -> {
                leasePeriodMapper.partialUpdate(existingLeasePeriod, leasePeriodDTO);

                return existingLeasePeriod;
            })
            .map(leasePeriodRepository::save)
            .map(savedLeasePeriod -> {
                leasePeriodSearchRepository.save(savedLeasePeriod);

                return savedLeasePeriod;
            })
            .map(leasePeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasePeriodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeasePeriods");
        return leasePeriodRepository.findAll(pageable).map(leasePeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeasePeriodDTO> findOne(Long id) {
        log.debug("Request to get LeasePeriod : {}", id);
        return leasePeriodRepository.findById(id).map(leasePeriodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeasePeriod : {}", id);
        leasePeriodRepository.deleteById(id);
        leasePeriodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasePeriodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeasePeriods for query {}", query);
        return leasePeriodSearchRepository.search(query, pageable).map(leasePeriodMapper::toDto);
    }
}
