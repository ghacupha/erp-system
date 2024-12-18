package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
