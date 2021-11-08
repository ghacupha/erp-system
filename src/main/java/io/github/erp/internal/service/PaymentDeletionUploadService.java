package io.github.erp.internal.service;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.PaymentBEO;
import io.github.erp.service.PaymentQueryService;
import io.github.erp.service.criteria.PaymentCriteria;
import io.github.erp.service.dto.PaymentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PaymentDeletionUploadService implements DeletionUploadService<PaymentBEO> {

    private final Mapping<PaymentBEO, PaymentDTO> batchEntityDTOMapping;
    private final PaymentQueryService queryService;

    public PaymentDeletionUploadService(Mapping<PaymentBEO, PaymentDTO> batchEntityDTOMapping, PaymentQueryService queryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.queryService = queryService;
    }

    @Override
    public Optional<List<PaymentBEO>> findAllByUploadToken(String stringToken) {
        PaymentCriteria criteria = new PaymentCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(queryService.findByCriteria(criteria)));
    }
}
