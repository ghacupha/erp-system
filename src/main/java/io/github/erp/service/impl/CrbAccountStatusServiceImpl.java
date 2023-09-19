package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.domain.CrbAccountStatus;
import io.github.erp.repository.CrbAccountStatusRepository;
import io.github.erp.repository.search.CrbAccountStatusSearchRepository;
import io.github.erp.service.CrbAccountStatusService;
import io.github.erp.service.dto.CrbAccountStatusDTO;
import io.github.erp.service.mapper.CrbAccountStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbAccountStatus}.
 */
@Service
@Transactional
public class CrbAccountStatusServiceImpl implements CrbAccountStatusService {

    private final Logger log = LoggerFactory.getLogger(CrbAccountStatusServiceImpl.class);

    private final CrbAccountStatusRepository crbAccountStatusRepository;

    private final CrbAccountStatusMapper crbAccountStatusMapper;

    private final CrbAccountStatusSearchRepository crbAccountStatusSearchRepository;

    public CrbAccountStatusServiceImpl(
        CrbAccountStatusRepository crbAccountStatusRepository,
        CrbAccountStatusMapper crbAccountStatusMapper,
        CrbAccountStatusSearchRepository crbAccountStatusSearchRepository
    ) {
        this.crbAccountStatusRepository = crbAccountStatusRepository;
        this.crbAccountStatusMapper = crbAccountStatusMapper;
        this.crbAccountStatusSearchRepository = crbAccountStatusSearchRepository;
    }

    @Override
    public CrbAccountStatusDTO save(CrbAccountStatusDTO crbAccountStatusDTO) {
        log.debug("Request to save CrbAccountStatus : {}", crbAccountStatusDTO);
        CrbAccountStatus crbAccountStatus = crbAccountStatusMapper.toEntity(crbAccountStatusDTO);
        crbAccountStatus = crbAccountStatusRepository.save(crbAccountStatus);
        CrbAccountStatusDTO result = crbAccountStatusMapper.toDto(crbAccountStatus);
        crbAccountStatusSearchRepository.save(crbAccountStatus);
        return result;
    }

    @Override
    public Optional<CrbAccountStatusDTO> partialUpdate(CrbAccountStatusDTO crbAccountStatusDTO) {
        log.debug("Request to partially update CrbAccountStatus : {}", crbAccountStatusDTO);

        return crbAccountStatusRepository
            .findById(crbAccountStatusDTO.getId())
            .map(existingCrbAccountStatus -> {
                crbAccountStatusMapper.partialUpdate(existingCrbAccountStatus, crbAccountStatusDTO);

                return existingCrbAccountStatus;
            })
            .map(crbAccountStatusRepository::save)
            .map(savedCrbAccountStatus -> {
                crbAccountStatusSearchRepository.save(savedCrbAccountStatus);

                return savedCrbAccountStatus;
            })
            .map(crbAccountStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAccountStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbAccountStatuses");
        return crbAccountStatusRepository.findAll(pageable).map(crbAccountStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbAccountStatusDTO> findOne(Long id) {
        log.debug("Request to get CrbAccountStatus : {}", id);
        return crbAccountStatusRepository.findById(id).map(crbAccountStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbAccountStatus : {}", id);
        crbAccountStatusRepository.deleteById(id);
        crbAccountStatusSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAccountStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbAccountStatuses for query {}", query);
        return crbAccountStatusSearchRepository.search(query, pageable).map(crbAccountStatusMapper::toDto);
    }
}
