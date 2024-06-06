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

import io.github.erp.domain.RemittanceFlag;
import io.github.erp.repository.RemittanceFlagRepository;
import io.github.erp.repository.search.RemittanceFlagSearchRepository;
import io.github.erp.service.RemittanceFlagService;
import io.github.erp.service.dto.RemittanceFlagDTO;
import io.github.erp.service.mapper.RemittanceFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RemittanceFlag}.
 */
@Service
@Transactional
public class RemittanceFlagServiceImpl implements RemittanceFlagService {

    private final Logger log = LoggerFactory.getLogger(RemittanceFlagServiceImpl.class);

    private final RemittanceFlagRepository remittanceFlagRepository;

    private final RemittanceFlagMapper remittanceFlagMapper;

    private final RemittanceFlagSearchRepository remittanceFlagSearchRepository;

    public RemittanceFlagServiceImpl(
        RemittanceFlagRepository remittanceFlagRepository,
        RemittanceFlagMapper remittanceFlagMapper,
        RemittanceFlagSearchRepository remittanceFlagSearchRepository
    ) {
        this.remittanceFlagRepository = remittanceFlagRepository;
        this.remittanceFlagMapper = remittanceFlagMapper;
        this.remittanceFlagSearchRepository = remittanceFlagSearchRepository;
    }

    @Override
    public RemittanceFlagDTO save(RemittanceFlagDTO remittanceFlagDTO) {
        log.debug("Request to save RemittanceFlag : {}", remittanceFlagDTO);
        RemittanceFlag remittanceFlag = remittanceFlagMapper.toEntity(remittanceFlagDTO);
        remittanceFlag = remittanceFlagRepository.save(remittanceFlag);
        RemittanceFlagDTO result = remittanceFlagMapper.toDto(remittanceFlag);
        remittanceFlagSearchRepository.save(remittanceFlag);
        return result;
    }

    @Override
    public Optional<RemittanceFlagDTO> partialUpdate(RemittanceFlagDTO remittanceFlagDTO) {
        log.debug("Request to partially update RemittanceFlag : {}", remittanceFlagDTO);

        return remittanceFlagRepository
            .findById(remittanceFlagDTO.getId())
            .map(existingRemittanceFlag -> {
                remittanceFlagMapper.partialUpdate(existingRemittanceFlag, remittanceFlagDTO);

                return existingRemittanceFlag;
            })
            .map(remittanceFlagRepository::save)
            .map(savedRemittanceFlag -> {
                remittanceFlagSearchRepository.save(savedRemittanceFlag);

                return savedRemittanceFlag;
            })
            .map(remittanceFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RemittanceFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RemittanceFlags");
        return remittanceFlagRepository.findAll(pageable).map(remittanceFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RemittanceFlagDTO> findOne(Long id) {
        log.debug("Request to get RemittanceFlag : {}", id);
        return remittanceFlagRepository.findById(id).map(remittanceFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RemittanceFlag : {}", id);
        remittanceFlagRepository.deleteById(id);
        remittanceFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RemittanceFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RemittanceFlags for query {}", query);
        return remittanceFlagSearchRepository.search(query, pageable).map(remittanceFlagMapper::toDto);
    }
}
