package io.github.erp.internal.service;

import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.mapper.PaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PaymentBatchService implements BatchService<PaymentDTO> {

    private final PaymentMapper mapper;
    private final PaymentRepository repository;
    private final PaymentSearchRepository searchRepository;

    public PaymentBatchService(PaymentMapper mapper, PaymentRepository repository, PaymentSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<PaymentDTO> save(List<PaymentDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<PaymentDTO> entities) {
        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
