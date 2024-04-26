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
import io.github.erp.domain.InterestCalcMethod;
import io.github.erp.repository.InterestCalcMethodRepository;
import io.github.erp.repository.search.InterestCalcMethodSearchRepository;
import io.github.erp.service.criteria.InterestCalcMethodCriteria;
import io.github.erp.service.dto.InterestCalcMethodDTO;
import io.github.erp.service.mapper.InterestCalcMethodMapper;
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
 * Service for executing complex queries for {@link InterestCalcMethod} entities in the database.
 * The main input is a {@link InterestCalcMethodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InterestCalcMethodDTO} or a {@link Page} of {@link InterestCalcMethodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterestCalcMethodQueryService extends QueryService<InterestCalcMethod> {

    private final Logger log = LoggerFactory.getLogger(InterestCalcMethodQueryService.class);

    private final InterestCalcMethodRepository interestCalcMethodRepository;

    private final InterestCalcMethodMapper interestCalcMethodMapper;

    private final InterestCalcMethodSearchRepository interestCalcMethodSearchRepository;

    public InterestCalcMethodQueryService(
        InterestCalcMethodRepository interestCalcMethodRepository,
        InterestCalcMethodMapper interestCalcMethodMapper,
        InterestCalcMethodSearchRepository interestCalcMethodSearchRepository
    ) {
        this.interestCalcMethodRepository = interestCalcMethodRepository;
        this.interestCalcMethodMapper = interestCalcMethodMapper;
        this.interestCalcMethodSearchRepository = interestCalcMethodSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InterestCalcMethodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InterestCalcMethodDTO> findByCriteria(InterestCalcMethodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InterestCalcMethod> specification = createSpecification(criteria);
        return interestCalcMethodMapper.toDto(interestCalcMethodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InterestCalcMethodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InterestCalcMethodDTO> findByCriteria(InterestCalcMethodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InterestCalcMethod> specification = createSpecification(criteria);
        return interestCalcMethodRepository.findAll(specification, page).map(interestCalcMethodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterestCalcMethodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InterestCalcMethod> specification = createSpecification(criteria);
        return interestCalcMethodRepository.count(specification);
    }

    /**
     * Function to convert {@link InterestCalcMethodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InterestCalcMethod> createSpecification(InterestCalcMethodCriteria criteria) {
        Specification<InterestCalcMethod> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InterestCalcMethod_.id));
            }
            if (criteria.getInterestCalculationMethodCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getInterestCalculationMethodCode(),
                            InterestCalcMethod_.interestCalculationMethodCode
                        )
                    );
            }
            if (criteria.getInterestCalculationMthodType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getInterestCalculationMthodType(),
                            InterestCalcMethod_.interestCalculationMthodType
                        )
                    );
            }
        }
        return specification;
    }
}
