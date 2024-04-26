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
