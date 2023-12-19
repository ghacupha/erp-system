package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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

import io.github.erp.domain.CrbSubscriptionStatusTypeCode;
import io.github.erp.repository.CrbSubscriptionStatusTypeCodeRepository;
import io.github.erp.repository.search.CrbSubscriptionStatusTypeCodeSearchRepository;
import io.github.erp.service.CrbSubscriptionStatusTypeCodeService;
import io.github.erp.service.dto.CrbSubscriptionStatusTypeCodeDTO;
import io.github.erp.service.mapper.CrbSubscriptionStatusTypeCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbSubscriptionStatusTypeCode}.
 */
@Service
@Transactional
public class CrbSubscriptionStatusTypeCodeServiceImpl implements CrbSubscriptionStatusTypeCodeService {

    private final Logger log = LoggerFactory.getLogger(CrbSubscriptionStatusTypeCodeServiceImpl.class);

    private final CrbSubscriptionStatusTypeCodeRepository crbSubscriptionStatusTypeCodeRepository;

    private final CrbSubscriptionStatusTypeCodeMapper crbSubscriptionStatusTypeCodeMapper;

    private final CrbSubscriptionStatusTypeCodeSearchRepository crbSubscriptionStatusTypeCodeSearchRepository;

    public CrbSubscriptionStatusTypeCodeServiceImpl(
        CrbSubscriptionStatusTypeCodeRepository crbSubscriptionStatusTypeCodeRepository,
        CrbSubscriptionStatusTypeCodeMapper crbSubscriptionStatusTypeCodeMapper,
        CrbSubscriptionStatusTypeCodeSearchRepository crbSubscriptionStatusTypeCodeSearchRepository
    ) {
        this.crbSubscriptionStatusTypeCodeRepository = crbSubscriptionStatusTypeCodeRepository;
        this.crbSubscriptionStatusTypeCodeMapper = crbSubscriptionStatusTypeCodeMapper;
        this.crbSubscriptionStatusTypeCodeSearchRepository = crbSubscriptionStatusTypeCodeSearchRepository;
    }

    @Override
    public CrbSubscriptionStatusTypeCodeDTO save(CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO) {
        log.debug("Request to save CrbSubscriptionStatusTypeCode : {}", crbSubscriptionStatusTypeCodeDTO);
        CrbSubscriptionStatusTypeCode crbSubscriptionStatusTypeCode = crbSubscriptionStatusTypeCodeMapper.toEntity(
            crbSubscriptionStatusTypeCodeDTO
        );
        crbSubscriptionStatusTypeCode = crbSubscriptionStatusTypeCodeRepository.save(crbSubscriptionStatusTypeCode);
        CrbSubscriptionStatusTypeCodeDTO result = crbSubscriptionStatusTypeCodeMapper.toDto(crbSubscriptionStatusTypeCode);
        crbSubscriptionStatusTypeCodeSearchRepository.save(crbSubscriptionStatusTypeCode);
        return result;
    }

    @Override
    public Optional<CrbSubscriptionStatusTypeCodeDTO> partialUpdate(CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO) {
        log.debug("Request to partially update CrbSubscriptionStatusTypeCode : {}", crbSubscriptionStatusTypeCodeDTO);

        return crbSubscriptionStatusTypeCodeRepository
            .findById(crbSubscriptionStatusTypeCodeDTO.getId())
            .map(existingCrbSubscriptionStatusTypeCode -> {
                crbSubscriptionStatusTypeCodeMapper.partialUpdate(existingCrbSubscriptionStatusTypeCode, crbSubscriptionStatusTypeCodeDTO);

                return existingCrbSubscriptionStatusTypeCode;
            })
            .map(crbSubscriptionStatusTypeCodeRepository::save)
            .map(savedCrbSubscriptionStatusTypeCode -> {
                crbSubscriptionStatusTypeCodeSearchRepository.save(savedCrbSubscriptionStatusTypeCode);

                return savedCrbSubscriptionStatusTypeCode;
            })
            .map(crbSubscriptionStatusTypeCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbSubscriptionStatusTypeCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbSubscriptionStatusTypeCodes");
        return crbSubscriptionStatusTypeCodeRepository.findAll(pageable).map(crbSubscriptionStatusTypeCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbSubscriptionStatusTypeCodeDTO> findOne(Long id) {
        log.debug("Request to get CrbSubscriptionStatusTypeCode : {}", id);
        return crbSubscriptionStatusTypeCodeRepository.findById(id).map(crbSubscriptionStatusTypeCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbSubscriptionStatusTypeCode : {}", id);
        crbSubscriptionStatusTypeCodeRepository.deleteById(id);
        crbSubscriptionStatusTypeCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbSubscriptionStatusTypeCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbSubscriptionStatusTypeCodes for query {}", query);
        return crbSubscriptionStatusTypeCodeSearchRepository.search(query, pageable).map(crbSubscriptionStatusTypeCodeMapper::toDto);
    }
}
