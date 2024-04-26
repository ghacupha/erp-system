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

import io.github.erp.domain.SecurityTenure;
import io.github.erp.repository.SecurityTenureRepository;
import io.github.erp.repository.search.SecurityTenureSearchRepository;
import io.github.erp.service.SecurityTenureService;
import io.github.erp.service.dto.SecurityTenureDTO;
import io.github.erp.service.mapper.SecurityTenureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SecurityTenure}.
 */
@Service
@Transactional
public class SecurityTenureServiceImpl implements SecurityTenureService {

    private final Logger log = LoggerFactory.getLogger(SecurityTenureServiceImpl.class);

    private final SecurityTenureRepository securityTenureRepository;

    private final SecurityTenureMapper securityTenureMapper;

    private final SecurityTenureSearchRepository securityTenureSearchRepository;

    public SecurityTenureServiceImpl(
        SecurityTenureRepository securityTenureRepository,
        SecurityTenureMapper securityTenureMapper,
        SecurityTenureSearchRepository securityTenureSearchRepository
    ) {
        this.securityTenureRepository = securityTenureRepository;
        this.securityTenureMapper = securityTenureMapper;
        this.securityTenureSearchRepository = securityTenureSearchRepository;
    }

    @Override
    public SecurityTenureDTO save(SecurityTenureDTO securityTenureDTO) {
        log.debug("Request to save SecurityTenure : {}", securityTenureDTO);
        SecurityTenure securityTenure = securityTenureMapper.toEntity(securityTenureDTO);
        securityTenure = securityTenureRepository.save(securityTenure);
        SecurityTenureDTO result = securityTenureMapper.toDto(securityTenure);
        securityTenureSearchRepository.save(securityTenure);
        return result;
    }

    @Override
    public Optional<SecurityTenureDTO> partialUpdate(SecurityTenureDTO securityTenureDTO) {
        log.debug("Request to partially update SecurityTenure : {}", securityTenureDTO);

        return securityTenureRepository
            .findById(securityTenureDTO.getId())
            .map(existingSecurityTenure -> {
                securityTenureMapper.partialUpdate(existingSecurityTenure, securityTenureDTO);

                return existingSecurityTenure;
            })
            .map(securityTenureRepository::save)
            .map(savedSecurityTenure -> {
                securityTenureSearchRepository.save(savedSecurityTenure);

                return savedSecurityTenure;
            })
            .map(securityTenureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityTenureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityTenures");
        return securityTenureRepository.findAll(pageable).map(securityTenureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecurityTenureDTO> findOne(Long id) {
        log.debug("Request to get SecurityTenure : {}", id);
        return securityTenureRepository.findById(id).map(securityTenureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SecurityTenure : {}", id);
        securityTenureRepository.deleteById(id);
        securityTenureSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecurityTenureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityTenures for query {}", query);
        return securityTenureSearchRepository.search(query, pageable).map(securityTenureMapper::toDto);
    }
}
