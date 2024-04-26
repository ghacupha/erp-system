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
import io.github.erp.domain.LoanRestructureFlag;
import io.github.erp.repository.LoanRestructureFlagRepository;
import io.github.erp.repository.search.LoanRestructureFlagSearchRepository;
import io.github.erp.service.criteria.LoanRestructureFlagCriteria;
import io.github.erp.service.dto.LoanRestructureFlagDTO;
import io.github.erp.service.mapper.LoanRestructureFlagMapper;
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
 * Service for executing complex queries for {@link LoanRestructureFlag} entities in the database.
 * The main input is a {@link LoanRestructureFlagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanRestructureFlagDTO} or a {@link Page} of {@link LoanRestructureFlagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanRestructureFlagQueryService extends QueryService<LoanRestructureFlag> {

    private final Logger log = LoggerFactory.getLogger(LoanRestructureFlagQueryService.class);

    private final LoanRestructureFlagRepository loanRestructureFlagRepository;

    private final LoanRestructureFlagMapper loanRestructureFlagMapper;

    private final LoanRestructureFlagSearchRepository loanRestructureFlagSearchRepository;

    public LoanRestructureFlagQueryService(
        LoanRestructureFlagRepository loanRestructureFlagRepository,
        LoanRestructureFlagMapper loanRestructureFlagMapper,
        LoanRestructureFlagSearchRepository loanRestructureFlagSearchRepository
    ) {
        this.loanRestructureFlagRepository = loanRestructureFlagRepository;
        this.loanRestructureFlagMapper = loanRestructureFlagMapper;
        this.loanRestructureFlagSearchRepository = loanRestructureFlagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanRestructureFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanRestructureFlagDTO> findByCriteria(LoanRestructureFlagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanRestructureFlag> specification = createSpecification(criteria);
        return loanRestructureFlagMapper.toDto(loanRestructureFlagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanRestructureFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanRestructureFlagDTO> findByCriteria(LoanRestructureFlagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanRestructureFlag> specification = createSpecification(criteria);
        return loanRestructureFlagRepository.findAll(specification, page).map(loanRestructureFlagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanRestructureFlagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanRestructureFlag> specification = createSpecification(criteria);
        return loanRestructureFlagRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanRestructureFlagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanRestructureFlag> createSpecification(LoanRestructureFlagCriteria criteria) {
        Specification<LoanRestructureFlag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanRestructureFlag_.id));
            }
            if (criteria.getLoanRestructureFlagCode() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLoanRestructureFlagCode(), LoanRestructureFlag_.loanRestructureFlagCode)
                    );
            }
            if (criteria.getLoanRestructureFlagType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanRestructureFlagType(), LoanRestructureFlag_.loanRestructureFlagType)
                    );
            }
        }
        return specification;
    }
}
