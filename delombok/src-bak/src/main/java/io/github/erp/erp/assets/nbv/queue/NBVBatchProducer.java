package io.github.erp.erp.assets.nbv.queue;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * Creates and sends a depreciation-batch-message to the queue
 */
@Component
public class NBVBatchProducer {

    @Value("${spring.kafka.topics.nbv.topic.name}")
    private String topicName;

    private static final Logger log = LoggerFactory.getLogger(NBVBatchProducer.class);

    private final KafkaTemplate<String, NBVBatchMessage> kafkaTemplate;

    public NBVBatchProducer(
        @Qualifier("nbvMessageKafkaTemplate") KafkaTemplate<String, NBVBatchMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendJobMessage(NBVBatchMessage nbvBatchMessage) {

        if (nbvBatchMessage.isLastBatch()) {
            log.info("Last batch encountered, sequence # {} with {} items; total items processed: {}, out of {}", nbvBatchMessage.getSequenceNumber(), nbvBatchMessage.getBatchSize(), nbvBatchMessage.getStartIndex() -1 , nbvBatchMessage.getTotalItems());
        }


        kafkaTemplate.send(topicName, nbvBatchMessage);
    }
}

