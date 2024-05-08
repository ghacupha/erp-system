package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.ParticularsOfOutlet;
import io.github.erp.repository.ParticularsOfOutletRepository;
import io.github.erp.repository.search.ParticularsOfOutletSearchRepository;
import io.github.erp.service.ParticularsOfOutletService;
import io.github.erp.service.dto.ParticularsOfOutletDTO;
import io.github.erp.service.mapper.ParticularsOfOutletMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParticularsOfOutlet}.
 */
@Service
@Transactional
public class ParticularsOfOutletServiceImpl implements ParticularsOfOutletService {

    private final Logger log = LoggerFactory.getLogger(ParticularsOfOutletServiceImpl.class);

    private final ParticularsOfOutletRepository particularsOfOutletRepository;

    private final ParticularsOfOutletMapper particularsOfOutletMapper;

    private final ParticularsOfOutletSearchRepository particularsOfOutletSearchRepository;

    public ParticularsOfOutletServiceImpl(
        ParticularsOfOutletRepository particularsOfOutletRepository,
        ParticularsOfOutletMapper particularsOfOutletMapper,
        ParticularsOfOutletSearchRepository particularsOfOutletSearchRepository
    ) {
        this.particularsOfOutletRepository = particularsOfOutletRepository;
        this.particularsOfOutletMapper = particularsOfOutletMapper;
        this.particularsOfOutletSearchRepository = particularsOfOutletSearchRepository;
    }

    @Override
    public ParticularsOfOutletDTO save(ParticularsOfOutletDTO particularsOfOutletDTO) {
        log.debug("Request to save ParticularsOfOutlet : {}", particularsOfOutletDTO);
        ParticularsOfOutlet particularsOfOutlet = particularsOfOutletMapper.toEntity(particularsOfOutletDTO);
        particularsOfOutlet = particularsOfOutletRepository.save(particularsOfOutlet);
        ParticularsOfOutletDTO result = particularsOfOutletMapper.toDto(particularsOfOutlet);
        particularsOfOutletSearchRepository.save(particularsOfOutlet);
        return result;
    }

    @Override
    public Optional<ParticularsOfOutletDTO> partialUpdate(ParticularsOfOutletDTO particularsOfOutletDTO) {
        log.debug("Request to partially update ParticularsOfOutlet : {}", particularsOfOutletDTO);

        return particularsOfOutletRepository
            .findById(particularsOfOutletDTO.getId())
            .map(existingParticularsOfOutlet -> {
                particularsOfOutletMapper.partialUpdate(existingParticularsOfOutlet, particularsOfOutletDTO);

                return existingParticularsOfOutlet;
            })
            .map(particularsOfOutletRepository::save)
            .map(savedParticularsOfOutlet -> {
                particularsOfOutletSearchRepository.save(savedParticularsOfOutlet);

                return savedParticularsOfOutlet;
            })
            .map(particularsOfOutletMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParticularsOfOutletDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ParticularsOfOutlets");
        return particularsOfOutletRepository.findAll(pageable).map(particularsOfOutletMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParticularsOfOutletDTO> findOne(Long id) {
        log.debug("Request to get ParticularsOfOutlet : {}", id);
        return particularsOfOutletRepository.findById(id).map(particularsOfOutletMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ParticularsOfOutlet : {}", id);
        particularsOfOutletRepository.deleteById(id);
        particularsOfOutletSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParticularsOfOutletDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ParticularsOfOutlets for query {}", query);
        return particularsOfOutletSearchRepository.search(query, pageable).map(particularsOfOutletMapper::toDto);
    }
}
