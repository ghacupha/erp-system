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
import io.github.erp.domain.MfbBranchCode;
import io.github.erp.repository.MfbBranchCodeRepository;
import io.github.erp.repository.search.MfbBranchCodeSearchRepository;
import io.github.erp.service.criteria.MfbBranchCodeCriteria;
import io.github.erp.service.dto.MfbBranchCodeDTO;
import io.github.erp.service.mapper.MfbBranchCodeMapper;
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
 * Service for executing complex queries for {@link MfbBranchCode} entities in the database.
 * The main input is a {@link MfbBranchCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MfbBranchCodeDTO} or a {@link Page} of {@link MfbBranchCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MfbBranchCodeQueryService extends QueryService<MfbBranchCode> {

    private final Logger log = LoggerFactory.getLogger(MfbBranchCodeQueryService.class);

    private final MfbBranchCodeRepository mfbBranchCodeRepository;

    private final MfbBranchCodeMapper mfbBranchCodeMapper;

    private final MfbBranchCodeSearchRepository mfbBranchCodeSearchRepository;

    public MfbBranchCodeQueryService(
        MfbBranchCodeRepository mfbBranchCodeRepository,
        MfbBranchCodeMapper mfbBranchCodeMapper,
        MfbBranchCodeSearchRepository mfbBranchCodeSearchRepository
    ) {
        this.mfbBranchCodeRepository = mfbBranchCodeRepository;
        this.mfbBranchCodeMapper = mfbBranchCodeMapper;
        this.mfbBranchCodeSearchRepository = mfbBranchCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MfbBranchCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MfbBranchCodeDTO> findByCriteria(MfbBranchCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MfbBranchCode> specification = createSpecification(criteria);
        return mfbBranchCodeMapper.toDto(mfbBranchCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MfbBranchCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MfbBranchCodeDTO> findByCriteria(MfbBranchCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MfbBranchCode> specification = createSpecification(criteria);
        return mfbBranchCodeRepository.findAll(specification, page).map(mfbBranchCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MfbBranchCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MfbBranchCode> specification = createSpecification(criteria);
        return mfbBranchCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link MfbBranchCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MfbBranchCode> createSpecification(MfbBranchCodeCriteria criteria) {
        Specification<MfbBranchCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MfbBranchCode_.id));
            }
            if (criteria.getBankCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankCode(), MfbBranchCode_.bankCode));
            }
            if (criteria.getBankName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankName(), MfbBranchCode_.bankName));
            }
            if (criteria.getBranchCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchCode(), MfbBranchCode_.branchCode));
            }
            if (criteria.getBranchName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchName(), MfbBranchCode_.branchName));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(MfbBranchCode_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
