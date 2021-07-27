package io.github.erp.internal.service;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import com.google.common.util.concurrent.Futures;
import io.github.jhipster.service.filter.LongFilter;
import io.github.erp.domain.FixedAssetNetBookValue;
import io.github.erp.internal.framework.batch.DeletionService;
import io.github.erp.repository.FixedAssetNetBookValueRepository;
import io.github.erp.repository.search.FixedAssetNetBookValueSearchRepository;
import io.github.erp.service.FixedAssetNetBookValueQueryService;
import io.github.erp.service.dto.FixedAssetNetBookValueCriteria;
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
