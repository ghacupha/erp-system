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

import io.github.erp.domain.InstitutionContactDetails;
import io.github.erp.repository.InstitutionContactDetailsRepository;
import io.github.erp.repository.search.InstitutionContactDetailsSearchRepository;
import io.github.erp.service.InstitutionContactDetailsService;
import io.github.erp.service.dto.InstitutionContactDetailsDTO;
import io.github.erp.service.mapper.InstitutionContactDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InstitutionContactDetails}.
 */
@Service
@Transactional
public class InstitutionContactDetailsServiceImpl implements InstitutionContactDetailsService {

    private final Logger log = LoggerFactory.getLogger(InstitutionContactDetailsServiceImpl.class);

    private final InstitutionContactDetailsRepository institutionContactDetailsRepository;

    private final InstitutionContactDetailsMapper institutionContactDetailsMapper;

    private final InstitutionContactDetailsSearchRepository institutionContactDetailsSearchRepository;

    public InstitutionContactDetailsServiceImpl(
        InstitutionContactDetailsRepository institutionContactDetailsRepository,
        InstitutionContactDetailsMapper institutionContactDetailsMapper,
        InstitutionContactDetailsSearchRepository institutionContactDetailsSearchRepository
    ) {
        this.institutionContactDetailsRepository = institutionContactDetailsRepository;
        this.institutionContactDetailsMapper = institutionContactDetailsMapper;
        this.institutionContactDetailsSearchRepository = institutionContactDetailsSearchRepository;
    }

    @Override
    public InstitutionContactDetailsDTO save(InstitutionContactDetailsDTO institutionContactDetailsDTO) {
        log.debug("Request to save InstitutionContactDetails : {}", institutionContactDetailsDTO);
        InstitutionContactDetails institutionContactDetails = institutionContactDetailsMapper.toEntity(institutionContactDetailsDTO);
        institutionContactDetails = institutionContactDetailsRepository.save(institutionContactDetails);
        InstitutionContactDetailsDTO result = institutionContactDetailsMapper.toDto(institutionContactDetails);
        institutionContactDetailsSearchRepository.save(institutionContactDetails);
        return result;
    }

    @Override
    public Optional<InstitutionContactDetailsDTO> partialUpdate(InstitutionContactDetailsDTO institutionContactDetailsDTO) {
        log.debug("Request to partially update InstitutionContactDetails : {}", institutionContactDetailsDTO);

        return institutionContactDetailsRepository
            .findById(institutionContactDetailsDTO.getId())
            .map(existingInstitutionContactDetails -> {
                institutionContactDetailsMapper.partialUpdate(existingInstitutionContactDetails, institutionContactDetailsDTO);

                return existingInstitutionContactDetails;
            })
            .map(institutionContactDetailsRepository::save)
            .map(savedInstitutionContactDetails -> {
                institutionContactDetailsSearchRepository.save(savedInstitutionContactDetails);

                return savedInstitutionContactDetails;
            })
            .map(institutionContactDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionContactDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InstitutionContactDetails");
        return institutionContactDetailsRepository.findAll(pageable).map(institutionContactDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstitutionContactDetailsDTO> findOne(Long id) {
        log.debug("Request to get InstitutionContactDetails : {}", id);
        return institutionContactDetailsRepository.findById(id).map(institutionContactDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InstitutionContactDetails : {}", id);
        institutionContactDetailsRepository.deleteById(id);
        institutionContactDetailsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstitutionContactDetailsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InstitutionContactDetails for query {}", query);
        return institutionContactDetailsSearchRepository.search(query, pageable).map(institutionContactDetailsMapper::toDto);
    }
}
