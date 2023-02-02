package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 9 (Caleb Series) Server ver 0.5.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.MfbBranchCode;
import io.github.erp.repository.MfbBranchCodeRepository;
import io.github.erp.repository.search.MfbBranchCodeSearchRepository;
import io.github.erp.service.MfbBranchCodeService;
import io.github.erp.service.dto.MfbBranchCodeDTO;
import io.github.erp.service.mapper.MfbBranchCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MfbBranchCode}.
 */
@Service
@Transactional
public class MfbBranchCodeServiceImpl implements MfbBranchCodeService {

    private final Logger log = LoggerFactory.getLogger(MfbBranchCodeServiceImpl.class);

    private final MfbBranchCodeRepository mfbBranchCodeRepository;

    private final MfbBranchCodeMapper mfbBranchCodeMapper;

    private final MfbBranchCodeSearchRepository mfbBranchCodeSearchRepository;

    public MfbBranchCodeServiceImpl(
        MfbBranchCodeRepository mfbBranchCodeRepository,
        MfbBranchCodeMapper mfbBranchCodeMapper,
        MfbBranchCodeSearchRepository mfbBranchCodeSearchRepository
    ) {
        this.mfbBranchCodeRepository = mfbBranchCodeRepository;
        this.mfbBranchCodeMapper = mfbBranchCodeMapper;
        this.mfbBranchCodeSearchRepository = mfbBranchCodeSearchRepository;
    }

    @Override
    public MfbBranchCodeDTO save(MfbBranchCodeDTO mfbBranchCodeDTO) {
        log.debug("Request to save MfbBranchCode : {}", mfbBranchCodeDTO);
        MfbBranchCode mfbBranchCode = mfbBranchCodeMapper.toEntity(mfbBranchCodeDTO);
        mfbBranchCode = mfbBranchCodeRepository.save(mfbBranchCode);
        MfbBranchCodeDTO result = mfbBranchCodeMapper.toDto(mfbBranchCode);
        mfbBranchCodeSearchRepository.save(mfbBranchCode);
        return result;
    }

    @Override
    public Optional<MfbBranchCodeDTO> partialUpdate(MfbBranchCodeDTO mfbBranchCodeDTO) {
        log.debug("Request to partially update MfbBranchCode : {}", mfbBranchCodeDTO);

        return mfbBranchCodeRepository
            .findById(mfbBranchCodeDTO.getId())
            .map(existingMfbBranchCode -> {
                mfbBranchCodeMapper.partialUpdate(existingMfbBranchCode, mfbBranchCodeDTO);

                return existingMfbBranchCode;
            })
            .map(mfbBranchCodeRepository::save)
            .map(savedMfbBranchCode -> {
                mfbBranchCodeSearchRepository.save(savedMfbBranchCode);

                return savedMfbBranchCode;
            })
            .map(mfbBranchCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MfbBranchCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MfbBranchCodes");
        return mfbBranchCodeRepository.findAll(pageable).map(mfbBranchCodeMapper::toDto);
    }

    public Page<MfbBranchCodeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mfbBranchCodeRepository.findAllWithEagerRelationships(pageable).map(mfbBranchCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MfbBranchCodeDTO> findOne(Long id) {
        log.debug("Request to get MfbBranchCode : {}", id);
        return mfbBranchCodeRepository.findOneWithEagerRelationships(id).map(mfbBranchCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MfbBranchCode : {}", id);
        mfbBranchCodeRepository.deleteById(id);
        mfbBranchCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MfbBranchCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MfbBranchCodes for query {}", query);
        return mfbBranchCodeSearchRepository.search(query, pageable).map(mfbBranchCodeMapper::toDto);
    }
}
