package io.github.erp.internal.service;

/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
