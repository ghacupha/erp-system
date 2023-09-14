package io.github.erp.service.impl;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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

import io.github.erp.domain.AcademicQualification;
import io.github.erp.repository.AcademicQualificationRepository;
import io.github.erp.repository.search.AcademicQualificationSearchRepository;
import io.github.erp.service.AcademicQualificationService;
import io.github.erp.service.dto.AcademicQualificationDTO;
import io.github.erp.service.mapper.AcademicQualificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AcademicQualification}.
 */
@Service
@Transactional
public class AcademicQualificationServiceImpl implements AcademicQualificationService {

    private final Logger log = LoggerFactory.getLogger(AcademicQualificationServiceImpl.class);

    private final AcademicQualificationRepository academicQualificationRepository;

    private final AcademicQualificationMapper academicQualificationMapper;

    private final AcademicQualificationSearchRepository academicQualificationSearchRepository;

    public AcademicQualificationServiceImpl(
        AcademicQualificationRepository academicQualificationRepository,
        AcademicQualificationMapper academicQualificationMapper,
        AcademicQualificationSearchRepository academicQualificationSearchRepository
    ) {
        this.academicQualificationRepository = academicQualificationRepository;
        this.academicQualificationMapper = academicQualificationMapper;
        this.academicQualificationSearchRepository = academicQualificationSearchRepository;
    }

    @Override
    public AcademicQualificationDTO save(AcademicQualificationDTO academicQualificationDTO) {
        log.debug("Request to save AcademicQualification : {}", academicQualificationDTO);
        AcademicQualification academicQualification = academicQualificationMapper.toEntity(academicQualificationDTO);
        academicQualification = academicQualificationRepository.save(academicQualification);
        AcademicQualificationDTO result = academicQualificationMapper.toDto(academicQualification);
        academicQualificationSearchRepository.save(academicQualification);
        return result;
    }

    @Override
    public Optional<AcademicQualificationDTO> partialUpdate(AcademicQualificationDTO academicQualificationDTO) {
        log.debug("Request to partially update AcademicQualification : {}", academicQualificationDTO);

        return academicQualificationRepository
            .findById(academicQualificationDTO.getId())
            .map(existingAcademicQualification -> {
                academicQualificationMapper.partialUpdate(existingAcademicQualification, academicQualificationDTO);

                return existingAcademicQualification;
            })
            .map(academicQualificationRepository::save)
            .map(savedAcademicQualification -> {
                academicQualificationSearchRepository.save(savedAcademicQualification);

                return savedAcademicQualification;
            })
            .map(academicQualificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicQualificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AcademicQualifications");
        return academicQualificationRepository.findAll(pageable).map(academicQualificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcademicQualificationDTO> findOne(Long id) {
        log.debug("Request to get AcademicQualification : {}", id);
        return academicQualificationRepository.findById(id).map(academicQualificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AcademicQualification : {}", id);
        academicQualificationRepository.deleteById(id);
        academicQualificationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AcademicQualificationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AcademicQualifications for query {}", query);
        return academicQualificationSearchRepository.search(query, pageable).map(academicQualificationMapper::toDto);
    }
}
