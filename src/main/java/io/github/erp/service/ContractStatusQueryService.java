package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.ContractStatus;
import io.github.erp.repository.ContractStatusRepository;
import io.github.erp.repository.search.ContractStatusSearchRepository;
import io.github.erp.service.criteria.ContractStatusCriteria;
import io.github.erp.service.dto.ContractStatusDTO;
import io.github.erp.service.mapper.ContractStatusMapper;
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
 * Service for executing complex queries for {@link ContractStatus} entities in the database.
 * The main input is a {@link ContractStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContractStatusDTO} or a {@link Page} of {@link ContractStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractStatusQueryService extends QueryService<ContractStatus> {

    private final Logger log = LoggerFactory.getLogger(ContractStatusQueryService.class);

    private final ContractStatusRepository contractStatusRepository;

    private final ContractStatusMapper contractStatusMapper;

    private final ContractStatusSearchRepository contractStatusSearchRepository;

    public ContractStatusQueryService(
        ContractStatusRepository contractStatusRepository,
        ContractStatusMapper contractStatusMapper,
        ContractStatusSearchRepository contractStatusSearchRepository
    ) {
        this.contractStatusRepository = contractStatusRepository;
        this.contractStatusMapper = contractStatusMapper;
        this.contractStatusSearchRepository = contractStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContractStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContractStatusDTO> findByCriteria(ContractStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContractStatus> specification = createSpecification(criteria);
        return contractStatusMapper.toDto(contractStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContractStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractStatusDTO> findByCriteria(ContractStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContractStatus> specification = createSpecification(criteria);
        return contractStatusRepository.findAll(specification, page).map(contractStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContractStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContractStatus> specification = createSpecification(criteria);
        return contractStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link ContractStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContractStatus> createSpecification(ContractStatusCriteria criteria) {
        Specification<ContractStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContractStatus_.id));
            }
            if (criteria.getContractStatusCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContractStatusCode(), ContractStatus_.contractStatusCode));
            }
            if (criteria.getContractStatusType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContractStatusType(), ContractStatus_.contractStatusType));
            }
        }
        return specification;
    }
}
