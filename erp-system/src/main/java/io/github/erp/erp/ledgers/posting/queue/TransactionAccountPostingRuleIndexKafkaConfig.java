package io.github.erp.erp.ledgers.posting.queue;

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

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class TransactionAccountPostingRuleIndexKafkaConfig {

    private final String bootstrapServers;
    private final String groupId;
    private final Integer concurrency;

    public TransactionAccountPostingRuleIndexKafkaConfig(
        @Value("${spring.kafka.bootstrap-servers:localhost:9092}") String bootstrapServers,
        @Value("${spring.kafka.topics.posting-rule-index.consumer.group.id:erp-system-posting-rule-index}") String groupId,
        @Value("${spring.kafka.topics.posting-rule-index.consumer.concurrency:1}") Integer concurrency
    ) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.concurrency = concurrency;
    }

    @Bean("postingRuleIndexProducerFactory")
    public ProducerFactory<String, TransactionAccountPostingRuleIndexMessage> postingRuleIndexProducerFactory(
        @Value("${spring.kafka.topics.posting-rule-index.producer.client-id:erp-system-posting-rule-index-producer}") String clientId
    ) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean("postingRuleIndexKafkaTemplate")
    public KafkaTemplate<String, TransactionAccountPostingRuleIndexMessage> postingRuleIndexKafkaTemplate(
        @Value("${spring.kafka.topics.posting-rule-index.producer.client-id:erp-system-posting-rule-index-producer}") String clientId
    ) {
        return new KafkaTemplate<>(postingRuleIndexProducerFactory(clientId));
    }

    @Bean("postingRuleIndexConsumerFactory")
    public DefaultKafkaConsumerFactory<String, TransactionAccountPostingRuleIndexMessage> postingRuleIndexConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<TransactionAccountPostingRuleIndexMessage> jsonDeserializer = new JsonDeserializer<>(
            TransactionAccountPostingRuleIndexMessage.class,
            false
        );
        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean("postingRuleIndexKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, TransactionAccountPostingRuleIndexMessage> postingRuleIndexKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionAccountPostingRuleIndexMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(postingRuleIndexConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        factory.setConcurrency(concurrency);
        return factory;
    }
}
