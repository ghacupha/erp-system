package io.github.erp.internal.service;

import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.SignedPaymentRepository;
import io.github.erp.repository.search.SignedPaymentSearchRepository;
import io.github.erp.service.dto.SignedPaymentDTO;
import io.github.erp.service.mapper.SignedPaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class SignedPaymentBatchService implements BatchService<SignedPaymentDTO> {

    private final SignedPaymentMapper mapper;
    private final SignedPaymentRepository repository;
    private final SignedPaymentSearchRepository searchRepository;

    public SignedPaymentBatchService(SignedPaymentMapper mapper, SignedPaymentRepository repository, SignedPaymentSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<SignedPaymentDTO> save(List<SignedPaymentDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<SignedPaymentDTO> entities) {
        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
