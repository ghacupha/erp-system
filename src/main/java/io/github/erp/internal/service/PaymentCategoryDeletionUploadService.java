package io.github.erp.internal.service;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
