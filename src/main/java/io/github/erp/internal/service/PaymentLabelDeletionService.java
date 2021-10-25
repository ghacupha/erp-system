package io.github.erp.internal.service;

import com.google.common.util.concurrent.Futures;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.internal.framework.batch.DeletionService;
import io.github.erp.repository.PaymentLabelRepository;
import io.github.erp.repository.search.PaymentLabelSearchRepository;
import io.github.erp.service.PaymentLabelQueryService;
import io.github.erp.service.criteria.PaymentLabelCriteria;
import io.github.erp.service.dto.PaymentLabelDTO;
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
public class PaymentLabelDeletionService implements DeletionService<PaymentLabel> {

    private final PaymentLabelRepository repository;
    private final PaymentLabelSearchRepository searchRepository;
    private final PaymentLabelQueryService queryService;
    private final EntityMapper<PaymentLabelDTO, PaymentLabel> mapper;

    public PaymentLabelDeletionService(PaymentLabelRepository repository, PaymentLabelSearchRepository searchRepository, PaymentLabelQueryService queryService, EntityMapper<PaymentLabelDTO, PaymentLabel> mapper) {
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
    public Future<List<PaymentLabel>> delete(final List<Long> list) {
        List<PaymentLabel> deletedAccounts = new CopyOnWriteArrayList<>();

        Optional<List<PaymentLabel>> deletable = Optional.of(new CopyOnWriteArrayList<>());

        List<PaymentLabelCriteria> criteriaList = new CopyOnWriteArrayList<>();

        list.forEach(
            id -> {
                LongFilter idFilter = new LongFilter();
                idFilter.setEquals(id);
                PaymentLabelCriteria criteria = new PaymentLabelCriteria();
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

    public void mapCriteriaList(Optional<List<PaymentLabel>> deletable, List<PaymentLabelCriteria> criteriaList) {
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
