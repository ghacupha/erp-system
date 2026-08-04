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

import io.github.erp.domain.AssetRegistrationIndex;
import io.github.erp.erp.assets.registrationindex.AssetRegistrationIndexMapper;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.repository.search.AssetRegistrationIndexSearchRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consumes {@link AssetRegistrationIndexMessage}s and does the actual Elasticsearch write - the
 * half of AssetRegistration's old synchronous save-then-index that used to be able to fail (or
 * roll back) the save itself. Always re-fetches the entity (with all its relationships eagerly
 * fetched) from Postgres by id rather than trusting anything carried in the message, so a
 * redelivered or delayed message can never index stale data.
 */
@Component
public class AssetRegistrationIndexConsumer {

    private static final Logger log = LoggerFactory.getLogger(AssetRegistrationIndexConsumer.class);
    private static final String TOPIC_NAME_PROPERTY = "spring.kafka.topics.asset-registration-index.topic.name:asset-registration-index";

    private final InternalAssetRegistrationRepository assetRegistrationRepository;
    private final AssetRegistrationIndexSearchRepository assetRegistrationIndexSearchRepository;
    private final AssetRegistrationIndexMapper mapper;

    public AssetRegistrationIndexConsumer(
        InternalAssetRegistrationRepository assetRegistrationRepository,
        AssetRegistrationIndexSearchRepository assetRegistrationIndexSearchRepository,
        AssetRegistrationIndexMapper mapper
    ) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationIndexSearchRepository = assetRegistrationIndexSearchRepository;
        this.mapper = mapper;
    }

    @KafkaListener(
        topics = "${" + TOPIC_NAME_PROPERTY + "}",
        containerFactory = "assetRegistrationIndexKafkaListenerContainerFactory"
    )
    @Transactional(readOnly = true)
    public void consume(AssetRegistrationIndexMessage message) {
        if (message.getAssetRegistrationIds() == null || message.getAssetRegistrationIds().isEmpty()) {
            return;
        }

        if (message.isDeleted()) {
            message.getAssetRegistrationIds().forEach(assetRegistrationIndexSearchRepository::deleteById);
            log.debug("Removed {} asset-registration index document(s)", message.getAssetRegistrationIds().size());
            return;
        }

        List<AssetRegistrationIndex> documents = new ArrayList<>();
        for (Long id : message.getAssetRegistrationIds()) {
            assetRegistrationRepository.findOneWithEagerRelationships(id).map(mapper::toIndex).ifPresent(documents::add);
        }

        if (documents.isEmpty()) {
            return;
        }

        assetRegistrationIndexSearchRepository.saveAll(documents);
        log.debug("Indexed {} asset-registration document(s)", documents.size());
    }
}
