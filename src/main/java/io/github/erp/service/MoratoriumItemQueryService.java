package io.github.erp.service;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.domain.MoratoriumItem;
import io.github.erp.repository.MoratoriumItemRepository;
import io.github.erp.repository.search.MoratoriumItemSearchRepository;
import io.github.erp.service.criteria.MoratoriumItemCriteria;
import io.github.erp.service.dto.MoratoriumItemDTO;
import io.github.erp.service.mapper.MoratoriumItemMapper;
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
 * Service for executing complex queries for {@link MoratoriumItem} entities in the database.
 * The main input is a {@link MoratoriumItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MoratoriumItemDTO} or a {@link Page} of {@link MoratoriumItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MoratoriumItemQueryService extends QueryService<MoratoriumItem> {

    private final Logger log = LoggerFactory.getLogger(MoratoriumItemQueryService.class);

    private final MoratoriumItemRepository moratoriumItemRepository;

    private final MoratoriumItemMapper moratoriumItemMapper;

    private final MoratoriumItemSearchRepository moratoriumItemSearchRepository;

    public MoratoriumItemQueryService(
        MoratoriumItemRepository moratoriumItemRepository,
        MoratoriumItemMapper moratoriumItemMapper,
        MoratoriumItemSearchRepository moratoriumItemSearchRepository
    ) {
        this.moratoriumItemRepository = moratoriumItemRepository;
        this.moratoriumItemMapper = moratoriumItemMapper;
        this.moratoriumItemSearchRepository = moratoriumItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MoratoriumItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MoratoriumItemDTO> findByCriteria(MoratoriumItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MoratoriumItem> specification = createSpecification(criteria);
        return moratoriumItemMapper.toDto(moratoriumItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MoratoriumItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MoratoriumItemDTO> findByCriteria(MoratoriumItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MoratoriumItem> specification = createSpecification(criteria);
        return moratoriumItemRepository.findAll(specification, page).map(moratoriumItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MoratoriumItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MoratoriumItem> specification = createSpecification(criteria);
        return moratoriumItemRepository.count(specification);
    }

    /**
     * Function to convert {@link MoratoriumItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MoratoriumItem> createSpecification(MoratoriumItemCriteria criteria) {
        Specification<MoratoriumItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MoratoriumItem_.id));
            }
            if (criteria.getMoratoriumItemTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getMoratoriumItemTypeCode(), MoratoriumItem_.moratoriumItemTypeCode)
                    );
            }
            if (criteria.getMoratoriumItemType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMoratoriumItemType(), MoratoriumItem_.moratoriumItemType));
            }
        }
        return specification;
    }
}
