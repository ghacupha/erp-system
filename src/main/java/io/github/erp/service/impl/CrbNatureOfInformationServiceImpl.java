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

import io.github.erp.domain.CrbNatureOfInformation;
import io.github.erp.repository.CrbNatureOfInformationRepository;
import io.github.erp.repository.search.CrbNatureOfInformationSearchRepository;
import io.github.erp.service.CrbNatureOfInformationService;
import io.github.erp.service.dto.CrbNatureOfInformationDTO;
import io.github.erp.service.mapper.CrbNatureOfInformationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbNatureOfInformation}.
 */
@Service
@Transactional
public class CrbNatureOfInformationServiceImpl implements CrbNatureOfInformationService {

    private final Logger log = LoggerFactory.getLogger(CrbNatureOfInformationServiceImpl.class);

    private final CrbNatureOfInformationRepository crbNatureOfInformationRepository;

    private final CrbNatureOfInformationMapper crbNatureOfInformationMapper;

    private final CrbNatureOfInformationSearchRepository crbNatureOfInformationSearchRepository;

    public CrbNatureOfInformationServiceImpl(
        CrbNatureOfInformationRepository crbNatureOfInformationRepository,
        CrbNatureOfInformationMapper crbNatureOfInformationMapper,
        CrbNatureOfInformationSearchRepository crbNatureOfInformationSearchRepository
    ) {
        this.crbNatureOfInformationRepository = crbNatureOfInformationRepository;
        this.crbNatureOfInformationMapper = crbNatureOfInformationMapper;
        this.crbNatureOfInformationSearchRepository = crbNatureOfInformationSearchRepository;
    }

    @Override
    public CrbNatureOfInformationDTO save(CrbNatureOfInformationDTO crbNatureOfInformationDTO) {
        log.debug("Request to save CrbNatureOfInformation : {}", crbNatureOfInformationDTO);
        CrbNatureOfInformation crbNatureOfInformation = crbNatureOfInformationMapper.toEntity(crbNatureOfInformationDTO);
        crbNatureOfInformation = crbNatureOfInformationRepository.save(crbNatureOfInformation);
        CrbNatureOfInformationDTO result = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);
        crbNatureOfInformationSearchRepository.save(crbNatureOfInformation);
        return result;
    }

    @Override
    public Optional<CrbNatureOfInformationDTO> partialUpdate(CrbNatureOfInformationDTO crbNatureOfInformationDTO) {
        log.debug("Request to partially update CrbNatureOfInformation : {}", crbNatureOfInformationDTO);

        return crbNatureOfInformationRepository
            .findById(crbNatureOfInformationDTO.getId())
            .map(existingCrbNatureOfInformation -> {
                crbNatureOfInformationMapper.partialUpdate(existingCrbNatureOfInformation, crbNatureOfInformationDTO);

                return existingCrbNatureOfInformation;
            })
            .map(crbNatureOfInformationRepository::save)
            .map(savedCrbNatureOfInformation -> {
                crbNatureOfInformationSearchRepository.save(savedCrbNatureOfInformation);

                return savedCrbNatureOfInformation;
            })
            .map(crbNatureOfInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbNatureOfInformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbNatureOfInformations");
        return crbNatureOfInformationRepository.findAll(pageable).map(crbNatureOfInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbNatureOfInformationDTO> findOne(Long id) {
        log.debug("Request to get CrbNatureOfInformation : {}", id);
        return crbNatureOfInformationRepository.findById(id).map(crbNatureOfInformationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbNatureOfInformation : {}", id);
        crbNatureOfInformationRepository.deleteById(id);
        crbNatureOfInformationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbNatureOfInformationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbNatureOfInformations for query {}", query);
        return crbNatureOfInformationSearchRepository.search(query, pageable).map(crbNatureOfInformationMapper::toDto);
    }
}
