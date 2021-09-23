
/*-
 * Payment Records - Payment records is part of the ERP System
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
package io.github.erp.erp;

import com.google.common.collect.ImmutableList;
import io.github.erp.domain.Payment;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * A service to index all payments during the application startup
 */
@Transactional
@Service
public class StartupPaymentsIndexingService extends AbtractStartUpIndexService implements ApplicationIndexingService, ApplicationListener<ApplicationReadyEvent> {

    private final PaymentSearchRepository paymentSearchRepository;
    private final PaymentRepository paymentRepository;

    public StartupPaymentsIndexingService(PaymentSearchRepository paymentSearchRepository, PaymentRepository paymentRepository) {
        this.paymentSearchRepository = paymentSearchRepository;
        this.paymentRepository = paymentRepository;
    }

    @Async
    @Override
    public void index() {

       List<Payment> entities  = paymentRepository.findAll();

       paymentSearchRepository.saveAll(
            entities.stream()
                .filter(entity -> !paymentSearchRepository.existsById(entity.getId()))
                .collect(ImmutableList.toImmutableList()));
    }
}
