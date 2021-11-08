package io.github.erp.internal.service;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.InvoiceBEO;
import io.github.erp.service.InvoiceQueryService;
import io.github.erp.service.criteria.InvoiceCriteria;
import io.github.erp.service.dto.InvoiceDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class InvoiceDeletionUploadService implements DeletionUploadService<InvoiceBEO> {

    private final Mapping<InvoiceBEO, InvoiceDTO> batchEntityDTOMapping;
    private final InvoiceQueryService queryService;

    public InvoiceDeletionUploadService(Mapping<InvoiceBEO, InvoiceDTO> batchEntityDTOMapping, InvoiceQueryService queryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.queryService = queryService;
    }

    @Override
    public Optional<List<InvoiceBEO>> findAllByUploadToken(String stringToken) {
        InvoiceCriteria criteria = new InvoiceCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(queryService.findByCriteria(criteria)));
    }
}
