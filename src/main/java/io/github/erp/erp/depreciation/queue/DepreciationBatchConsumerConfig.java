package io.github.erp.erp.depreciation.queue;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import io.github.erp.erp.depreciation.model.DepreciationBatchMessage;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
// import org.springframework.kafka.listener.config.ContainerProperties.ListenerMode;
// import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// import static io.github.erp.erp.depreciation.queue.DepreciationBatchProducer.DEPRECIATION_BATCH_TOPIC;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class DepreciationBatchConsumerConfig {

    @Value("${kafka.depreciation-batch.topic.name}")
    private String topicName;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.consumer.group.id}")
    private String groupId;

    // Set Kafka consumer properties
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DepreciationBatchMessage> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DepreciationBatchMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // Set other properties on the factory if needed
        return factory;
    }


    @Bean
    public ContainerProperties containerProperties() {
        ContainerProperties containerProperties = new ContainerProperties(topicName);
        containerProperties.setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        // containerProperties.setListenerMode(ListenerMode.BATCH);
        containerProperties.setAckOnError(false);
        containerProperties.setPollTimeout(3000);
        // containerProperties.setErrorHandler(new SeekToCurrentErrorHandler());
        return containerProperties;
    }

    // Define Kafka consumer configurations
    @SneakyThrows
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // Add additional properties as needed
        return properties;
    }

    // Create Kafka consumer factory
    @Bean
    public DefaultKafkaConsumerFactory<String, DepreciationBatchMessage> consumerFactory() {
        DefaultKafkaConsumerFactory<String, DepreciationBatchMessage> consumerFactory =  new DefaultKafkaConsumerFactory<>(consumerConfigs());

        consumerFactory.setKeyDeserializer(new StringDeserializer());
        consumerFactory.setValueDeserializer(new DepreciationBatchMessageDeserializer());
        return consumerFactory;
    }
}


