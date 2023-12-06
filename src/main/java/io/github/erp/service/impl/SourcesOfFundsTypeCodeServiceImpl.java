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

import io.github.erp.domain.SourcesOfFundsTypeCode;
import io.github.erp.repository.SourcesOfFundsTypeCodeRepository;
import io.github.erp.repository.search.SourcesOfFundsTypeCodeSearchRepository;
import io.github.erp.service.SourcesOfFundsTypeCodeService;
import io.github.erp.service.dto.SourcesOfFundsTypeCodeDTO;
import io.github.erp.service.mapper.SourcesOfFundsTypeCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SourcesOfFundsTypeCode}.
 */
@Service
@Transactional
public class SourcesOfFundsTypeCodeServiceImpl implements SourcesOfFundsTypeCodeService {

    private final Logger log = LoggerFactory.getLogger(SourcesOfFundsTypeCodeServiceImpl.class);

    private final SourcesOfFundsTypeCodeRepository sourcesOfFundsTypeCodeRepository;

    private final SourcesOfFundsTypeCodeMapper sourcesOfFundsTypeCodeMapper;

    private final SourcesOfFundsTypeCodeSearchRepository sourcesOfFundsTypeCodeSearchRepository;

    public SourcesOfFundsTypeCodeServiceImpl(
        SourcesOfFundsTypeCodeRepository sourcesOfFundsTypeCodeRepository,
        SourcesOfFundsTypeCodeMapper sourcesOfFundsTypeCodeMapper,
        SourcesOfFundsTypeCodeSearchRepository sourcesOfFundsTypeCodeSearchRepository
    ) {
        this.sourcesOfFundsTypeCodeRepository = sourcesOfFundsTypeCodeRepository;
        this.sourcesOfFundsTypeCodeMapper = sourcesOfFundsTypeCodeMapper;
        this.sourcesOfFundsTypeCodeSearchRepository = sourcesOfFundsTypeCodeSearchRepository;
    }

    @Override
    public SourcesOfFundsTypeCodeDTO save(SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO) {
        log.debug("Request to save SourcesOfFundsTypeCode : {}", sourcesOfFundsTypeCodeDTO);
        SourcesOfFundsTypeCode sourcesOfFundsTypeCode = sourcesOfFundsTypeCodeMapper.toEntity(sourcesOfFundsTypeCodeDTO);
        sourcesOfFundsTypeCode = sourcesOfFundsTypeCodeRepository.save(sourcesOfFundsTypeCode);
        SourcesOfFundsTypeCodeDTO result = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);
        sourcesOfFundsTypeCodeSearchRepository.save(sourcesOfFundsTypeCode);
        return result;
    }

    @Override
    public Optional<SourcesOfFundsTypeCodeDTO> partialUpdate(SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO) {
        log.debug("Request to partially update SourcesOfFundsTypeCode : {}", sourcesOfFundsTypeCodeDTO);

        return sourcesOfFundsTypeCodeRepository
            .findById(sourcesOfFundsTypeCodeDTO.getId())
            .map(existingSourcesOfFundsTypeCode -> {
                sourcesOfFundsTypeCodeMapper.partialUpdate(existingSourcesOfFundsTypeCode, sourcesOfFundsTypeCodeDTO);

                return existingSourcesOfFundsTypeCode;
            })
            .map(sourcesOfFundsTypeCodeRepository::save)
            .map(savedSourcesOfFundsTypeCode -> {
                sourcesOfFundsTypeCodeSearchRepository.save(savedSourcesOfFundsTypeCode);

                return savedSourcesOfFundsTypeCode;
            })
            .map(sourcesOfFundsTypeCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SourcesOfFundsTypeCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SourcesOfFundsTypeCodes");
        return sourcesOfFundsTypeCodeRepository.findAll(pageable).map(sourcesOfFundsTypeCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SourcesOfFundsTypeCodeDTO> findOne(Long id) {
        log.debug("Request to get SourcesOfFundsTypeCode : {}", id);
        return sourcesOfFundsTypeCodeRepository.findById(id).map(sourcesOfFundsTypeCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SourcesOfFundsTypeCode : {}", id);
        sourcesOfFundsTypeCodeRepository.deleteById(id);
        sourcesOfFundsTypeCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SourcesOfFundsTypeCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SourcesOfFundsTypeCodes for query {}", query);
        return sourcesOfFundsTypeCodeSearchRepository.search(query, pageable).map(sourcesOfFundsTypeCodeMapper::toDto);
    }
}
