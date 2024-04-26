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
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka.topics.nbv.consumer")
public class NBVCompilationConsumerConfig {


//    @Value("${spring.kafka.consumer.nbv.topic.name}")
//    private String topicName;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topics.nbv.consumer.group.id}")
    private String groupId;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NBVBatchMessage> nbvListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NBVBatchMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(nbvConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }


    @SneakyThrows
    @Bean
    public Map<String, Object> nbvConsumerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return properties;
    }

    // Create Kafka consumer factory
    @Bean
    public DefaultKafkaConsumerFactory<String, NBVBatchMessage> nbvConsumerFactory() {
        DefaultKafkaConsumerFactory<String, NBVBatchMessage> consumerFactory =  new DefaultKafkaConsumerFactory<>(nbvConsumerConfigs());

        // TODO Add listener
        consumerFactory.setKeyDeserializer(new StringDeserializer());
        consumerFactory.setValueDeserializer(new NBVBatchMessageDeserializer());
        return consumerFactory;
    }
}
