package io.github.erp.internal.service;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.SignedPaymentBEO;
import io.github.erp.service.SignedPaymentQueryService;
import io.github.erp.service.criteria.SignedPaymentCriteria;
import io.github.erp.service.dto.SignedPaymentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class SignedPaymentDeletionUploadService implements DeletionUploadService<SignedPaymentBEO> {

    private final Mapping<SignedPaymentBEO, SignedPaymentDTO> batchEntityDTOMapping;
    private final SignedPaymentQueryService queryService;

    public SignedPaymentDeletionUploadService(Mapping<SignedPaymentBEO, SignedPaymentDTO> batchEntityDTOMapping, SignedPaymentQueryService queryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.queryService = queryService;
    }

    @Override
    public Optional<List<SignedPaymentBEO>> findAllByUploadToken(String stringToken) {
        SignedPaymentCriteria criteria = new SignedPaymentCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(queryService.findByCriteria(criteria)));
    }
}
