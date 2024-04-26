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

import io.github.erp.domain.CrbReportViewBand;
import io.github.erp.repository.CrbReportViewBandRepository;
import io.github.erp.repository.search.CrbReportViewBandSearchRepository;
import io.github.erp.service.CrbReportViewBandService;
import io.github.erp.service.dto.CrbReportViewBandDTO;
import io.github.erp.service.mapper.CrbReportViewBandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbReportViewBand}.
 */
@Service
@Transactional
public class CrbReportViewBandServiceImpl implements CrbReportViewBandService {

    private final Logger log = LoggerFactory.getLogger(CrbReportViewBandServiceImpl.class);

    private final CrbReportViewBandRepository crbReportViewBandRepository;

    private final CrbReportViewBandMapper crbReportViewBandMapper;

    private final CrbReportViewBandSearchRepository crbReportViewBandSearchRepository;

    public CrbReportViewBandServiceImpl(
        CrbReportViewBandRepository crbReportViewBandRepository,
        CrbReportViewBandMapper crbReportViewBandMapper,
        CrbReportViewBandSearchRepository crbReportViewBandSearchRepository
    ) {
        this.crbReportViewBandRepository = crbReportViewBandRepository;
        this.crbReportViewBandMapper = crbReportViewBandMapper;
        this.crbReportViewBandSearchRepository = crbReportViewBandSearchRepository;
    }

    @Override
    public CrbReportViewBandDTO save(CrbReportViewBandDTO crbReportViewBandDTO) {
        log.debug("Request to save CrbReportViewBand : {}", crbReportViewBandDTO);
        CrbReportViewBand crbReportViewBand = crbReportViewBandMapper.toEntity(crbReportViewBandDTO);
        crbReportViewBand = crbReportViewBandRepository.save(crbReportViewBand);
        CrbReportViewBandDTO result = crbReportViewBandMapper.toDto(crbReportViewBand);
        crbReportViewBandSearchRepository.save(crbReportViewBand);
        return result;
    }

    @Override
    public Optional<CrbReportViewBandDTO> partialUpdate(CrbReportViewBandDTO crbReportViewBandDTO) {
        log.debug("Request to partially update CrbReportViewBand : {}", crbReportViewBandDTO);

        return crbReportViewBandRepository
            .findById(crbReportViewBandDTO.getId())
            .map(existingCrbReportViewBand -> {
                crbReportViewBandMapper.partialUpdate(existingCrbReportViewBand, crbReportViewBandDTO);

                return existingCrbReportViewBand;
            })
            .map(crbReportViewBandRepository::save)
            .map(savedCrbReportViewBand -> {
                crbReportViewBandSearchRepository.save(savedCrbReportViewBand);

                return savedCrbReportViewBand;
            })
            .map(crbReportViewBandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbReportViewBandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbReportViewBands");
        return crbReportViewBandRepository.findAll(pageable).map(crbReportViewBandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbReportViewBandDTO> findOne(Long id) {
        log.debug("Request to get CrbReportViewBand : {}", id);
        return crbReportViewBandRepository.findById(id).map(crbReportViewBandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbReportViewBand : {}", id);
        crbReportViewBandRepository.deleteById(id);
        crbReportViewBandSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbReportViewBandDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbReportViewBands for query {}", query);
        return crbReportViewBandSearchRepository.search(query, pageable).map(crbReportViewBandMapper::toDto);
    }
}
