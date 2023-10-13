package io.github.erp.service;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.WorkProjectRegister;
import io.github.erp.repository.WorkProjectRegisterRepository;
import io.github.erp.repository.search.WorkProjectRegisterSearchRepository;
import io.github.erp.service.criteria.WorkProjectRegisterCriteria;
import io.github.erp.service.dto.WorkProjectRegisterDTO;
import io.github.erp.service.mapper.WorkProjectRegisterMapper;
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
 * Service for executing complex queries for {@link WorkProjectRegister} entities in the database.
 * The main input is a {@link WorkProjectRegisterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkProjectRegisterDTO} or a {@link Page} of {@link WorkProjectRegisterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkProjectRegisterQueryService extends QueryService<WorkProjectRegister> {

    private final Logger log = LoggerFactory.getLogger(WorkProjectRegisterQueryService.class);

    private final WorkProjectRegisterRepository workProjectRegisterRepository;

    private final WorkProjectRegisterMapper workProjectRegisterMapper;

    private final WorkProjectRegisterSearchRepository workProjectRegisterSearchRepository;

    public WorkProjectRegisterQueryService(
        WorkProjectRegisterRepository workProjectRegisterRepository,
        WorkProjectRegisterMapper workProjectRegisterMapper,
        WorkProjectRegisterSearchRepository workProjectRegisterSearchRepository
    ) {
        this.workProjectRegisterRepository = workProjectRegisterRepository;
        this.workProjectRegisterMapper = workProjectRegisterMapper;
        this.workProjectRegisterSearchRepository = workProjectRegisterSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WorkProjectRegisterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkProjectRegisterDTO> findByCriteria(WorkProjectRegisterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkProjectRegister> specification = createSpecification(criteria);
        return workProjectRegisterMapper.toDto(workProjectRegisterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WorkProjectRegisterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkProjectRegisterDTO> findByCriteria(WorkProjectRegisterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkProjectRegister> specification = createSpecification(criteria);
        return workProjectRegisterRepository.findAll(specification, page).map(workProjectRegisterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkProjectRegisterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkProjectRegister> specification = createSpecification(criteria);
        return workProjectRegisterRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkProjectRegisterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkProjectRegister> createSpecification(WorkProjectRegisterCriteria criteria) {
        Specification<WorkProjectRegister> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkProjectRegister_.id));
            }
            if (criteria.getCatalogueNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCatalogueNumber(), WorkProjectRegister_.catalogueNumber));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), WorkProjectRegister_.description));
            }
            if (criteria.getTotalProjectCost() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTotalProjectCost(), WorkProjectRegister_.totalProjectCost));
            }
            if (criteria.getDealersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDealersId(),
                            root -> root.join(WorkProjectRegister_.dealers, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(WorkProjectRegister_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(WorkProjectRegister_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(WorkProjectRegister_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
