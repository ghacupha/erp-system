package io.github.erp.service;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.AccountAttributeMetadata;
import io.github.erp.repository.AccountAttributeMetadataRepository;
import io.github.erp.repository.search.AccountAttributeMetadataSearchRepository;
import io.github.erp.service.criteria.AccountAttributeMetadataCriteria;
import io.github.erp.service.dto.AccountAttributeMetadataDTO;
import io.github.erp.service.mapper.AccountAttributeMetadataMapper;
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
 * Service for executing complex queries for {@link AccountAttributeMetadata} entities in the database.
 * The main input is a {@link AccountAttributeMetadataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccountAttributeMetadataDTO} or a {@link Page} of {@link AccountAttributeMetadataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountAttributeMetadataQueryService extends QueryService<AccountAttributeMetadata> {

    private final Logger log = LoggerFactory.getLogger(AccountAttributeMetadataQueryService.class);

    private final AccountAttributeMetadataRepository accountAttributeMetadataRepository;

    private final AccountAttributeMetadataMapper accountAttributeMetadataMapper;

    private final AccountAttributeMetadataSearchRepository accountAttributeMetadataSearchRepository;

    public AccountAttributeMetadataQueryService(
        AccountAttributeMetadataRepository accountAttributeMetadataRepository,
        AccountAttributeMetadataMapper accountAttributeMetadataMapper,
        AccountAttributeMetadataSearchRepository accountAttributeMetadataSearchRepository
    ) {
        this.accountAttributeMetadataRepository = accountAttributeMetadataRepository;
        this.accountAttributeMetadataMapper = accountAttributeMetadataMapper;
        this.accountAttributeMetadataSearchRepository = accountAttributeMetadataSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AccountAttributeMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccountAttributeMetadataDTO> findByCriteria(AccountAttributeMetadataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccountAttributeMetadata> specification = createSpecification(criteria);
        return accountAttributeMetadataMapper.toDto(accountAttributeMetadataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AccountAttributeMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountAttributeMetadataDTO> findByCriteria(AccountAttributeMetadataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountAttributeMetadata> specification = createSpecification(criteria);
        return accountAttributeMetadataRepository.findAll(specification, page).map(accountAttributeMetadataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountAttributeMetadataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccountAttributeMetadata> specification = createSpecification(criteria);
        return accountAttributeMetadataRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountAttributeMetadataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountAttributeMetadata> createSpecification(AccountAttributeMetadataCriteria criteria) {
        Specification<AccountAttributeMetadata> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccountAttributeMetadata_.id));
            }
            if (criteria.getPrecedence() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecedence(), AccountAttributeMetadata_.precedence));
            }
            if (criteria.getColumnName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColumnName(), AccountAttributeMetadata_.columnName));
            }
            if (criteria.getShortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortName(), AccountAttributeMetadata_.shortName));
            }
            if (criteria.getDataType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataType(), AccountAttributeMetadata_.dataType));
            }
            if (criteria.getLength() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLength(), AccountAttributeMetadata_.length));
            }
            if (criteria.getColumnIndex() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getColumnIndex(), AccountAttributeMetadata_.columnIndex));
            }
            if (criteria.getMandatoryFieldFlag() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getMandatoryFieldFlag(), AccountAttributeMetadata_.mandatoryFieldFlag));
            }
            if (criteria.getDbColumnName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDbColumnName(), AccountAttributeMetadata_.dbColumnName));
            }
            if (criteria.getMetadataVersion() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMetadataVersion(), AccountAttributeMetadata_.metadataVersion));
            }
            if (criteria.getStandardInputTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStandardInputTemplateId(),
                            root -> root.join(AccountAttributeMetadata_.standardInputTemplate, JoinType.LEFT).get(GdiMasterDataIndex_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
