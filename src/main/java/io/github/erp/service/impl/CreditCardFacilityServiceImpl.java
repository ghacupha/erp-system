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

import io.github.erp.domain.CreditCardFacility;
import io.github.erp.repository.CreditCardFacilityRepository;
import io.github.erp.repository.search.CreditCardFacilitySearchRepository;
import io.github.erp.service.CreditCardFacilityService;
import io.github.erp.service.dto.CreditCardFacilityDTO;
import io.github.erp.service.mapper.CreditCardFacilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CreditCardFacility}.
 */
@Service
@Transactional
public class CreditCardFacilityServiceImpl implements CreditCardFacilityService {

    private final Logger log = LoggerFactory.getLogger(CreditCardFacilityServiceImpl.class);

    private final CreditCardFacilityRepository creditCardFacilityRepository;

    private final CreditCardFacilityMapper creditCardFacilityMapper;

    private final CreditCardFacilitySearchRepository creditCardFacilitySearchRepository;

    public CreditCardFacilityServiceImpl(
        CreditCardFacilityRepository creditCardFacilityRepository,
        CreditCardFacilityMapper creditCardFacilityMapper,
        CreditCardFacilitySearchRepository creditCardFacilitySearchRepository
    ) {
        this.creditCardFacilityRepository = creditCardFacilityRepository;
        this.creditCardFacilityMapper = creditCardFacilityMapper;
        this.creditCardFacilitySearchRepository = creditCardFacilitySearchRepository;
    }

    @Override
    public CreditCardFacilityDTO save(CreditCardFacilityDTO creditCardFacilityDTO) {
        log.debug("Request to save CreditCardFacility : {}", creditCardFacilityDTO);
        CreditCardFacility creditCardFacility = creditCardFacilityMapper.toEntity(creditCardFacilityDTO);
        creditCardFacility = creditCardFacilityRepository.save(creditCardFacility);
        CreditCardFacilityDTO result = creditCardFacilityMapper.toDto(creditCardFacility);
        creditCardFacilitySearchRepository.save(creditCardFacility);
        return result;
    }

    @Override
    public Optional<CreditCardFacilityDTO> partialUpdate(CreditCardFacilityDTO creditCardFacilityDTO) {
        log.debug("Request to partially update CreditCardFacility : {}", creditCardFacilityDTO);

        return creditCardFacilityRepository
            .findById(creditCardFacilityDTO.getId())
            .map(existingCreditCardFacility -> {
                creditCardFacilityMapper.partialUpdate(existingCreditCardFacility, creditCardFacilityDTO);

                return existingCreditCardFacility;
            })
            .map(creditCardFacilityRepository::save)
            .map(savedCreditCardFacility -> {
                creditCardFacilitySearchRepository.save(savedCreditCardFacility);

                return savedCreditCardFacility;
            })
            .map(creditCardFacilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardFacilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CreditCardFacilities");
        return creditCardFacilityRepository.findAll(pageable).map(creditCardFacilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditCardFacilityDTO> findOne(Long id) {
        log.debug("Request to get CreditCardFacility : {}", id);
        return creditCardFacilityRepository.findById(id).map(creditCardFacilityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CreditCardFacility : {}", id);
        creditCardFacilityRepository.deleteById(id);
        creditCardFacilitySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardFacilityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CreditCardFacilities for query {}", query);
        return creditCardFacilitySearchRepository.search(query, pageable).map(creditCardFacilityMapper::toDto);
    }
}
