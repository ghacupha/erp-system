package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.domain.ProfessionalQualification;
import io.github.erp.repository.ProfessionalQualificationRepository;
import io.github.erp.repository.search.ProfessionalQualificationSearchRepository;
import io.github.erp.service.ProfessionalQualificationService;
import io.github.erp.service.dto.ProfessionalQualificationDTO;
import io.github.erp.service.mapper.ProfessionalQualificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProfessionalQualification}.
 */
@Service
@Transactional
public class ProfessionalQualificationServiceImpl implements ProfessionalQualificationService {

    private final Logger log = LoggerFactory.getLogger(ProfessionalQualificationServiceImpl.class);

    private final ProfessionalQualificationRepository professionalQualificationRepository;

    private final ProfessionalQualificationMapper professionalQualificationMapper;

    private final ProfessionalQualificationSearchRepository professionalQualificationSearchRepository;

    public ProfessionalQualificationServiceImpl(
        ProfessionalQualificationRepository professionalQualificationRepository,
        ProfessionalQualificationMapper professionalQualificationMapper,
        ProfessionalQualificationSearchRepository professionalQualificationSearchRepository
    ) {
        this.professionalQualificationRepository = professionalQualificationRepository;
        this.professionalQualificationMapper = professionalQualificationMapper;
        this.professionalQualificationSearchRepository = professionalQualificationSearchRepository;
    }

    @Override
    public ProfessionalQualificationDTO save(ProfessionalQualificationDTO professionalQualificationDTO) {
        log.debug("Request to save ProfessionalQualification : {}", professionalQualificationDTO);
        ProfessionalQualification professionalQualification = professionalQualificationMapper.toEntity(professionalQualificationDTO);
        professionalQualification = professionalQualificationRepository.save(professionalQualification);
        ProfessionalQualificationDTO result = professionalQualificationMapper.toDto(professionalQualification);
        professionalQualificationSearchRepository.save(professionalQualification);
        return result;
    }

    @Override
    public Optional<ProfessionalQualificationDTO> partialUpdate(ProfessionalQualificationDTO professionalQualificationDTO) {
        log.debug("Request to partially update ProfessionalQualification : {}", professionalQualificationDTO);

        return professionalQualificationRepository
            .findById(professionalQualificationDTO.getId())
            .map(existingProfessionalQualification -> {
                professionalQualificationMapper.partialUpdate(existingProfessionalQualification, professionalQualificationDTO);

                return existingProfessionalQualification;
            })
            .map(professionalQualificationRepository::save)
            .map(savedProfessionalQualification -> {
                professionalQualificationSearchRepository.save(savedProfessionalQualification);

                return savedProfessionalQualification;
            })
            .map(professionalQualificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionalQualificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProfessionalQualifications");
        return professionalQualificationRepository.findAll(pageable).map(professionalQualificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfessionalQualificationDTO> findOne(Long id) {
        log.debug("Request to get ProfessionalQualification : {}", id);
        return professionalQualificationRepository.findById(id).map(professionalQualificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProfessionalQualification : {}", id);
        professionalQualificationRepository.deleteById(id);
        professionalQualificationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionalQualificationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProfessionalQualifications for query {}", query);
        return professionalQualificationSearchRepository.search(query, pageable).map(professionalQualificationMapper::toDto);
    }
}
