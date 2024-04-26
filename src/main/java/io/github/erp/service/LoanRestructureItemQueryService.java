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
import io.github.erp.domain.LoanRestructureItem;
import io.github.erp.repository.LoanRestructureItemRepository;
import io.github.erp.repository.search.LoanRestructureItemSearchRepository;
import io.github.erp.service.criteria.LoanRestructureItemCriteria;
import io.github.erp.service.dto.LoanRestructureItemDTO;
import io.github.erp.service.mapper.LoanRestructureItemMapper;
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
 * Service for executing complex queries for {@link LoanRestructureItem} entities in the database.
 * The main input is a {@link LoanRestructureItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanRestructureItemDTO} or a {@link Page} of {@link LoanRestructureItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanRestructureItemQueryService extends QueryService<LoanRestructureItem> {

    private final Logger log = LoggerFactory.getLogger(LoanRestructureItemQueryService.class);

    private final LoanRestructureItemRepository loanRestructureItemRepository;

    private final LoanRestructureItemMapper loanRestructureItemMapper;

    private final LoanRestructureItemSearchRepository loanRestructureItemSearchRepository;

    public LoanRestructureItemQueryService(
        LoanRestructureItemRepository loanRestructureItemRepository,
        LoanRestructureItemMapper loanRestructureItemMapper,
        LoanRestructureItemSearchRepository loanRestructureItemSearchRepository
    ) {
        this.loanRestructureItemRepository = loanRestructureItemRepository;
        this.loanRestructureItemMapper = loanRestructureItemMapper;
        this.loanRestructureItemSearchRepository = loanRestructureItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanRestructureItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanRestructureItemDTO> findByCriteria(LoanRestructureItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanRestructureItem> specification = createSpecification(criteria);
        return loanRestructureItemMapper.toDto(loanRestructureItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanRestructureItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanRestructureItemDTO> findByCriteria(LoanRestructureItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanRestructureItem> specification = createSpecification(criteria);
        return loanRestructureItemRepository.findAll(specification, page).map(loanRestructureItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanRestructureItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanRestructureItem> specification = createSpecification(criteria);
        return loanRestructureItemRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanRestructureItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanRestructureItem> createSpecification(LoanRestructureItemCriteria criteria) {
        Specification<LoanRestructureItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanRestructureItem_.id));
            }
            if (criteria.getLoanRestructureItemCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanRestructureItemCode(), LoanRestructureItem_.loanRestructureItemCode)
                    );
            }
            if (criteria.getLoanRestructureItemType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanRestructureItemType(), LoanRestructureItem_.loanRestructureItemType)
                    );
            }
        }
        return specification;
    }
}
