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
public class PresentValueEnumerationKafkaConfig {

    private final String bootstrapServers;
    private final String groupId;
    private final Integer concurrency;

    public PresentValueEnumerationKafkaConfig(
        @Value("${spring.kafka.bootstrap-servers:localhost:9092}") String bootstrapServers,
        @Value("${spring.kafka.topics.present-value-enumeration.consumer.group.id:erp-system-present-value-enumeration}") String groupId,
        @Value("${spring.kafka.topics.present-value-enumeration.consumer.concurrency:1}") Integer concurrency
    ) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.concurrency = concurrency;
    }

    @Bean("presentValueEnumerationProducerFactory")
    public ProducerFactory<String, PresentValueEnumerationQueueItem> presentValueEnumerationProducerFactory(
        @Value("${spring.kafka.topics.present-value-enumeration.producer.client-id:erp-system-present-value-enumeration-producer}") String clientId
    ) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean("presentValueEnumerationKafkaTemplate")
    public KafkaTemplate<String, PresentValueEnumerationQueueItem> presentValueEnumerationKafkaTemplate(
        @Value("${spring.kafka.topics.present-value-enumeration.producer.client-id:erp-system-present-value-enumeration-producer}") String clientId
    ) {
        return new KafkaTemplate<>(presentValueEnumerationProducerFactory(clientId));
    }

    @Bean("presentValueEnumerationConsumerFactory")
    public DefaultKafkaConsumerFactory<String, PresentValueEnumerationQueueItem> presentValueEnumerationConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<PresentValueEnumerationQueueItem> jsonDeserializer = new JsonDeserializer<>(
            PresentValueEnumerationQueueItem.class,
            false
        );
        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean("presentValueEnumerationKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, PresentValueEnumerationQueueItem> presentValueEnumerationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PresentValueEnumerationQueueItem> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(presentValueEnumerationConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        factory.setConcurrency(concurrency);
        return factory;
    }
}
