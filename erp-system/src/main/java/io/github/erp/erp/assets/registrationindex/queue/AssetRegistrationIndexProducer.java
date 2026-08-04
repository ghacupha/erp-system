package io.github.erp.erp.assets.registrationindex.queue;

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

/**
 * Decouples AssetRegistration's own persistence from its search-index write: the entity save
 * commits to Postgres and returns to the caller immediately, this producer just queues a message
 * naming the id(s) to (re)index, and {@link AssetRegistrationIndexConsumer} does the actual
 * Elasticsearch write asynchronously. A slow or failing Elasticsearch node can no longer fail or
 * roll back an AssetRegistration save.
 */
@Component
public class AssetRegistrationIndexProducer {

    private static final Logger log = LoggerFactory.getLogger(AssetRegistrationIndexProducer.class);

    @Value("${spring.kafka.topics.asset-registration-index.topic.name:asset-registration-index}")
    private String topicName;

    private final KafkaTemplate<String, AssetRegistrationIndexMessage> kafkaTemplate;

    public AssetRegistrationIndexProducer(
        @Qualifier("assetRegistrationIndexKafkaTemplate") KafkaTemplate<String, AssetRegistrationIndexMessage> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendIndexMessage(List<Long> assetRegistrationIds) {
        send(assetRegistrationIds, false);
    }

    public void sendDeleteMessage(Long assetRegistrationId) {
        send(List.of(assetRegistrationId), true);
    }

    private void send(List<Long> assetRegistrationIds, boolean deleted) {
        if (assetRegistrationIds == null || assetRegistrationIds.isEmpty()) {
            log.debug("No asset-registration ids provided for indexing; skipping dispatch");
            return;
        }

        AssetRegistrationIndexMessage message = new AssetRegistrationIndexMessage();
        message.setAssetRegistrationIds(assetRegistrationIds);
        message.setDeleted(deleted);
        kafkaTemplate.send(topicName, message);
    }
}
