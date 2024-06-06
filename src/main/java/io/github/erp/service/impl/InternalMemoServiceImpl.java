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

import io.github.erp.domain.InternalMemo;
import io.github.erp.repository.InternalMemoRepository;
import io.github.erp.repository.search.InternalMemoSearchRepository;
import io.github.erp.service.InternalMemoService;
import io.github.erp.service.dto.InternalMemoDTO;
import io.github.erp.service.mapper.InternalMemoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InternalMemo}.
 */
@Service
@Transactional
public class InternalMemoServiceImpl implements InternalMemoService {

    private final Logger log = LoggerFactory.getLogger(InternalMemoServiceImpl.class);

    private final InternalMemoRepository internalMemoRepository;

    private final InternalMemoMapper internalMemoMapper;

    private final InternalMemoSearchRepository internalMemoSearchRepository;

    public InternalMemoServiceImpl(
        InternalMemoRepository internalMemoRepository,
        InternalMemoMapper internalMemoMapper,
        InternalMemoSearchRepository internalMemoSearchRepository
    ) {
        this.internalMemoRepository = internalMemoRepository;
        this.internalMemoMapper = internalMemoMapper;
        this.internalMemoSearchRepository = internalMemoSearchRepository;
    }

    @Override
    public InternalMemoDTO save(InternalMemoDTO internalMemoDTO) {
        log.debug("Request to save InternalMemo : {}", internalMemoDTO);
        InternalMemo internalMemo = internalMemoMapper.toEntity(internalMemoDTO);
        internalMemo = internalMemoRepository.save(internalMemo);
        InternalMemoDTO result = internalMemoMapper.toDto(internalMemo);
        internalMemoSearchRepository.save(internalMemo);
        return result;
    }

    @Override
    public Optional<InternalMemoDTO> partialUpdate(InternalMemoDTO internalMemoDTO) {
        log.debug("Request to partially update InternalMemo : {}", internalMemoDTO);

        return internalMemoRepository
            .findById(internalMemoDTO.getId())
            .map(existingInternalMemo -> {
                internalMemoMapper.partialUpdate(existingInternalMemo, internalMemoDTO);

                return existingInternalMemo;
            })
            .map(internalMemoRepository::save)
            .map(savedInternalMemo -> {
                internalMemoSearchRepository.save(savedInternalMemo);

                return savedInternalMemo;
            })
            .map(internalMemoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InternalMemoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InternalMemos");
        return internalMemoRepository.findAll(pageable).map(internalMemoMapper::toDto);
    }

    public Page<InternalMemoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return internalMemoRepository.findAllWithEagerRelationships(pageable).map(internalMemoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InternalMemoDTO> findOne(Long id) {
        log.debug("Request to get InternalMemo : {}", id);
        return internalMemoRepository.findOneWithEagerRelationships(id).map(internalMemoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InternalMemo : {}", id);
        internalMemoRepository.deleteById(id);
        internalMemoSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InternalMemoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InternalMemos for query {}", query);
        return internalMemoSearchRepository.search(query, pageable).map(internalMemoMapper::toDto);
    }
}
