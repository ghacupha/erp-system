package io.github.erp.service;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.BusinessDocument;
import io.github.erp.repository.BusinessDocumentRepository;
import io.github.erp.repository.search.BusinessDocumentSearchRepository;
import io.github.erp.service.criteria.BusinessDocumentCriteria;
import io.github.erp.service.dto.BusinessDocumentDTO;
import io.github.erp.service.mapper.BusinessDocumentMapper;
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
 * Service for executing complex queries for {@link BusinessDocument} entities in the database.
 * The main input is a {@link BusinessDocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusinessDocumentDTO} or a {@link Page} of {@link BusinessDocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusinessDocumentQueryService extends QueryService<BusinessDocument> {

    private final Logger log = LoggerFactory.getLogger(BusinessDocumentQueryService.class);

    private final BusinessDocumentRepository businessDocumentRepository;

    private final BusinessDocumentMapper businessDocumentMapper;

    private final BusinessDocumentSearchRepository businessDocumentSearchRepository;

    public BusinessDocumentQueryService(
        BusinessDocumentRepository businessDocumentRepository,
        BusinessDocumentMapper businessDocumentMapper,
        BusinessDocumentSearchRepository businessDocumentSearchRepository
    ) {
        this.businessDocumentRepository = businessDocumentRepository;
        this.businessDocumentMapper = businessDocumentMapper;
        this.businessDocumentSearchRepository = businessDocumentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BusinessDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessDocumentDTO> findByCriteria(BusinessDocumentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BusinessDocument> specification = createSpecification(criteria);
        return businessDocumentMapper.toDto(businessDocumentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusinessDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessDocumentDTO> findByCriteria(BusinessDocumentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BusinessDocument> specification = createSpecification(criteria);
        return businessDocumentRepository.findAll(specification, page).map(businessDocumentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusinessDocumentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BusinessDocument> specification = createSpecification(criteria);
        return businessDocumentRepository.count(specification);
    }

    /**
     * Function to convert {@link BusinessDocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BusinessDocument> createSpecification(BusinessDocumentCriteria criteria) {
        Specification<BusinessDocument> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BusinessDocument_.id));
            }
            if (criteria.getDocumentTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentTitle(), BusinessDocument_.documentTitle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), BusinessDocument_.description));
            }
            if (criteria.getDocumentSerial() != null) {
                specification = specification.and(buildSpecification(criteria.getDocumentSerial(), BusinessDocument_.documentSerial));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), BusinessDocument_.lastModified));
            }
            if (criteria.getAttachmentFilePath() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAttachmentFilePath(), BusinessDocument_.attachmentFilePath));
            }
            if (criteria.getDocumentFileContentType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDocumentFileContentType(), BusinessDocument_.documentFileContentType)
                    );
            }
            if (criteria.getFileTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getFileTampered(), BusinessDocument_.fileTampered));
            }
            if (criteria.getDocumentFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDocumentFileChecksum(), BusinessDocument_.documentFileChecksum));
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(BusinessDocument_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastModifiedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastModifiedById(),
                            root -> root.join(BusinessDocument_.lastModifiedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getOriginatingDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOriginatingDepartmentId(),
                            root -> root.join(BusinessDocument_.originatingDepartment, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getApplicationMappingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApplicationMappingsId(),
                            root -> root.join(BusinessDocument_.applicationMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(BusinessDocument_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getFileChecksumAlgorithmId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFileChecksumAlgorithmId(),
                            root -> root.join(BusinessDocument_.fileChecksumAlgorithm, JoinType.LEFT).get(Algorithm_.id)
                        )
                    );
            }
            if (criteria.getSecurityClearanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSecurityClearanceId(),
                            root -> root.join(BusinessDocument_.securityClearance, JoinType.LEFT).get(SecurityClearance_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
