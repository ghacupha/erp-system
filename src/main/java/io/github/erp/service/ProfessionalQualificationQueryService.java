package io.github.erp.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.ProfessionalQualification;
import io.github.erp.repository.ProfessionalQualificationRepository;
import io.github.erp.repository.search.ProfessionalQualificationSearchRepository;
import io.github.erp.service.criteria.ProfessionalQualificationCriteria;
import io.github.erp.service.dto.ProfessionalQualificationDTO;
import io.github.erp.service.mapper.ProfessionalQualificationMapper;
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
 * Service for executing complex queries for {@link ProfessionalQualification} entities in the database.
 * The main input is a {@link ProfessionalQualificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfessionalQualificationDTO} or a {@link Page} of {@link ProfessionalQualificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfessionalQualificationQueryService extends QueryService<ProfessionalQualification> {

    private final Logger log = LoggerFactory.getLogger(ProfessionalQualificationQueryService.class);

    private final ProfessionalQualificationRepository professionalQualificationRepository;

    private final ProfessionalQualificationMapper professionalQualificationMapper;

    private final ProfessionalQualificationSearchRepository professionalQualificationSearchRepository;

    public ProfessionalQualificationQueryService(
        ProfessionalQualificationRepository professionalQualificationRepository,
        ProfessionalQualificationMapper professionalQualificationMapper,
        ProfessionalQualificationSearchRepository professionalQualificationSearchRepository
    ) {
        this.professionalQualificationRepository = professionalQualificationRepository;
        this.professionalQualificationMapper = professionalQualificationMapper;
        this.professionalQualificationSearchRepository = professionalQualificationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProfessionalQualificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfessionalQualificationDTO> findByCriteria(ProfessionalQualificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProfessionalQualification> specification = createSpecification(criteria);
        return professionalQualificationMapper.toDto(professionalQualificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProfessionalQualificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfessionalQualificationDTO> findByCriteria(ProfessionalQualificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfessionalQualification> specification = createSpecification(criteria);
        return professionalQualificationRepository.findAll(specification, page).map(professionalQualificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfessionalQualificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProfessionalQualification> specification = createSpecification(criteria);
        return professionalQualificationRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfessionalQualificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProfessionalQualification> createSpecification(ProfessionalQualificationCriteria criteria) {
        Specification<ProfessionalQualification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProfessionalQualification_.id));
            }
            if (criteria.getProfessionalQualificationsCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getProfessionalQualificationsCode(),
                            ProfessionalQualification_.professionalQualificationsCode
                        )
                    );
            }
            if (criteria.getProfessionalQualificationsType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getProfessionalQualificationsType(),
                            ProfessionalQualification_.professionalQualificationsType
                        )
                    );
            }
        }
        return specification;
    }
}
