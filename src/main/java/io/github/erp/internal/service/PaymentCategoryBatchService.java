package io.github.erp.internal.service;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
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
import io.github.erp.internal.framework.BatchService;
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.mapper.PaymentCategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("paymentCategoryBatchService")
public class PaymentCategoryBatchService implements BatchService<PaymentCategoryDTO> {

    private final PaymentCategoryMapper mapper;
    private final PaymentCategoryRepository repository;
    private final PaymentCategorySearchRepository searchRepository;

    public PaymentCategoryBatchService(PaymentCategoryMapper mapper, PaymentCategoryRepository repository, PaymentCategorySearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<PaymentCategoryDTO> save(List<PaymentCategoryDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<PaymentCategoryDTO> entities) {

        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
