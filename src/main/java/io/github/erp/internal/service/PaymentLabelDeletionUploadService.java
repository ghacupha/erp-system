package io.github.erp.internal.service;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.PaymentLabelBEO;
import io.github.erp.internal.model.PaymentLabelBEO;
import io.github.erp.service.PaymentLabelQueryService;
import io.github.erp.service.criteria.PaymentLabelCriteria;
import io.github.erp.service.dto.PaymentLabelDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PaymentLabelDeletionUploadService implements DeletionUploadService<PaymentLabelBEO> {

    private final Mapping<PaymentLabelBEO, PaymentLabelDTO> batchEntityDTOMapping;
    private final PaymentLabelQueryService queryService;

    public PaymentLabelDeletionUploadService(Mapping<PaymentLabelBEO, PaymentLabelDTO> batchEntityDTOMapping, PaymentLabelQueryService queryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.queryService = queryService;
    }

    @Override
    public Optional<List<PaymentLabelBEO>> findAllByUploadToken(String stringToken) {
        PaymentLabelCriteria criteria = new PaymentLabelCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(queryService.findByCriteria(criteria)));
    }
}
