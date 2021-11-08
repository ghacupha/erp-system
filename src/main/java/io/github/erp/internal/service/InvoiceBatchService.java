package io.github.erp.internal.service;

import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.InvoiceRepository;
import io.github.erp.repository.search.InvoiceSearchRepository;
import io.github.erp.service.dto.InvoiceDTO;
import io.github.erp.service.mapper.InvoiceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class InvoiceBatchService implements BatchService<InvoiceDTO> {

    private final InvoiceMapper mapper;
    private final InvoiceRepository repository;
    private final InvoiceSearchRepository searchRepository;

    public InvoiceBatchService(InvoiceMapper mapper, InvoiceRepository repository, InvoiceSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<InvoiceDTO> save(List<InvoiceDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<InvoiceDTO> entities) {
        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
