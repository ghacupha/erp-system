package io.github.erp.internal.service;

import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.mapper.PaymentCategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("paymentCategoryBatchService")
public class PaymentCategoryBatchService implements BatchService<PaymentCategoryDTO> {

    private final PaymentCategoryMapper mapper;
    private final PaymentCategoryRepository repository;
    private final PaymentCategorySearchRepository searchRepository;

    public PaymentCategoryBatchService(PaymentCategoryMapper mapper, PaymentCategoryRepository repository, PaymentCategorySearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<PaymentCategoryDTO> save(List<PaymentCategoryDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<PaymentCategoryDTO> entities) {

        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
