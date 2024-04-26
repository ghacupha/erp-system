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

import io.github.erp.domain.CrbReportRequestReasons;
import io.github.erp.repository.CrbReportRequestReasonsRepository;
import io.github.erp.repository.search.CrbReportRequestReasonsSearchRepository;
import io.github.erp.service.CrbReportRequestReasonsService;
import io.github.erp.service.dto.CrbReportRequestReasonsDTO;
import io.github.erp.service.mapper.CrbReportRequestReasonsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbReportRequestReasons}.
 */
@Service
@Transactional
public class CrbReportRequestReasonsServiceImpl implements CrbReportRequestReasonsService {

    private final Logger log = LoggerFactory.getLogger(CrbReportRequestReasonsServiceImpl.class);

    private final CrbReportRequestReasonsRepository crbReportRequestReasonsRepository;

    private final CrbReportRequestReasonsMapper crbReportRequestReasonsMapper;

    private final CrbReportRequestReasonsSearchRepository crbReportRequestReasonsSearchRepository;

    public CrbReportRequestReasonsServiceImpl(
        CrbReportRequestReasonsRepository crbReportRequestReasonsRepository,
        CrbReportRequestReasonsMapper crbReportRequestReasonsMapper,
        CrbReportRequestReasonsSearchRepository crbReportRequestReasonsSearchRepository
    ) {
        this.crbReportRequestReasonsRepository = crbReportRequestReasonsRepository;
        this.crbReportRequestReasonsMapper = crbReportRequestReasonsMapper;
        this.crbReportRequestReasonsSearchRepository = crbReportRequestReasonsSearchRepository;
    }

    @Override
    public CrbReportRequestReasonsDTO save(CrbReportRequestReasonsDTO crbReportRequestReasonsDTO) {
        log.debug("Request to save CrbReportRequestReasons : {}", crbReportRequestReasonsDTO);
        CrbReportRequestReasons crbReportRequestReasons = crbReportRequestReasonsMapper.toEntity(crbReportRequestReasonsDTO);
        crbReportRequestReasons = crbReportRequestReasonsRepository.save(crbReportRequestReasons);
        CrbReportRequestReasonsDTO result = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);
        crbReportRequestReasonsSearchRepository.save(crbReportRequestReasons);
        return result;
    }

    @Override
    public Optional<CrbReportRequestReasonsDTO> partialUpdate(CrbReportRequestReasonsDTO crbReportRequestReasonsDTO) {
        log.debug("Request to partially update CrbReportRequestReasons : {}", crbReportRequestReasonsDTO);

        return crbReportRequestReasonsRepository
            .findById(crbReportRequestReasonsDTO.getId())
            .map(existingCrbReportRequestReasons -> {
                crbReportRequestReasonsMapper.partialUpdate(existingCrbReportRequestReasons, crbReportRequestReasonsDTO);

                return existingCrbReportRequestReasons;
            })
            .map(crbReportRequestReasonsRepository::save)
            .map(savedCrbReportRequestReasons -> {
                crbReportRequestReasonsSearchRepository.save(savedCrbReportRequestReasons);

                return savedCrbReportRequestReasons;
            })
            .map(crbReportRequestReasonsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbReportRequestReasonsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbReportRequestReasons");
        return crbReportRequestReasonsRepository.findAll(pageable).map(crbReportRequestReasonsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbReportRequestReasonsDTO> findOne(Long id) {
        log.debug("Request to get CrbReportRequestReasons : {}", id);
        return crbReportRequestReasonsRepository.findById(id).map(crbReportRequestReasonsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbReportRequestReasons : {}", id);
        crbReportRequestReasonsRepository.deleteById(id);
        crbReportRequestReasonsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbReportRequestReasonsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbReportRequestReasons for query {}", query);
        return crbReportRequestReasonsSearchRepository.search(query, pageable).map(crbReportRequestReasonsMapper::toDto);
    }
}
