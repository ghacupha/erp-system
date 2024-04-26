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
import io.github.erp.domain.CurrencyServiceabilityFlag;
import io.github.erp.repository.CurrencyServiceabilityFlagRepository;
import io.github.erp.repository.search.CurrencyServiceabilityFlagSearchRepository;
import io.github.erp.service.criteria.CurrencyServiceabilityFlagCriteria;
import io.github.erp.service.dto.CurrencyServiceabilityFlagDTO;
import io.github.erp.service.mapper.CurrencyServiceabilityFlagMapper;
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
 * Service for executing complex queries for {@link CurrencyServiceabilityFlag} entities in the database.
 * The main input is a {@link CurrencyServiceabilityFlagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CurrencyServiceabilityFlagDTO} or a {@link Page} of {@link CurrencyServiceabilityFlagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyServiceabilityFlagQueryService extends QueryService<CurrencyServiceabilityFlag> {

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceabilityFlagQueryService.class);

    private final CurrencyServiceabilityFlagRepository currencyServiceabilityFlagRepository;

    private final CurrencyServiceabilityFlagMapper currencyServiceabilityFlagMapper;

    private final CurrencyServiceabilityFlagSearchRepository currencyServiceabilityFlagSearchRepository;

    public CurrencyServiceabilityFlagQueryService(
        CurrencyServiceabilityFlagRepository currencyServiceabilityFlagRepository,
        CurrencyServiceabilityFlagMapper currencyServiceabilityFlagMapper,
        CurrencyServiceabilityFlagSearchRepository currencyServiceabilityFlagSearchRepository
    ) {
        this.currencyServiceabilityFlagRepository = currencyServiceabilityFlagRepository;
        this.currencyServiceabilityFlagMapper = currencyServiceabilityFlagMapper;
        this.currencyServiceabilityFlagSearchRepository = currencyServiceabilityFlagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CurrencyServiceabilityFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CurrencyServiceabilityFlagDTO> findByCriteria(CurrencyServiceabilityFlagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CurrencyServiceabilityFlag> specification = createSpecification(criteria);
        return currencyServiceabilityFlagMapper.toDto(currencyServiceabilityFlagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CurrencyServiceabilityFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CurrencyServiceabilityFlagDTO> findByCriteria(CurrencyServiceabilityFlagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CurrencyServiceabilityFlag> specification = createSpecification(criteria);
        return currencyServiceabilityFlagRepository.findAll(specification, page).map(currencyServiceabilityFlagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CurrencyServiceabilityFlagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CurrencyServiceabilityFlag> specification = createSpecification(criteria);
        return currencyServiceabilityFlagRepository.count(specification);
    }

    /**
     * Function to convert {@link CurrencyServiceabilityFlagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CurrencyServiceabilityFlag> createSpecification(CurrencyServiceabilityFlagCriteria criteria) {
        Specification<CurrencyServiceabilityFlag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CurrencyServiceabilityFlag_.id));
            }
            if (criteria.getCurrencyServiceabilityFlag() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCurrencyServiceabilityFlag(), CurrencyServiceabilityFlag_.currencyServiceabilityFlag)
                    );
            }
            if (criteria.getCurrencyServiceability() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCurrencyServiceability(), CurrencyServiceabilityFlag_.currencyServiceability)
                    );
            }
        }
        return specification;
    }
}
