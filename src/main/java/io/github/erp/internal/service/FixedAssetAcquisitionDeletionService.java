package io.github.erp.internal.service;

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
import com.google.common.util.concurrent.Futures;
import tech.jhipster.service.filter.LongFilter;
import io.github.erp.domain.FixedAssetAcquisition;
import io.github.erp.internal.framework.batch.DeletionService;
import io.github.erp.repository.FixedAssetAcquisitionRepository;
import io.github.erp.repository.search.FixedAssetAcquisitionSearchRepository;
import io.github.erp.service.FixedAssetAcquisitionQueryService;
import io.github.erp.service.criteria.FixedAssetAcquisitionCriteria;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
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
public class FixedAssetAcquisitionDeletionService implements DeletionService<FixedAssetAcquisition> {

    private final FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository;
    private final FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository;
    private final FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService;
    private final EntityMapper<FixedAssetAcquisitionDTO, FixedAssetAcquisition> fixedAssetAcquisitionMapper;

    public FixedAssetAcquisitionDeletionService(
        final FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository,
        final FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository,
        final FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService,
        final EntityMapper<FixedAssetAcquisitionDTO, FixedAssetAcquisition> fixedAssetAcquisitionMapper
    ) {
        this.fixedAssetAcquisitionRepository = fixedAssetAcquisitionRepository;
        this.fixedAssetAcquisitionSearchRepository = fixedAssetAcquisitionSearchRepository;
        this.fixedAssetAcquisitionQueryService = fixedAssetAcquisitionQueryService;
        this.fixedAssetAcquisitionMapper = fixedAssetAcquisitionMapper;
    }

    /**
     * Implements batch deletion for entities with the ids in the list provided
     *
     * @param list of ids of objects to delete
     * @return List of deleted and detached objects
     */
    @Override
    @Async
    public Future<List<FixedAssetAcquisition>> delete(final List<Long> list) {
        List<FixedAssetAcquisition> deletedAccounts = new CopyOnWriteArrayList<>();

        Optional<List<FixedAssetAcquisition>> deletable = Optional.of(new CopyOnWriteArrayList<>());

        List<FixedAssetAcquisitionCriteria> criteriaList = new CopyOnWriteArrayList<>();

        list.forEach(
            id -> {
                LongFilter idFilter = new LongFilter();
                idFilter.setEquals(id);
                FixedAssetAcquisitionCriteria criteria = new FixedAssetAcquisitionCriteria();
                criteria.setId(idFilter);
                criteriaList.add(criteria);
            }
        );

        mapCriteriaList(deletable, criteriaList, fixedAssetAcquisitionQueryService);

        deletable.ifPresent(deletedAccounts::addAll);

        deletable.ifPresent(fixedAssetAcquisitionRepository::deleteAll);

        deletable.ifPresent(
            deletableItems ->
                fixedAssetAcquisitionSearchRepository.deleteAll(
                    // use deletable
                    deletableItems
                        .stream()
                        .filter(ac -> fixedAssetAcquisitionSearchRepository.existsById(ac.getId()))
                        .collect(Collectors.toList())
                )
        );

        return Futures.immediateFuture(deletedAccounts);
    }

    public void mapCriteriaList(Optional<List<FixedAssetAcquisition>> deletable, List<FixedAssetAcquisitionCriteria> criteriaList, FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService) {
        criteriaList.forEach(
            criteria ->
                deletable.ifPresent(
                    toDelete ->
                        toDelete.addAll(
                            fixedAssetAcquisitionQueryService
                                .findByCriteria(criteria)
                                .stream()
                                .map(fixedAssetAcquisitionMapper::toEntity)
                                .collect(Collectors.toList())
                        )
                )
        );
    }
}
