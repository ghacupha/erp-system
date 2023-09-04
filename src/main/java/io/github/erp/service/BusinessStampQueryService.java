package io.github.erp.service;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
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
import io.github.erp.domain.BusinessStamp;
import io.github.erp.repository.BusinessStampRepository;
import io.github.erp.repository.search.BusinessStampSearchRepository;
import io.github.erp.service.criteria.BusinessStampCriteria;
import io.github.erp.service.dto.BusinessStampDTO;
import io.github.erp.service.mapper.BusinessStampMapper;
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
 * Service for executing complex queries for {@link BusinessStamp} entities in the database.
 * The main input is a {@link BusinessStampCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusinessStampDTO} or a {@link Page} of {@link BusinessStampDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusinessStampQueryService extends QueryService<BusinessStamp> {

    private final Logger log = LoggerFactory.getLogger(BusinessStampQueryService.class);

    private final BusinessStampRepository businessStampRepository;

    private final BusinessStampMapper businessStampMapper;

    private final BusinessStampSearchRepository businessStampSearchRepository;

    public BusinessStampQueryService(
        BusinessStampRepository businessStampRepository,
        BusinessStampMapper businessStampMapper,
        BusinessStampSearchRepository businessStampSearchRepository
    ) {
        this.businessStampRepository = businessStampRepository;
        this.businessStampMapper = businessStampMapper;
        this.businessStampSearchRepository = businessStampSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BusinessStampDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessStampDTO> findByCriteria(BusinessStampCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BusinessStamp> specification = createSpecification(criteria);
        return businessStampMapper.toDto(businessStampRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusinessStampDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessStampDTO> findByCriteria(BusinessStampCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BusinessStamp> specification = createSpecification(criteria);
        return businessStampRepository.findAll(specification, page).map(businessStampMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusinessStampCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BusinessStamp> specification = createSpecification(criteria);
        return businessStampRepository.count(specification);
    }

    /**
     * Function to convert {@link BusinessStampCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BusinessStamp> createSpecification(BusinessStampCriteria criteria) {
        Specification<BusinessStamp> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BusinessStamp_.id));
            }
            if (criteria.getStampDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStampDate(), BusinessStamp_.stampDate));
            }
            if (criteria.getPurpose() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPurpose(), BusinessStamp_.purpose));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), BusinessStamp_.details));
            }
            if (criteria.getStampHolderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStampHolderId(),
                            root -> root.join(BusinessStamp_.stampHolder, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(BusinessStamp_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
