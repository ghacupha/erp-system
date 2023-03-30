package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
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

import io.github.erp.domain.OutletStatus;
import io.github.erp.repository.OutletStatusRepository;
import io.github.erp.repository.search.OutletStatusSearchRepository;
import io.github.erp.service.OutletStatusService;
import io.github.erp.service.dto.OutletStatusDTO;
import io.github.erp.service.mapper.OutletStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OutletStatus}.
 */
@Service
@Transactional
public class OutletStatusServiceImpl implements OutletStatusService {

    private final Logger log = LoggerFactory.getLogger(OutletStatusServiceImpl.class);

    private final OutletStatusRepository outletStatusRepository;

    private final OutletStatusMapper outletStatusMapper;

    private final OutletStatusSearchRepository outletStatusSearchRepository;

    public OutletStatusServiceImpl(
        OutletStatusRepository outletStatusRepository,
        OutletStatusMapper outletStatusMapper,
        OutletStatusSearchRepository outletStatusSearchRepository
    ) {
        this.outletStatusRepository = outletStatusRepository;
        this.outletStatusMapper = outletStatusMapper;
        this.outletStatusSearchRepository = outletStatusSearchRepository;
    }

    @Override
    public OutletStatusDTO save(OutletStatusDTO outletStatusDTO) {
        log.debug("Request to save OutletStatus : {}", outletStatusDTO);
        OutletStatus outletStatus = outletStatusMapper.toEntity(outletStatusDTO);
        outletStatus = outletStatusRepository.save(outletStatus);
        OutletStatusDTO result = outletStatusMapper.toDto(outletStatus);
        outletStatusSearchRepository.save(outletStatus);
        return result;
    }

    @Override
    public Optional<OutletStatusDTO> partialUpdate(OutletStatusDTO outletStatusDTO) {
        log.debug("Request to partially update OutletStatus : {}", outletStatusDTO);

        return outletStatusRepository
            .findById(outletStatusDTO.getId())
            .map(existingOutletStatus -> {
                outletStatusMapper.partialUpdate(existingOutletStatus, outletStatusDTO);

                return existingOutletStatus;
            })
            .map(outletStatusRepository::save)
            .map(savedOutletStatus -> {
                outletStatusSearchRepository.save(savedOutletStatus);

                return savedOutletStatus;
            })
            .map(outletStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutletStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OutletStatuses");
        return outletStatusRepository.findAll(pageable).map(outletStatusMapper::toDto);
    }

    public Page<OutletStatusDTO> findAllWithEagerRelationships(Pageable pageable) {
        return outletStatusRepository.findAllWithEagerRelationships(pageable).map(outletStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OutletStatusDTO> findOne(Long id) {
        log.debug("Request to get OutletStatus : {}", id);
        return outletStatusRepository.findOneWithEagerRelationships(id).map(outletStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OutletStatus : {}", id);
        outletStatusRepository.deleteById(id);
        outletStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OutletStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OutletStatuses for query {}", query);
        return outletStatusSearchRepository.search(query, pageable).map(outletStatusMapper::toDto);
    }
}
