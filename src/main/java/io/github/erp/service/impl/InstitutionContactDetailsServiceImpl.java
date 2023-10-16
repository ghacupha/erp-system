package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
