package io.github.erp.internal.service;

import com.google.common.util.concurrent.Futures;
import io.github.erp.domain.Payment;
import io.github.erp.internal.framework.batch.DeletionService;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.service.PaymentQueryService;
import io.github.erp.service.criteria.PaymentCriteria;
import io.github.erp.service.dto.PaymentDTO;
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
public class PaymentDeletionService implements DeletionService<Payment> {

    private final PaymentRepository repository;
    private final PaymentSearchRepository searchRepository;
    private final PaymentQueryService queryService;
    private final EntityMapper<PaymentDTO, Payment> mapper;

    public PaymentDeletionService(PaymentRepository repository, PaymentSearchRepository searchRepository, PaymentQueryService queryService, EntityMapper<PaymentDTO, Payment> mapper) {
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
    public Future<List<Payment>> delete(final List<Long> list) {
        List<Payment> deletedAccounts = new CopyOnWriteArrayList<>();

        Optional<List<Payment>> deletable = Optional.of(new CopyOnWriteArrayList<>());

        List<PaymentCriteria> criteriaList = new CopyOnWriteArrayList<>();

        list.forEach(
            id -> {
                LongFilter idFilter = new LongFilter();
                idFilter.setEquals(id);
                PaymentCriteria criteria = new PaymentCriteria();
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

    public void mapCriteriaList(Optional<List<Payment>> deletable, List<PaymentCriteria> criteriaList) {
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
