package io.github.erp.internal.service;

import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.PaymentLabelRepository;
import io.github.erp.repository.search.PaymentLabelSearchRepository;
import io.github.erp.service.dto.PaymentLabelDTO;
import io.github.erp.service.mapper.PaymentLabelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("paymentLabelBatchService")
public class PaymentLabelBatchService implements BatchService<PaymentLabelDTO> {

    private final PaymentLabelMapper mapper;
    private final PaymentLabelRepository repository;
    private final PaymentLabelSearchRepository searchRepository;

    public PaymentLabelBatchService(PaymentLabelMapper mapper, PaymentLabelRepository repository, PaymentLabelSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<PaymentLabelDTO> save(List<PaymentLabelDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<PaymentLabelDTO> entities) {

        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
