package io.github.erp.service;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
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
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.ApplicationUser;
import io.github.erp.repository.ApplicationUserRepository;
import io.github.erp.repository.search.ApplicationUserSearchRepository;
import io.github.erp.service.criteria.ApplicationUserCriteria;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
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
 * Service for executing complex queries for {@link ApplicationUser} entities in the database.
 * The main input is a {@link ApplicationUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ApplicationUserDTO} or a {@link Page} of {@link ApplicationUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ApplicationUserQueryService extends QueryService<ApplicationUser> {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserQueryService.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserMapper applicationUserMapper;

    private final ApplicationUserSearchRepository applicationUserSearchRepository;

    public ApplicationUserQueryService(
        ApplicationUserRepository applicationUserRepository,
        ApplicationUserMapper applicationUserMapper,
        ApplicationUserSearchRepository applicationUserSearchRepository
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserMapper = applicationUserMapper;
        this.applicationUserSearchRepository = applicationUserSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ApplicationUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicationUserDTO> findByCriteria(ApplicationUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserMapper.toDto(applicationUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ApplicationUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationUserDTO> findByCriteria(ApplicationUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.findAll(specification, page).map(applicationUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ApplicationUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ApplicationUser> specification = createSpecification(criteria);
        return applicationUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ApplicationUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ApplicationUser> createSpecification(ApplicationUserCriteria criteria) {
        Specification<ApplicationUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ApplicationUser_.id));
            }
            if (criteria.getDesignation() != null) {
                specification = specification.and(buildSpecification(criteria.getDesignation(), ApplicationUser_.designation));
            }
            if (criteria.getApplicationIdentity() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getApplicationIdentity(), ApplicationUser_.applicationIdentity));
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(ApplicationUser_.organization, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(ApplicationUser_.department, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getSecurityClearanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSecurityClearanceId(),
                            root -> root.join(ApplicationUser_.securityClearance, JoinType.LEFT).get(SecurityClearance_.id)
                        )
                    );
            }
            if (criteria.getSystemIdentityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemIdentityId(),
                            root -> root.join(ApplicationUser_.systemIdentity, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getUserPropertiesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserPropertiesId(),
                            root -> root.join(ApplicationUser_.userProperties, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getDealerIdentityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDealerIdentityId(),
                            root -> root.join(ApplicationUser_.dealerIdentity, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ApplicationUser_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
