package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.SecurityClearance;
import io.github.erp.repository.SecurityClearanceRepository;
import io.github.erp.repository.search.SecurityClearanceSearchRepository;
import io.github.erp.service.criteria.SecurityClearanceCriteria;
import io.github.erp.service.dto.SecurityClearanceDTO;
import io.github.erp.service.mapper.SecurityClearanceMapper;
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
 * Service for executing complex queries for {@link SecurityClearance} entities in the database.
 * The main input is a {@link SecurityClearanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SecurityClearanceDTO} or a {@link Page} of {@link SecurityClearanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SecurityClearanceQueryService extends QueryService<SecurityClearance> {

    private final Logger log = LoggerFactory.getLogger(SecurityClearanceQueryService.class);

    private final SecurityClearanceRepository securityClearanceRepository;

    private final SecurityClearanceMapper securityClearanceMapper;

    private final SecurityClearanceSearchRepository securityClearanceSearchRepository;

    public SecurityClearanceQueryService(
        SecurityClearanceRepository securityClearanceRepository,
        SecurityClearanceMapper securityClearanceMapper,
        SecurityClearanceSearchRepository securityClearanceSearchRepository
    ) {
        this.securityClearanceRepository = securityClearanceRepository;
        this.securityClearanceMapper = securityClearanceMapper;
        this.securityClearanceSearchRepository = securityClearanceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SecurityClearanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SecurityClearanceDTO> findByCriteria(SecurityClearanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SecurityClearance> specification = createSpecification(criteria);
        return securityClearanceMapper.toDto(securityClearanceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SecurityClearanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SecurityClearanceDTO> findByCriteria(SecurityClearanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SecurityClearance> specification = createSpecification(criteria);
        return securityClearanceRepository.findAll(specification, page).map(securityClearanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SecurityClearanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SecurityClearance> specification = createSpecification(criteria);
        return securityClearanceRepository.count(specification);
    }

    /**
     * Function to convert {@link SecurityClearanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SecurityClearance> createSpecification(SecurityClearanceCriteria criteria) {
        Specification<SecurityClearance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SecurityClearance_.id));
            }
            if (criteria.getClearanceLevel() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getClearanceLevel(), SecurityClearance_.clearanceLevel));
            }
            if (criteria.getGrantedClearancesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGrantedClearancesId(),
                            root -> root.join(SecurityClearance_.grantedClearances, JoinType.LEFT).get(SecurityClearance_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(SecurityClearance_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getSystemParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemParametersId(),
                            root -> root.join(SecurityClearance_.systemParameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
