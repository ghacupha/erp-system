package io.github.erp.internal.service;

import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.AssetDepreciationBatchEntityDTOMapping;
import io.github.erp.internal.model.PaymentCategoryBEO;
import io.github.erp.internal.model.PaymentCategoryBEO;
import io.github.erp.service.PaymentCategoryQueryService;
import io.github.erp.service.criteria.PaymentCategoryCriteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PaymentCategoryDeletionUploadService implements DeletionUploadService<PaymentCategoryBEO> {

    private final AssetDepreciationBatchEntityDTOMapping batchEntityDTOMapping;
    private final PaymentCategoryQueryService queryService;

    public PaymentCategoryDeletionUploadService(AssetDepreciationBatchEntityDTOMapping batchEntityDTOMapping, PaymentCategoryQueryService queryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.queryService = queryService;
    }

    @Override
    public Optional<List<PaymentCategoryBEO>> findAllByUploadToken(String stringToken) {
        PaymentCategoryCriteria criteria = new PaymentCategoryCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        // criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(queryService.findByCriteria(criteria)));
    }
}
