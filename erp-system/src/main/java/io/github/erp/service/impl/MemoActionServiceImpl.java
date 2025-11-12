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

import io.github.erp.domain.MemoAction;
import io.github.erp.repository.MemoActionRepository;
import io.github.erp.repository.search.MemoActionSearchRepository;
import io.github.erp.service.MemoActionService;
import io.github.erp.service.dto.MemoActionDTO;
import io.github.erp.service.mapper.MemoActionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MemoAction}.
 */
@Service
@Transactional
public class MemoActionServiceImpl implements MemoActionService {

    private final Logger log = LoggerFactory.getLogger(MemoActionServiceImpl.class);

    private final MemoActionRepository memoActionRepository;

    private final MemoActionMapper memoActionMapper;

    private final MemoActionSearchRepository memoActionSearchRepository;

    public MemoActionServiceImpl(
        MemoActionRepository memoActionRepository,
        MemoActionMapper memoActionMapper,
        MemoActionSearchRepository memoActionSearchRepository
    ) {
        this.memoActionRepository = memoActionRepository;
        this.memoActionMapper = memoActionMapper;
        this.memoActionSearchRepository = memoActionSearchRepository;
    }

    @Override
    public MemoActionDTO save(MemoActionDTO memoActionDTO) {
        log.debug("Request to save MemoAction : {}", memoActionDTO);
        MemoAction memoAction = memoActionMapper.toEntity(memoActionDTO);
        memoAction = memoActionRepository.save(memoAction);
        MemoActionDTO result = memoActionMapper.toDto(memoAction);
        memoActionSearchRepository.save(memoAction);
        return result;
    }

    @Override
    public Optional<MemoActionDTO> partialUpdate(MemoActionDTO memoActionDTO) {
        log.debug("Request to partially update MemoAction : {}", memoActionDTO);

        return memoActionRepository
            .findById(memoActionDTO.getId())
            .map(existingMemoAction -> {
                memoActionMapper.partialUpdate(existingMemoAction, memoActionDTO);

                return existingMemoAction;
            })
            .map(memoActionRepository::save)
            .map(savedMemoAction -> {
                memoActionSearchRepository.save(savedMemoAction);

                return savedMemoAction;
            })
            .map(memoActionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemoActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MemoActions");
        return memoActionRepository.findAll(pageable).map(memoActionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemoActionDTO> findOne(Long id) {
        log.debug("Request to get MemoAction : {}", id);
        return memoActionRepository.findById(id).map(memoActionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MemoAction : {}", id);
        memoActionRepository.deleteById(id);
        memoActionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemoActionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MemoActions for query {}", query);
        return memoActionSearchRepository.search(query, pageable).map(memoActionMapper::toDto);
    }
}
