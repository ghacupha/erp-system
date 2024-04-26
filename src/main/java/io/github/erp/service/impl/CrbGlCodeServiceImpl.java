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

import io.github.erp.domain.CrbGlCode;
import io.github.erp.repository.CrbGlCodeRepository;
import io.github.erp.repository.search.CrbGlCodeSearchRepository;
import io.github.erp.service.CrbGlCodeService;
import io.github.erp.service.dto.CrbGlCodeDTO;
import io.github.erp.service.mapper.CrbGlCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbGlCode}.
 */
@Service
@Transactional
public class CrbGlCodeServiceImpl implements CrbGlCodeService {

    private final Logger log = LoggerFactory.getLogger(CrbGlCodeServiceImpl.class);

    private final CrbGlCodeRepository crbGlCodeRepository;

    private final CrbGlCodeMapper crbGlCodeMapper;

    private final CrbGlCodeSearchRepository crbGlCodeSearchRepository;

    public CrbGlCodeServiceImpl(
        CrbGlCodeRepository crbGlCodeRepository,
        CrbGlCodeMapper crbGlCodeMapper,
        CrbGlCodeSearchRepository crbGlCodeSearchRepository
    ) {
        this.crbGlCodeRepository = crbGlCodeRepository;
        this.crbGlCodeMapper = crbGlCodeMapper;
        this.crbGlCodeSearchRepository = crbGlCodeSearchRepository;
    }

    @Override
    public CrbGlCodeDTO save(CrbGlCodeDTO crbGlCodeDTO) {
        log.debug("Request to save CrbGlCode : {}", crbGlCodeDTO);
        CrbGlCode crbGlCode = crbGlCodeMapper.toEntity(crbGlCodeDTO);
        crbGlCode = crbGlCodeRepository.save(crbGlCode);
        CrbGlCodeDTO result = crbGlCodeMapper.toDto(crbGlCode);
        crbGlCodeSearchRepository.save(crbGlCode);
        return result;
    }

    @Override
    public Optional<CrbGlCodeDTO> partialUpdate(CrbGlCodeDTO crbGlCodeDTO) {
        log.debug("Request to partially update CrbGlCode : {}", crbGlCodeDTO);

        return crbGlCodeRepository
            .findById(crbGlCodeDTO.getId())
            .map(existingCrbGlCode -> {
                crbGlCodeMapper.partialUpdate(existingCrbGlCode, crbGlCodeDTO);

                return existingCrbGlCode;
            })
            .map(crbGlCodeRepository::save)
            .map(savedCrbGlCode -> {
                crbGlCodeSearchRepository.save(savedCrbGlCode);

                return savedCrbGlCode;
            })
            .map(crbGlCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbGlCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbGlCodes");
        return crbGlCodeRepository.findAll(pageable).map(crbGlCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbGlCodeDTO> findOne(Long id) {
        log.debug("Request to get CrbGlCode : {}", id);
        return crbGlCodeRepository.findById(id).map(crbGlCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbGlCode : {}", id);
        crbGlCodeRepository.deleteById(id);
        crbGlCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbGlCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbGlCodes for query {}", query);
        return crbGlCodeSearchRepository.search(query, pageable).map(crbGlCodeMapper::toDto);
    }
}
