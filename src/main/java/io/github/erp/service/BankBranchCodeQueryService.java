package io.github.erp.service;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
import io.github.erp.domain.BankBranchCode;
import io.github.erp.repository.BankBranchCodeRepository;
import io.github.erp.repository.search.BankBranchCodeSearchRepository;
import io.github.erp.service.criteria.BankBranchCodeCriteria;
import io.github.erp.service.dto.BankBranchCodeDTO;
import io.github.erp.service.mapper.BankBranchCodeMapper;
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
 * Service for executing complex queries for {@link BankBranchCode} entities in the database.
 * The main input is a {@link BankBranchCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BankBranchCodeDTO} or a {@link Page} of {@link BankBranchCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankBranchCodeQueryService extends QueryService<BankBranchCode> {

    private final Logger log = LoggerFactory.getLogger(BankBranchCodeQueryService.class);

    private final BankBranchCodeRepository bankBranchCodeRepository;

    private final BankBranchCodeMapper bankBranchCodeMapper;

    private final BankBranchCodeSearchRepository bankBranchCodeSearchRepository;

    public BankBranchCodeQueryService(
        BankBranchCodeRepository bankBranchCodeRepository,
        BankBranchCodeMapper bankBranchCodeMapper,
        BankBranchCodeSearchRepository bankBranchCodeSearchRepository
    ) {
        this.bankBranchCodeRepository = bankBranchCodeRepository;
        this.bankBranchCodeMapper = bankBranchCodeMapper;
        this.bankBranchCodeSearchRepository = bankBranchCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BankBranchCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BankBranchCodeDTO> findByCriteria(BankBranchCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BankBranchCode> specification = createSpecification(criteria);
        return bankBranchCodeMapper.toDto(bankBranchCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BankBranchCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BankBranchCodeDTO> findByCriteria(BankBranchCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BankBranchCode> specification = createSpecification(criteria);
        return bankBranchCodeRepository.findAll(specification, page).map(bankBranchCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BankBranchCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BankBranchCode> specification = createSpecification(criteria);
        return bankBranchCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link BankBranchCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BankBranchCode> createSpecification(BankBranchCodeCriteria criteria) {
        Specification<BankBranchCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BankBranchCode_.id));
            }
            if (criteria.getBankCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankCode(), BankBranchCode_.bankCode));
            }
            if (criteria.getBankName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankName(), BankBranchCode_.bankName));
            }
            if (criteria.getBranchCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchCode(), BankBranchCode_.branchCode));
            }
            if (criteria.getBranchName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchName(), BankBranchCode_.branchName));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), BankBranchCode_.notes));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(BankBranchCode_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
