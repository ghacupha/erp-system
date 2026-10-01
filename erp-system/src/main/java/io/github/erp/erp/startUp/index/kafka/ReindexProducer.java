
/*-
 * Erp System - Mark X No 12 (Kadar Series) Server ver 1.9.0
 * Copyright © 2021 - 2026 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
package io.github.erp.erp.startUp.index.kafka;

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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Shared producer for every phased-reindex entity. Startup (and the manual admin "reindex all"
 * trigger) call this to queue ids for (re)indexing instead of writing to Elasticsearch inline, so
 * neither app startup nor the request thread is blocked by the actual search-index write. The
 * topic is supplied per call - each entity owns its own topic name/constant - but all of them
 * share this one producer bean and its underlying Kafka client.
 */
@Component
public class ReindexProducer {

    private static final Logger log = LoggerFactory.getLogger(ReindexProducer.class);

    private final KafkaTemplate<String, ReindexMessage> kafkaTemplate;

    public ReindexProducer(@Qualifier("phasedReindexKafkaTemplate") KafkaTemplate<String, ReindexMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReindexMessage(String topicName, List<Long> ids) {
        send(topicName, ids, false);
    }

    public void sendDeleteMessage(String topicName, List<Long> ids) {
        send(topicName, ids, true);
    }

    private void send(String topicName, List<Long> ids, boolean deleted) {
        if (ids == null || ids.isEmpty()) {
            log.debug("No ids provided for topic {}; skipping dispatch", topicName);
            return;
        }

        ReindexMessage message = new ReindexMessage();
        message.setIds(ids);
        message.setDeleted(deleted);
        kafkaTemplate.send(topicName, message);
    }
}
