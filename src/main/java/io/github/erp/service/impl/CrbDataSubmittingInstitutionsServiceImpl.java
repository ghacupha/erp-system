package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

import io.github.erp.domain.CrbDataSubmittingInstitutions;
import io.github.erp.repository.CrbDataSubmittingInstitutionsRepository;
import io.github.erp.repository.search.CrbDataSubmittingInstitutionsSearchRepository;
import io.github.erp.service.CrbDataSubmittingInstitutionsService;
import io.github.erp.service.dto.CrbDataSubmittingInstitutionsDTO;
import io.github.erp.service.mapper.CrbDataSubmittingInstitutionsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbDataSubmittingInstitutions}.
 */
@Service
@Transactional
public class CrbDataSubmittingInstitutionsServiceImpl implements CrbDataSubmittingInstitutionsService {

    private final Logger log = LoggerFactory.getLogger(CrbDataSubmittingInstitutionsServiceImpl.class);

    private final CrbDataSubmittingInstitutionsRepository crbDataSubmittingInstitutionsRepository;

    private final CrbDataSubmittingInstitutionsMapper crbDataSubmittingInstitutionsMapper;

    private final CrbDataSubmittingInstitutionsSearchRepository crbDataSubmittingInstitutionsSearchRepository;

    public CrbDataSubmittingInstitutionsServiceImpl(
        CrbDataSubmittingInstitutionsRepository crbDataSubmittingInstitutionsRepository,
        CrbDataSubmittingInstitutionsMapper crbDataSubmittingInstitutionsMapper,
        CrbDataSubmittingInstitutionsSearchRepository crbDataSubmittingInstitutionsSearchRepository
    ) {
        this.crbDataSubmittingInstitutionsRepository = crbDataSubmittingInstitutionsRepository;
        this.crbDataSubmittingInstitutionsMapper = crbDataSubmittingInstitutionsMapper;
        this.crbDataSubmittingInstitutionsSearchRepository = crbDataSubmittingInstitutionsSearchRepository;
    }

    @Override
    public CrbDataSubmittingInstitutionsDTO save(CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO) {
        log.debug("Request to save CrbDataSubmittingInstitutions : {}", crbDataSubmittingInstitutionsDTO);
        CrbDataSubmittingInstitutions crbDataSubmittingInstitutions = crbDataSubmittingInstitutionsMapper.toEntity(
            crbDataSubmittingInstitutionsDTO
        );
        crbDataSubmittingInstitutions = crbDataSubmittingInstitutionsRepository.save(crbDataSubmittingInstitutions);
        CrbDataSubmittingInstitutionsDTO result = crbDataSubmittingInstitutionsMapper.toDto(crbDataSubmittingInstitutions);
        crbDataSubmittingInstitutionsSearchRepository.save(crbDataSubmittingInstitutions);
        return result;
    }

    @Override
    public Optional<CrbDataSubmittingInstitutionsDTO> partialUpdate(CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO) {
        log.debug("Request to partially update CrbDataSubmittingInstitutions : {}", crbDataSubmittingInstitutionsDTO);

        return crbDataSubmittingInstitutionsRepository
            .findById(crbDataSubmittingInstitutionsDTO.getId())
            .map(existingCrbDataSubmittingInstitutions -> {
                crbDataSubmittingInstitutionsMapper.partialUpdate(existingCrbDataSubmittingInstitutions, crbDataSubmittingInstitutionsDTO);

                return existingCrbDataSubmittingInstitutions;
            })
            .map(crbDataSubmittingInstitutionsRepository::save)
            .map(savedCrbDataSubmittingInstitutions -> {
                crbDataSubmittingInstitutionsSearchRepository.save(savedCrbDataSubmittingInstitutions);

                return savedCrbDataSubmittingInstitutions;
            })
            .map(crbDataSubmittingInstitutionsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbDataSubmittingInstitutionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbDataSubmittingInstitutions");
        return crbDataSubmittingInstitutionsRepository.findAll(pageable).map(crbDataSubmittingInstitutionsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbDataSubmittingInstitutionsDTO> findOne(Long id) {
        log.debug("Request to get CrbDataSubmittingInstitutions : {}", id);
        return crbDataSubmittingInstitutionsRepository.findById(id).map(crbDataSubmittingInstitutionsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbDataSubmittingInstitutions : {}", id);
        crbDataSubmittingInstitutionsRepository.deleteById(id);
        crbDataSubmittingInstitutionsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbDataSubmittingInstitutionsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbDataSubmittingInstitutions for query {}", query);
        return crbDataSubmittingInstitutionsSearchRepository.search(query, pageable).map(crbDataSubmittingInstitutionsMapper::toDto);
    }
}
