package io.github.erp.internal.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.SignedPayment;
import io.github.erp.internal.framework.batch.DeletionService;
import io.github.erp.repository.SignedPaymentRepository;
import io.github.erp.repository.search.SignedPaymentSearchRepository;
import io.github.erp.service.SignedPaymentQueryService;
import io.github.erp.service.criteria.SignedPaymentCriteria;
import io.github.erp.service.dto.SignedPaymentDTO;
import io.github.erp.service.mapper.EntityMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.LongFilter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Transactional
@Service
public class SignedPaymentDeletionService implements DeletionService<SignedPayment> {

    private final SignedPaymentRepository repository;
    private final SignedPaymentSearchRepository searchRepository;
    private final SignedPaymentQueryService queryService;
    private final EntityMapper<SignedPaymentDTO, SignedPayment> mapper;

    public SignedPaymentDeletionService(SignedPaymentRepository repository, SignedPaymentSearchRepository searchRepository, SignedPaymentQueryService queryService, EntityMapper<SignedPaymentDTO, SignedPayment> mapper) {
        this.repository = repository;
        this.searchRepository = searchRepository;
        this.queryService = queryService;
        this.mapper = mapper;
    }

    /**
     * Implements batch deletion for entities with the ids in the list provided
     *
     * @param list of ids of objects to delete
     * @return List of deleted and detached objects
     */
    @Override
    @Async
    public Future<List<SignedPayment>> delete(final List<Long> list) {
        List<SignedPayment> deletedAccounts = new CopyOnWriteArrayList<>();

        Optional<List<SignedPayment>> deletable = Optional.of(new CopyOnWriteArrayList<>());

        List<SignedPaymentCriteria> criteriaList = new CopyOnWriteArrayList<>();

        list.forEach(
            id -> {
                LongFilter idFilter = new LongFilter();
                idFilter.setEquals(id);
                SignedPaymentCriteria criteria = new SignedPaymentCriteria();
                criteria.setId(idFilter);
                criteriaList.add(criteria);
            }
        );

        mapCriteriaList(deletable, criteriaList);

        deletable.ifPresent(deletedAccounts::addAll);

        deletable.ifPresent(repository::deleteAll);

        deletable.ifPresent(
            deletableItems ->
                searchRepository.deleteAll(
                    // use deletable
                    deletableItems
                        .stream()
                        .filter(ac -> searchRepository.existsById(ac.getId()))
                        .collect(Collectors.toList())
                )
        );

        return Futures.immediateFuture(deletedAccounts);
    }

    public void mapCriteriaList(Optional<List<SignedPayment>> deletable, List<SignedPaymentCriteria> criteriaList) {
        criteriaList.forEach(
            criteria ->
                deletable.ifPresent(
                    toDelete ->
                        toDelete.addAll(
                            queryService
                                .findByCriteria(criteria)
                                .stream()
                                .map(mapper::toEntity)
                                .collect(Collectors.toList())
                        )
                )
        );
    }
}
