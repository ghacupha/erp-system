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
import io.github.erp.domain.BankTransactionType;
import io.github.erp.repository.BankTransactionTypeRepository;
import io.github.erp.repository.search.BankTransactionTypeSearchRepository;
import io.github.erp.service.criteria.BankTransactionTypeCriteria;
import io.github.erp.service.dto.BankTransactionTypeDTO;
import io.github.erp.service.mapper.BankTransactionTypeMapper;
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
 * Service for executing complex queries for {@link BankTransactionType} entities in the database.
 * The main input is a {@link BankTransactionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BankTransactionTypeDTO} or a {@link Page} of {@link BankTransactionTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankTransactionTypeQueryService extends QueryService<BankTransactionType> {

    private final Logger log = LoggerFactory.getLogger(BankTransactionTypeQueryService.class);

    private final BankTransactionTypeRepository bankTransactionTypeRepository;

    private final BankTransactionTypeMapper bankTransactionTypeMapper;

    private final BankTransactionTypeSearchRepository bankTransactionTypeSearchRepository;

    public BankTransactionTypeQueryService(
        BankTransactionTypeRepository bankTransactionTypeRepository,
        BankTransactionTypeMapper bankTransactionTypeMapper,
        BankTransactionTypeSearchRepository bankTransactionTypeSearchRepository
    ) {
        this.bankTransactionTypeRepository = bankTransactionTypeRepository;
        this.bankTransactionTypeMapper = bankTransactionTypeMapper;
        this.bankTransactionTypeSearchRepository = bankTransactionTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BankTransactionTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BankTransactionTypeDTO> findByCriteria(BankTransactionTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BankTransactionType> specification = createSpecification(criteria);
        return bankTransactionTypeMapper.toDto(bankTransactionTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BankTransactionTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BankTransactionTypeDTO> findByCriteria(BankTransactionTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BankTransactionType> specification = createSpecification(criteria);
        return bankTransactionTypeRepository.findAll(specification, page).map(bankTransactionTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BankTransactionTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BankTransactionType> specification = createSpecification(criteria);
        return bankTransactionTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link BankTransactionTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BankTransactionType> createSpecification(BankTransactionTypeCriteria criteria) {
        Specification<BankTransactionType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BankTransactionType_.id));
            }
            if (criteria.getTransactionTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getTransactionTypeCode(), BankTransactionType_.transactionTypeCode)
                    );
            }
            if (criteria.getTransactionTypeDetails() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getTransactionTypeDetails(), BankTransactionType_.transactionTypeDetails)
                    );
            }
        }
        return specification;
    }
}
