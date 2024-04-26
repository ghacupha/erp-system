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
import io.github.erp.domain.JobSheet;
import io.github.erp.repository.JobSheetRepository;
import io.github.erp.repository.search.JobSheetSearchRepository;
import io.github.erp.service.criteria.JobSheetCriteria;
import io.github.erp.service.dto.JobSheetDTO;
import io.github.erp.service.mapper.JobSheetMapper;
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
 * Service for executing complex queries for {@link JobSheet} entities in the database.
 * The main input is a {@link JobSheetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobSheetDTO} or a {@link Page} of {@link JobSheetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobSheetQueryService extends QueryService<JobSheet> {

    private final Logger log = LoggerFactory.getLogger(JobSheetQueryService.class);

    private final JobSheetRepository jobSheetRepository;

    private final JobSheetMapper jobSheetMapper;

    private final JobSheetSearchRepository jobSheetSearchRepository;

    public JobSheetQueryService(
        JobSheetRepository jobSheetRepository,
        JobSheetMapper jobSheetMapper,
        JobSheetSearchRepository jobSheetSearchRepository
    ) {
        this.jobSheetRepository = jobSheetRepository;
        this.jobSheetMapper = jobSheetMapper;
        this.jobSheetSearchRepository = jobSheetSearchRepository;
    }

    /**
     * Return a {@link List} of {@link JobSheetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobSheetDTO> findByCriteria(JobSheetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<JobSheet> specification = createSpecification(criteria);
        return jobSheetMapper.toDto(jobSheetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link JobSheetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobSheetDTO> findByCriteria(JobSheetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<JobSheet> specification = createSpecification(criteria);
        return jobSheetRepository.findAll(specification, page).map(jobSheetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JobSheetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<JobSheet> specification = createSpecification(criteria);
        return jobSheetRepository.count(specification);
    }

    /**
     * Function to convert {@link JobSheetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<JobSheet> createSpecification(JobSheetCriteria criteria) {
        Specification<JobSheet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), JobSheet_.id));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNumber(), JobSheet_.serialNumber));
            }
            if (criteria.getJobSheetDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJobSheetDate(), JobSheet_.jobSheetDate));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), JobSheet_.details));
            }
            if (criteria.getBillerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBillerId(), root -> root.join(JobSheet_.biller, JoinType.LEFT).get(Dealer_.id))
                    );
            }
            if (criteria.getSignatoriesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignatoriesId(),
                            root -> root.join(JobSheet_.signatories, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getContactPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactPersonId(),
                            root -> root.join(JobSheet_.contactPerson, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getBusinessStampsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessStampsId(),
                            root -> root.join(JobSheet_.businessStamps, JoinType.LEFT).get(BusinessStamp_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(JobSheet_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getPaymentLabelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentLabelId(),
                            root -> root.join(JobSheet_.paymentLabels, JoinType.LEFT).get(PaymentLabel_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(JobSheet_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
