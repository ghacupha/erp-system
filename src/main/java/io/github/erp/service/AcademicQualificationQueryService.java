package io.github.erp.service;

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
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.AcademicQualification;
import io.github.erp.repository.AcademicQualificationRepository;
import io.github.erp.repository.search.AcademicQualificationSearchRepository;
import io.github.erp.service.criteria.AcademicQualificationCriteria;
import io.github.erp.service.dto.AcademicQualificationDTO;
import io.github.erp.service.mapper.AcademicQualificationMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AcademicQualification} entities in the database.
 * The main input is a {@link AcademicQualificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AcademicQualificationDTO} or a {@link Page} of {@link AcademicQualificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AcademicQualificationQueryService extends QueryService<AcademicQualification> {

    private final Logger log = LoggerFactory.getLogger(AcademicQualificationQueryService.class);

    private final AcademicQualificationRepository academicQualificationRepository;

    private final AcademicQualificationMapper academicQualificationMapper;

    private final AcademicQualificationSearchRepository academicQualificationSearchRepository;

    public AcademicQualificationQueryService(
        AcademicQualificationRepository academicQualificationRepository,
        AcademicQualificationMapper academicQualificationMapper,
        AcademicQualificationSearchRepository academicQualificationSearchRepository
    ) {
        this.academicQualificationRepository = academicQualificationRepository;
        this.academicQualificationMapper = academicQualificationMapper;
        this.academicQualificationSearchRepository = academicQualificationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AcademicQualificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AcademicQualificationDTO> findByCriteria(AcademicQualificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AcademicQualification> specification = createSpecification(criteria);
        return academicQualificationMapper.toDto(academicQualificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AcademicQualificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AcademicQualificationDTO> findByCriteria(AcademicQualificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AcademicQualification> specification = createSpecification(criteria);
        return academicQualificationRepository.findAll(specification, page).map(academicQualificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AcademicQualificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AcademicQualification> specification = createSpecification(criteria);
        return academicQualificationRepository.count(specification);
    }

    /**
     * Function to convert {@link AcademicQualificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AcademicQualification> createSpecification(AcademicQualificationCriteria criteria) {
        Specification<AcademicQualification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AcademicQualification_.id));
            }
            if (criteria.getAcademicQualificationsCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAcademicQualificationsCode(),
                            AcademicQualification_.academicQualificationsCode
                        )
                    );
            }
            if (criteria.getAcademicQualificationType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAcademicQualificationType(), AcademicQualification_.academicQualificationType)
                    );
            }
        }
        return specification;
    }
}
