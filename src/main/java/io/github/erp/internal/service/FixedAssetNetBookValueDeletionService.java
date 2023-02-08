package io.github.erp.internal.service;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import com.google.common.util.concurrent.Futures;
import tech.jhipster.service.filter.LongFilter;
import io.github.erp.domain.FixedAssetNetBookValue;
import io.github.erp.internal.framework.batch.DeletionService;
import io.github.erp.repository.FixedAssetNetBookValueRepository;
import io.github.erp.repository.search.FixedAssetNetBookValueSearchRepository;
import io.github.erp.service.FixedAssetNetBookValueQueryService;
import io.github.erp.service.criteria.FixedAssetNetBookValueCriteria;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.service.mapper.EntityMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Transactional
@Service
public class FixedAssetNetBookValueDeletionService  implements DeletionService<FixedAssetNetBookValue> {

    private final FixedAssetNetBookValueRepository fixedAssetDepreciationRepository;
    private final FixedAssetNetBookValueSearchRepository fixedAssetDepreciationSearchRepository;
    private final FixedAssetNetBookValueQueryService fixedAssetDepreciationQueryService;
    private final EntityMapper<FixedAssetNetBookValueDTO, FixedAssetNetBookValue> fixedAssetDepreciationMapper;

    public FixedAssetNetBookValueDeletionService(FixedAssetNetBookValueRepository fixedAssetDepreciationRepository, FixedAssetNetBookValueSearchRepository fixedAssetDepreciationSearchRepository, FixedAssetNetBookValueQueryService fixedAssetDepreciationQueryService, EntityMapper<FixedAssetNetBookValueDTO, FixedAssetNetBookValue> fixedAssetDepreciationMapper) {
        this.fixedAssetDepreciationRepository = fixedAssetDepreciationRepository;
        this.fixedAssetDepreciationSearchRepository = fixedAssetDepreciationSearchRepository;
        this.fixedAssetDepreciationQueryService = fixedAssetDepreciationQueryService;
        this.fixedAssetDepreciationMapper = fixedAssetDepreciationMapper;
    }

    /**
     * Implements batch deletion for entities with the ids in the list provided
     *
     * @param list of ids of objects to delete
     * @return List of deleted and detached objects
     */
    @Override
    @Async
    public Future<List<FixedAssetNetBookValue>> delete(final List<Long> list) {
        List<FixedAssetNetBookValue> deletedAccounts = new CopyOnWriteArrayList<>();

        Optional<List<FixedAssetNetBookValue>> deletable = Optional.of(new CopyOnWriteArrayList<>());

        List<FixedAssetNetBookValueCriteria> criteriaList = new CopyOnWriteArrayList<>();

        list.forEach(
            id -> {
                LongFilter idFilter = new LongFilter();
                idFilter.setEquals(id);
                FixedAssetNetBookValueCriteria criteria = new FixedAssetNetBookValueCriteria();
                criteria.setId(idFilter);
                criteriaList.add(criteria);
            }
        );

        mapCriteriaList(deletable, criteriaList, fixedAssetDepreciationQueryService);

        deletable.ifPresent(deletedAccounts::addAll);

        deletable.ifPresent(fixedAssetDepreciationRepository::deleteAll);

        deletable.ifPresent(
            deletableItems ->
                fixedAssetDepreciationSearchRepository.deleteAll(
                    // use deletable
                    deletableItems
                        .stream()
                        .filter(ac -> fixedAssetDepreciationSearchRepository.existsById(ac.getId()))
                        .collect(Collectors.toList())
                )
        );

        return Futures.immediateFuture(deletedAccounts);
    }

    public void mapCriteriaList(Optional<List<FixedAssetNetBookValue>> deletable, List<FixedAssetNetBookValueCriteria> criteriaList, FixedAssetNetBookValueQueryService fixedAssetDepreciationQueryService) {
        criteriaList.forEach(
            criteria ->
                deletable.ifPresent(
                    toDelete ->
                        toDelete.addAll(
                            fixedAssetDepreciationQueryService
                                .findByCriteria(criteria)
                                .stream()
                                .map(fixedAssetDepreciationMapper::toEntity)
                                .collect(Collectors.toList())
                        )
                )
        );
    }
}
