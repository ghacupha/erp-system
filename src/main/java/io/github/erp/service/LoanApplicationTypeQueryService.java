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
import io.github.erp.domain.LoanApplicationType;
import io.github.erp.repository.LoanApplicationTypeRepository;
import io.github.erp.repository.search.LoanApplicationTypeSearchRepository;
import io.github.erp.service.criteria.LoanApplicationTypeCriteria;
import io.github.erp.service.dto.LoanApplicationTypeDTO;
import io.github.erp.service.mapper.LoanApplicationTypeMapper;
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
 * Service for executing complex queries for {@link LoanApplicationType} entities in the database.
 * The main input is a {@link LoanApplicationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanApplicationTypeDTO} or a {@link Page} of {@link LoanApplicationTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanApplicationTypeQueryService extends QueryService<LoanApplicationType> {

    private final Logger log = LoggerFactory.getLogger(LoanApplicationTypeQueryService.class);

    private final LoanApplicationTypeRepository loanApplicationTypeRepository;

    private final LoanApplicationTypeMapper loanApplicationTypeMapper;

    private final LoanApplicationTypeSearchRepository loanApplicationTypeSearchRepository;

    public LoanApplicationTypeQueryService(
        LoanApplicationTypeRepository loanApplicationTypeRepository,
        LoanApplicationTypeMapper loanApplicationTypeMapper,
        LoanApplicationTypeSearchRepository loanApplicationTypeSearchRepository
    ) {
        this.loanApplicationTypeRepository = loanApplicationTypeRepository;
        this.loanApplicationTypeMapper = loanApplicationTypeMapper;
        this.loanApplicationTypeSearchRepository = loanApplicationTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanApplicationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanApplicationTypeDTO> findByCriteria(LoanApplicationTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanApplicationType> specification = createSpecification(criteria);
        return loanApplicationTypeMapper.toDto(loanApplicationTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanApplicationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanApplicationTypeDTO> findByCriteria(LoanApplicationTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanApplicationType> specification = createSpecification(criteria);
        return loanApplicationTypeRepository.findAll(specification, page).map(loanApplicationTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanApplicationTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanApplicationType> specification = createSpecification(criteria);
        return loanApplicationTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanApplicationTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanApplicationType> createSpecification(LoanApplicationTypeCriteria criteria) {
        Specification<LoanApplicationType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanApplicationType_.id));
            }
            if (criteria.getLoanApplicationTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanApplicationTypeCode(), LoanApplicationType_.loanApplicationTypeCode)
                    );
            }
            if (criteria.getLoanApplicationType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanApplicationType(), LoanApplicationType_.loanApplicationType)
                    );
            }
        }
        return specification;
    }
}
