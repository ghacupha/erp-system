package io.github.erp.erp.leases.payments.upload.queue;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.LeasePayment;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.search.LeaseContractSearchRepository;
import io.github.erp.repository.search.LeasePaymentSearchRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LeasePaymentReindexConsumer {

    private static final Logger log = LoggerFactory.getLogger(LeasePaymentReindexConsumer.class);
    private static final String LEASE_PAYMENT_REINDEX_TOPIC_NAME = "spring.kafka.topics.lease-payment-reindex.topic.name:lease-payment-reindex";

    private final LeasePaymentRepository leasePaymentRepository;
    private final LeasePaymentSearchRepository leasePaymentSearchRepository;
    private final String topicName;
    private final LeaseContractSearchRepository leaseContractSearchRepository;

    public LeasePaymentReindexConsumer(
        LeasePaymentRepository leasePaymentRepository,
        LeasePaymentSearchRepository leasePaymentSearchRepository,
        @Value("${" + LEASE_PAYMENT_REINDEX_TOPIC_NAME + "}") String topicName,
        LeaseContractSearchRepository leaseContractSearchRepository) {
        this.leasePaymentRepository = leasePaymentRepository;
        this.leasePaymentSearchRepository = leasePaymentSearchRepository;
        this.topicName = topicName;
        this.leaseContractSearchRepository = leaseContractSearchRepository;
    }

    @KafkaListener(
        topics = "${" + LEASE_PAYMENT_REINDEX_TOPIC_NAME + "}",
        containerFactory = "leasePaymentReindexKafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(LeasePaymentReindexMessage message) {
        log.debug("Received lease payment reindex message for ids {} on topic {}", message.getPaymentIds(), topicName);

        if (message.getPaymentIds() == null || message.getPaymentIds().isEmpty()) {
            return;
        }

        List<LeasePayment> paymentsToUpdate = new ArrayList<>();
        leasePaymentRepository.findAllById(message.getPaymentIds()).forEach(payment -> {
            payment.setActive(message.getActive());
            paymentsToUpdate.add(payment);
        });

        if (paymentsToUpdate.isEmpty()) {
            return;
        }

        // Not necessary as the items have already been saved in the batch writer
        // List<LeasePayment> savedPayments = leasePaymentRepository.saveAll(paymentsToUpdate);
        // TODO Implement batching here to prevent stackoverflow errors
        leasePaymentSearchRepository.saveAll(paymentsToUpdate);
    }
}
