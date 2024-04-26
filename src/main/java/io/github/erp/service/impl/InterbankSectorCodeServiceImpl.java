package io.github.erp.service.impl;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
