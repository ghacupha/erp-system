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

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LeasePaymentReindexProducer {

    private static final Logger log = LoggerFactory.getLogger(LeasePaymentReindexProducer.class);

    @Value("${spring.kafka.topics.lease-payment-reindex.topic.name:lease-payment-reindex}")
    private String topicName;

    private final KafkaTemplate<String, LeasePaymentReindexMessage> kafkaTemplate;

    public LeasePaymentReindexProducer(
        @Qualifier("leasePaymentReindexKafkaTemplate") KafkaTemplate<String, LeasePaymentReindexMessage> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReindexMessage(List<Long> paymentIds, boolean active) {
        if (paymentIds == null || paymentIds.isEmpty()) {
            log.debug("No lease payment ids provided for reindexing; skipping dispatch");
            return;
        }

        LeasePaymentReindexMessage message = new LeasePaymentReindexMessage();
        message.setPaymentIds(paymentIds);
        message.setActive(active);
        kafkaTemplate.send(topicName, message);
    }
}
