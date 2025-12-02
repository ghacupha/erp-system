package io.github.erp.erp.leases.liability.enumeration.queue;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PresentValueEnumerationProducer {

    private static final Logger log = LoggerFactory.getLogger(PresentValueEnumerationProducer.class);

    private final KafkaTemplate<String, PresentValueEnumerationQueueItem> kafkaTemplate;
    private final String topic;

    public PresentValueEnumerationProducer(
        @Qualifier("presentValueEnumerationKafkaTemplate") KafkaTemplate<String, PresentValueEnumerationQueueItem> kafkaTemplate,
        @Value("${spring.kafka.topics.present-value-enumeration.topic:present-value-enumeration}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(PresentValueEnumerationQueueItem queueItem) {
        log.debug(
            "Dispatching present value enumeration {} for liability enumeration {}",
            queueItem.getSequenceNumber(),
            queueItem.getLiabilityEnumerationId()
        );
        kafkaTemplate.send(topic, queueItem);
    }
}
