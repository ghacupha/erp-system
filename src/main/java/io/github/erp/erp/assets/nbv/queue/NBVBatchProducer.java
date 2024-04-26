package io.github.erp.erp.assets.nbv.queue;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

