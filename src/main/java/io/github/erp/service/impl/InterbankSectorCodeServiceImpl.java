package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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

import io.github.erp.domain.InterbankSectorCode;
import io.github.erp.repository.InterbankSectorCodeRepository;
import io.github.erp.repository.search.InterbankSectorCodeSearchRepository;
import io.github.erp.service.InterbankSectorCodeService;
import io.github.erp.service.dto.InterbankSectorCodeDTO;
import io.github.erp.service.mapper.InterbankSectorCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InterbankSectorCode}.
 */
@Service
@Transactional
public class InterbankSectorCodeServiceImpl implements InterbankSectorCodeService {

    private final Logger log = LoggerFactory.getLogger(InterbankSectorCodeServiceImpl.class);

    private final InterbankSectorCodeRepository interbankSectorCodeRepository;

    private final InterbankSectorCodeMapper interbankSectorCodeMapper;

    private final InterbankSectorCodeSearchRepository interbankSectorCodeSearchRepository;

    public InterbankSectorCodeServiceImpl(
        InterbankSectorCodeRepository interbankSectorCodeRepository,
        InterbankSectorCodeMapper interbankSectorCodeMapper,
        InterbankSectorCodeSearchRepository interbankSectorCodeSearchRepository
    ) {
        this.interbankSectorCodeRepository = interbankSectorCodeRepository;
        this.interbankSectorCodeMapper = interbankSectorCodeMapper;
        this.interbankSectorCodeSearchRepository = interbankSectorCodeSearchRepository;
    }

    @Override
    public InterbankSectorCodeDTO save(InterbankSectorCodeDTO interbankSectorCodeDTO) {
        log.debug("Request to save InterbankSectorCode : {}", interbankSectorCodeDTO);
        InterbankSectorCode interbankSectorCode = interbankSectorCodeMapper.toEntity(interbankSectorCodeDTO);
        interbankSectorCode = interbankSectorCodeRepository.save(interbankSectorCode);
        InterbankSectorCodeDTO result = interbankSectorCodeMapper.toDto(interbankSectorCode);
        interbankSectorCodeSearchRepository.save(interbankSectorCode);
        return result;
    }

    @Override
    public Optional<InterbankSectorCodeDTO> partialUpdate(InterbankSectorCodeDTO interbankSectorCodeDTO) {
        log.debug("Request to partially update InterbankSectorCode : {}", interbankSectorCodeDTO);

        return interbankSectorCodeRepository
            .findById(interbankSectorCodeDTO.getId())
            .map(existingInterbankSectorCode -> {
                interbankSectorCodeMapper.partialUpdate(existingInterbankSectorCode, interbankSectorCodeDTO);

                return existingInterbankSectorCode;
            })
            .map(interbankSectorCodeRepository::save)
            .map(savedInterbankSectorCode -> {
                interbankSectorCodeSearchRepository.save(savedInterbankSectorCode);

                return savedInterbankSectorCode;
            })
            .map(interbankSectorCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterbankSectorCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InterbankSectorCodes");
        return interbankSectorCodeRepository.findAll(pageable).map(interbankSectorCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InterbankSectorCodeDTO> findOne(Long id) {
        log.debug("Request to get InterbankSectorCode : {}", id);
        return interbankSectorCodeRepository.findById(id).map(interbankSectorCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InterbankSectorCode : {}", id);
        interbankSectorCodeRepository.deleteById(id);
        interbankSectorCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterbankSectorCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InterbankSectorCodes for query {}", query);
        return interbankSectorCodeSearchRepository.search(query, pageable).map(interbankSectorCodeMapper::toDto);
    }
}
