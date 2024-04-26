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

import io.github.erp.domain.EmploymentTerms;
import io.github.erp.repository.EmploymentTermsRepository;
import io.github.erp.repository.search.EmploymentTermsSearchRepository;
import io.github.erp.service.EmploymentTermsService;
import io.github.erp.service.dto.EmploymentTermsDTO;
import io.github.erp.service.mapper.EmploymentTermsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmploymentTerms}.
 */
@Service
@Transactional
public class EmploymentTermsServiceImpl implements EmploymentTermsService {

    private final Logger log = LoggerFactory.getLogger(EmploymentTermsServiceImpl.class);

    private final EmploymentTermsRepository employmentTermsRepository;

    private final EmploymentTermsMapper employmentTermsMapper;

    private final EmploymentTermsSearchRepository employmentTermsSearchRepository;

    public EmploymentTermsServiceImpl(
        EmploymentTermsRepository employmentTermsRepository,
        EmploymentTermsMapper employmentTermsMapper,
        EmploymentTermsSearchRepository employmentTermsSearchRepository
    ) {
        this.employmentTermsRepository = employmentTermsRepository;
        this.employmentTermsMapper = employmentTermsMapper;
        this.employmentTermsSearchRepository = employmentTermsSearchRepository;
    }

    @Override
    public EmploymentTermsDTO save(EmploymentTermsDTO employmentTermsDTO) {
        log.debug("Request to save EmploymentTerms : {}", employmentTermsDTO);
        EmploymentTerms employmentTerms = employmentTermsMapper.toEntity(employmentTermsDTO);
        employmentTerms = employmentTermsRepository.save(employmentTerms);
        EmploymentTermsDTO result = employmentTermsMapper.toDto(employmentTerms);
        employmentTermsSearchRepository.save(employmentTerms);
        return result;
    }

    @Override
    public Optional<EmploymentTermsDTO> partialUpdate(EmploymentTermsDTO employmentTermsDTO) {
        log.debug("Request to partially update EmploymentTerms : {}", employmentTermsDTO);

        return employmentTermsRepository
            .findById(employmentTermsDTO.getId())
            .map(existingEmploymentTerms -> {
                employmentTermsMapper.partialUpdate(existingEmploymentTerms, employmentTermsDTO);

                return existingEmploymentTerms;
            })
            .map(employmentTermsRepository::save)
            .map(savedEmploymentTerms -> {
                employmentTermsSearchRepository.save(savedEmploymentTerms);

                return savedEmploymentTerms;
            })
            .map(employmentTermsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentTermsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmploymentTerms");
        return employmentTermsRepository.findAll(pageable).map(employmentTermsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmploymentTermsDTO> findOne(Long id) {
        log.debug("Request to get EmploymentTerms : {}", id);
        return employmentTermsRepository.findById(id).map(employmentTermsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmploymentTerms : {}", id);
        employmentTermsRepository.deleteById(id);
        employmentTermsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentTermsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EmploymentTerms for query {}", query);
        return employmentTermsSearchRepository.search(query, pageable).map(employmentTermsMapper::toDto);
    }
}
