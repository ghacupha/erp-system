package io.github.erp.internal.service;

import com.google.common.util.concurrent.Futures;
import io.github.erp.domain.PaymentCategory;
import io.github.erp.internal.framework.batch.DeletionService;
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.service.PaymentCategoryQueryService;
import io.github.erp.service.criteria.PaymentCategoryCriteria;
import io.github.erp.service.dto.PaymentCategoryDTO;
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
public class PaymentCategoryDeletionService implements DeletionService<PaymentCategory> {

    private final PaymentCategoryRepository repository;
    private final PaymentCategorySearchRepository searchRepository;
    private final PaymentCategoryQueryService queryService;
    private final EntityMapper<PaymentCategoryDTO, PaymentCategory> mapper;

    public PaymentCategoryDeletionService(PaymentCategoryRepository repository, PaymentCategorySearchRepository searchRepository, PaymentCategoryQueryService queryService, EntityMapper<PaymentCategoryDTO, PaymentCategory> mapper) {
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
    public Future<List<PaymentCategory>> delete(final List<Long> list) {
        List<PaymentCategory> deletedAccounts = new CopyOnWriteArrayList<>();

        Optional<List<PaymentCategory>> deletable = Optional.of(new CopyOnWriteArrayList<>());

        List<PaymentCategoryCriteria> criteriaList = new CopyOnWriteArrayList<>();

        list.forEach(
            id -> {
                LongFilter idFilter = new LongFilter();
                idFilter.setEquals(id);
                PaymentCategoryCriteria criteria = new PaymentCategoryCriteria();
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

    public void mapCriteriaList(Optional<List<PaymentCategory>> deletable, List<PaymentCategoryCriteria> criteriaList) {
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
