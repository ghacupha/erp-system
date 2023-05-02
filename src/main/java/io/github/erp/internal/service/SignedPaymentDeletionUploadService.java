package io.github.erp.internal.service;

/*-
 * Erp System - Mark III No 13 (Caleb Series) Server ver 1.1.3-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
