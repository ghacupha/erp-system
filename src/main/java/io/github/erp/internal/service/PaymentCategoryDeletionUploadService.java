package io.github.erp.internal.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.PaymentCategoryBEO;
import io.github.erp.service.PaymentCategoryQueryService;
import io.github.erp.service.criteria.PaymentCategoryCriteria;
import io.github.erp.service.dto.PaymentCategoryDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PaymentCategoryDeletionUploadService implements DeletionUploadService<PaymentCategoryBEO> {

    private final Mapping<PaymentCategoryBEO, PaymentCategoryDTO> batchEntityDTOMapping;
    private final PaymentCategoryQueryService queryService;

    public PaymentCategoryDeletionUploadService(Mapping<PaymentCategoryBEO, PaymentCategoryDTO> batchEntityDTOMapping, PaymentCategoryQueryService queryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.queryService = queryService;
    }

    @Override
    public Optional<List<PaymentCategoryBEO>> findAllByUploadToken(String stringToken) {
        PaymentCategoryCriteria criteria = new PaymentCategoryCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(queryService.findByCriteria(criteria)));
    }
}
