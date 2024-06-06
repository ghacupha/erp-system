package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.domain.SnaSectorCode;
import io.github.erp.repository.SnaSectorCodeRepository;
import io.github.erp.repository.search.SnaSectorCodeSearchRepository;
import io.github.erp.service.SnaSectorCodeService;
import io.github.erp.service.dto.SnaSectorCodeDTO;
import io.github.erp.service.mapper.SnaSectorCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SnaSectorCode}.
 */
@Service
@Transactional
public class SnaSectorCodeServiceImpl implements SnaSectorCodeService {

    private final Logger log = LoggerFactory.getLogger(SnaSectorCodeServiceImpl.class);

    private final SnaSectorCodeRepository snaSectorCodeRepository;

    private final SnaSectorCodeMapper snaSectorCodeMapper;

    private final SnaSectorCodeSearchRepository snaSectorCodeSearchRepository;

    public SnaSectorCodeServiceImpl(
        SnaSectorCodeRepository snaSectorCodeRepository,
        SnaSectorCodeMapper snaSectorCodeMapper,
        SnaSectorCodeSearchRepository snaSectorCodeSearchRepository
    ) {
        this.snaSectorCodeRepository = snaSectorCodeRepository;
        this.snaSectorCodeMapper = snaSectorCodeMapper;
        this.snaSectorCodeSearchRepository = snaSectorCodeSearchRepository;
    }

    @Override
    public SnaSectorCodeDTO save(SnaSectorCodeDTO snaSectorCodeDTO) {
        log.debug("Request to save SnaSectorCode : {}", snaSectorCodeDTO);
        SnaSectorCode snaSectorCode = snaSectorCodeMapper.toEntity(snaSectorCodeDTO);
        snaSectorCode = snaSectorCodeRepository.save(snaSectorCode);
        SnaSectorCodeDTO result = snaSectorCodeMapper.toDto(snaSectorCode);
        snaSectorCodeSearchRepository.save(snaSectorCode);
        return result;
    }

    @Override
    public Optional<SnaSectorCodeDTO> partialUpdate(SnaSectorCodeDTO snaSectorCodeDTO) {
        log.debug("Request to partially update SnaSectorCode : {}", snaSectorCodeDTO);

        return snaSectorCodeRepository
            .findById(snaSectorCodeDTO.getId())
            .map(existingSnaSectorCode -> {
                snaSectorCodeMapper.partialUpdate(existingSnaSectorCode, snaSectorCodeDTO);

                return existingSnaSectorCode;
            })
            .map(snaSectorCodeRepository::save)
            .map(savedSnaSectorCode -> {
                snaSectorCodeSearchRepository.save(savedSnaSectorCode);

                return savedSnaSectorCode;
            })
            .map(snaSectorCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SnaSectorCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SnaSectorCodes");
        return snaSectorCodeRepository.findAll(pageable).map(snaSectorCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SnaSectorCodeDTO> findOne(Long id) {
        log.debug("Request to get SnaSectorCode : {}", id);
        return snaSectorCodeRepository.findById(id).map(snaSectorCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SnaSectorCode : {}", id);
        snaSectorCodeRepository.deleteById(id);
        snaSectorCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SnaSectorCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SnaSectorCodes for query {}", query);
        return snaSectorCodeSearchRepository.search(query, pageable).map(snaSectorCodeMapper::toDto);
    }
}
