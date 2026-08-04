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

import io.github.erp.domain.AssetAccessoryIndex;
import io.github.erp.erp.assets.accessoryindex.AssetAccessoryIndexMapper;
import io.github.erp.repository.AssetAccessoryRepository;
import io.github.erp.repository.search.AssetAccessoryIndexSearchRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Mirrors AssetRegistrationIndexConsumer - see its doc comment for the full rationale. */
@Component
public class AssetAccessoryIndexConsumer {

    private static final Logger log = LoggerFactory.getLogger(AssetAccessoryIndexConsumer.class);
    private static final String TOPIC_NAME_PROPERTY = "spring.kafka.topics.asset-accessory-index.topic.name:asset-accessory-index";

    private final AssetAccessoryRepository assetAccessoryRepository;
    private final AssetAccessoryIndexSearchRepository assetAccessoryIndexSearchRepository;
    private final AssetAccessoryIndexMapper mapper;

    public AssetAccessoryIndexConsumer(
        AssetAccessoryRepository assetAccessoryRepository,
        AssetAccessoryIndexSearchRepository assetAccessoryIndexSearchRepository,
        AssetAccessoryIndexMapper mapper
    ) {
        this.assetAccessoryRepository = assetAccessoryRepository;
        this.assetAccessoryIndexSearchRepository = assetAccessoryIndexSearchRepository;
        this.mapper = mapper;
    }

    @KafkaListener(
        topics = "${" + TOPIC_NAME_PROPERTY + "}",
        containerFactory = "assetAccessoryIndexKafkaListenerContainerFactory"
    )
    @Transactional(readOnly = true)
    public void consume(AssetAccessoryIndexMessage message) {
        if (message.getAssetAccessoryIds() == null || message.getAssetAccessoryIds().isEmpty()) {
            return;
        }

        if (message.isDeleted()) {
            message.getAssetAccessoryIds().forEach(assetAccessoryIndexSearchRepository::deleteById);
            log.debug("Removed {} asset-accessory index document(s)", message.getAssetAccessoryIds().size());
            return;
        }

        List<AssetAccessoryIndex> documents = new ArrayList<>();
        for (Long id : message.getAssetAccessoryIds()) {
            assetAccessoryRepository.findOneWithEagerRelationships(id).map(mapper::toIndex).ifPresent(documents::add);
        }

        if (documents.isEmpty()) {
            return;
        }

        assetAccessoryIndexSearchRepository.saveAll(documents);
        log.debug("Indexed {} asset-accessory document(s)", documents.size());
    }
}
