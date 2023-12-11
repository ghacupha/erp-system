package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

import io.github.erp.domain.AcquiringIssuingFlag;
import io.github.erp.repository.AcquiringIssuingFlagRepository;
import io.github.erp.repository.search.AcquiringIssuingFlagSearchRepository;
import io.github.erp.service.AcquiringIssuingFlagService;
import io.github.erp.service.dto.AcquiringIssuingFlagDTO;
import io.github.erp.service.mapper.AcquiringIssuingFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AcquiringIssuingFlag}.
 */
@Service
@Transactional
public class AcquiringIssuingFlagServiceImpl implements AcquiringIssuingFlagService {

    private final Logger log = LoggerFactory.getLogger(AcquiringIssuingFlagServiceImpl.class);

    private final AcquiringIssuingFlagRepository acquiringIssuingFlagRepository;

    private final AcquiringIssuingFlagMapper acquiringIssuingFlagMapper;

    private final AcquiringIssuingFlagSearchRepository acquiringIssuingFlagSearchRepository;

    public AcquiringIssuingFlagServiceImpl(
        AcquiringIssuingFlagRepository acquiringIssuingFlagRepository,
        AcquiringIssuingFlagMapper acquiringIssuingFlagMapper,
        AcquiringIssuingFlagSearchRepository acquiringIssuingFlagSearchRepository
    ) {
        this.acquiringIssuingFlagRepository = acquiringIssuingFlagRepository;
        this.acquiringIssuingFlagMapper = acquiringIssuingFlagMapper;
        this.acquiringIssuingFlagSearchRepository = acquiringIssuingFlagSearchRepository;
    }

    @Override
    public AcquiringIssuingFlagDTO save(AcquiringIssuingFlagDTO acquiringIssuingFlagDTO) {
        log.debug("Request to save AcquiringIssuingFlag : {}", acquiringIssuingFlagDTO);
        AcquiringIssuingFlag acquiringIssuingFlag = acquiringIssuingFlagMapper.toEntity(acquiringIssuingFlagDTO);
        acquiringIssuingFlag = acquiringIssuingFlagRepository.save(acquiringIssuingFlag);
        AcquiringIssuingFlagDTO result = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);
        acquiringIssuingFlagSearchRepository.save(acquiringIssuingFlag);
        return result;
    }

    @Override
    public Optional<AcquiringIssuingFlagDTO> partialUpdate(AcquiringIssuingFlagDTO acquiringIssuingFlagDTO) {
        log.debug("Request to partially update AcquiringIssuingFlag : {}", acquiringIssuingFlagDTO);

        return acquiringIssuingFlagRepository
            .findById(acquiringIssuingFlagDTO.getId())
            .map(existingAcquiringIssuingFlag -> {
                acquiringIssuingFlagMapper.partialUpdate(existingAcquiringIssuingFlag, acquiringIssuingFlagDTO);

                return existingAcquiringIssuingFlag;
            })
            .map(acquiringIssuingFlagRepository::save)
            .map(savedAcquiringIssuingFlag -> {
                acquiringIssuingFlagSearchRepository.save(savedAcquiringIssuingFlag);

                return savedAcquiringIssuingFlag;
            })
            .map(acquiringIssuingFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcquiringIssuingFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AcquiringIssuingFlags");
        return acquiringIssuingFlagRepository.findAll(pageable).map(acquiringIssuingFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcquiringIssuingFlagDTO> findOne(Long id) {
        log.debug("Request to get AcquiringIssuingFlag : {}", id);
        return acquiringIssuingFlagRepository.findById(id).map(acquiringIssuingFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AcquiringIssuingFlag : {}", id);
        acquiringIssuingFlagRepository.deleteById(id);
        acquiringIssuingFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcquiringIssuingFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AcquiringIssuingFlags for query {}", query);
        return acquiringIssuingFlagSearchRepository.search(query, pageable).map(acquiringIssuingFlagMapper::toDto);
    }
}
