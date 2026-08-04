package io.github.erp.erp.assets.accessoryindex.queue;

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

/** Mirrors AssetRegistrationIndexProducer - see its doc comment for the full rationale. */
@Component
public class AssetAccessoryIndexProducer {

    private static final Logger log = LoggerFactory.getLogger(AssetAccessoryIndexProducer.class);

    @Value("${spring.kafka.topics.asset-accessory-index.topic.name:asset-accessory-index}")
    private String topicName;

    private final KafkaTemplate<String, AssetAccessoryIndexMessage> kafkaTemplate;

    public AssetAccessoryIndexProducer(
        @Qualifier("assetAccessoryIndexKafkaTemplate") KafkaTemplate<String, AssetAccessoryIndexMessage> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendIndexMessage(List<Long> assetAccessoryIds) {
        send(assetAccessoryIds, false);
    }

    public void sendDeleteMessage(Long assetAccessoryId) {
        send(List.of(assetAccessoryId), true);
    }

    private void send(List<Long> assetAccessoryIds, boolean deleted) {
        if (assetAccessoryIds == null || assetAccessoryIds.isEmpty()) {
            log.debug("No asset-accessory ids provided for indexing; skipping dispatch");
            return;
        }

        AssetAccessoryIndexMessage message = new AssetAccessoryIndexMessage();
        message.setAssetAccessoryIds(assetAccessoryIds);
        message.setDeleted(deleted);
        kafkaTemplate.send(topicName, message);
    }
}
