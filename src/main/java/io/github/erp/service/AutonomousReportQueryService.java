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
import io.github.erp.domain.AutonomousReport;
import io.github.erp.repository.AutonomousReportRepository;
import io.github.erp.repository.search.AutonomousReportSearchRepository;
import io.github.erp.service.criteria.AutonomousReportCriteria;
import io.github.erp.service.dto.AutonomousReportDTO;
import io.github.erp.service.mapper.AutonomousReportMapper;
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
 * Service for executing complex queries for {@link AutonomousReport} entities in the database.
 * The main input is a {@link AutonomousReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AutonomousReportDTO} or a {@link Page} of {@link AutonomousReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AutonomousReportQueryService extends QueryService<AutonomousReport> {

    private final Logger log = LoggerFactory.getLogger(AutonomousReportQueryService.class);

    private final AutonomousReportRepository autonomousReportRepository;

    private final AutonomousReportMapper autonomousReportMapper;

    private final AutonomousReportSearchRepository autonomousReportSearchRepository;

    public AutonomousReportQueryService(
        AutonomousReportRepository autonomousReportRepository,
        AutonomousReportMapper autonomousReportMapper,
        AutonomousReportSearchRepository autonomousReportSearchRepository
    ) {
        this.autonomousReportRepository = autonomousReportRepository;
        this.autonomousReportMapper = autonomousReportMapper;
        this.autonomousReportSearchRepository = autonomousReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AutonomousReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AutonomousReportDTO> findByCriteria(AutonomousReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AutonomousReport> specification = createSpecification(criteria);
        return autonomousReportMapper.toDto(autonomousReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AutonomousReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AutonomousReportDTO> findByCriteria(AutonomousReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AutonomousReport> specification = createSpecification(criteria);
        return autonomousReportRepository.findAll(specification, page).map(autonomousReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AutonomousReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AutonomousReport> specification = createSpecification(criteria);
        return autonomousReportRepository.count(specification);
    }

    /**
     * Function to convert {@link AutonomousReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AutonomousReport> createSpecification(AutonomousReportCriteria criteria) {
        Specification<AutonomousReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AutonomousReport_.id));
            }
            if (criteria.getReportName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReportName(), AutonomousReport_.reportName));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), AutonomousReport_.reportParameters));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), AutonomousReport_.createdAt));
            }
            if (criteria.getReportFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getReportFilename(), AutonomousReport_.reportFilename));
            }
            if (criteria.getFileChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileChecksum(), AutonomousReport_.fileChecksum));
            }
            if (criteria.getReportTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getReportTampered(), AutonomousReport_.reportTampered));
            }
            if (criteria.getReportMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportMappingId(),
                            root -> root.join(AutonomousReport_.reportMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AutonomousReport_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(AutonomousReport_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
