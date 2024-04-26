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
import io.github.erp.domain.RouDepreciationPostingReportItem;
import io.github.erp.repository.RouDepreciationPostingReportItemRepository;
import io.github.erp.repository.search.RouDepreciationPostingReportItemSearchRepository;
import io.github.erp.service.criteria.RouDepreciationPostingReportItemCriteria;
import io.github.erp.service.dto.RouDepreciationPostingReportItemDTO;
import io.github.erp.service.mapper.RouDepreciationPostingReportItemMapper;
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
 * Service for executing complex queries for {@link RouDepreciationPostingReportItem} entities in the database.
 * The main input is a {@link RouDepreciationPostingReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouDepreciationPostingReportItemDTO} or a {@link Page} of {@link RouDepreciationPostingReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouDepreciationPostingReportItemQueryService extends QueryService<RouDepreciationPostingReportItem> {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationPostingReportItemQueryService.class);

    private final RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository;

    private final RouDepreciationPostingReportItemMapper rouDepreciationPostingReportItemMapper;

    private final RouDepreciationPostingReportItemSearchRepository rouDepreciationPostingReportItemSearchRepository;

    public RouDepreciationPostingReportItemQueryService(
        RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository,
        RouDepreciationPostingReportItemMapper rouDepreciationPostingReportItemMapper,
        RouDepreciationPostingReportItemSearchRepository rouDepreciationPostingReportItemSearchRepository
    ) {
        this.rouDepreciationPostingReportItemRepository = rouDepreciationPostingReportItemRepository;
        this.rouDepreciationPostingReportItemMapper = rouDepreciationPostingReportItemMapper;
        this.rouDepreciationPostingReportItemSearchRepository = rouDepreciationPostingReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouDepreciationPostingReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouDepreciationPostingReportItemDTO> findByCriteria(RouDepreciationPostingReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouDepreciationPostingReportItem> specification = createSpecification(criteria);
        return rouDepreciationPostingReportItemMapper.toDto(rouDepreciationPostingReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouDepreciationPostingReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouDepreciationPostingReportItemDTO> findByCriteria(RouDepreciationPostingReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouDepreciationPostingReportItem> specification = createSpecification(criteria);
        return rouDepreciationPostingReportItemRepository.findAll(specification, page).map(rouDepreciationPostingReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouDepreciationPostingReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouDepreciationPostingReportItem> specification = createSpecification(criteria);
        return rouDepreciationPostingReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link RouDepreciationPostingReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouDepreciationPostingReportItem> createSpecification(RouDepreciationPostingReportItemCriteria criteria) {
        Specification<RouDepreciationPostingReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouDepreciationPostingReportItem_.id));
            }
            if (criteria.getLeaseContractNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLeaseContractNumber(), RouDepreciationPostingReportItem_.leaseContractNumber)
                    );
            }
            if (criteria.getLeaseDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLeaseDescription(), RouDepreciationPostingReportItem_.leaseDescription)
                    );
            }
            if (criteria.getFiscalMonthCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFiscalMonthCode(), RouDepreciationPostingReportItem_.fiscalMonthCode)
                    );
            }
            if (criteria.getAccountForCredit() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAccountForCredit(), RouDepreciationPostingReportItem_.accountForCredit)
                    );
            }
            if (criteria.getAccountForDebit() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAccountForDebit(), RouDepreciationPostingReportItem_.accountForDebit)
                    );
            }
            if (criteria.getDepreciationAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getDepreciationAmount(), RouDepreciationPostingReportItem_.depreciationAmount)
                    );
            }
        }
        return specification;
    }
}
