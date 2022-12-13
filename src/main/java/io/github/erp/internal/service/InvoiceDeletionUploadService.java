package io.github.erp.internal.service;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.6-SNAPSHOT
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
