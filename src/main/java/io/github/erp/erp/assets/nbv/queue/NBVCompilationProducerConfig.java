package io.github.erp.erp.assets.nbv.queue;

import io.github.erp.erp.assets.depreciation.model.DepreciationBatchMessage;
import io.github.erp.erp.assets.depreciation.queue.DepreciationBatchMessageSerializer;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka.nbv.producer")
public class NBVCompilationProducerConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, NBVBatchMessage> producerFactory() throws ClassNotFoundException {
        DefaultKafkaProducerFactory<String, NBVBatchMessage> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        producerFactory.setKeySerializer(new StringSerializer());
        producerFactory.setValueSerializer(new NBVBatchMessageSerializer());
        return producerFactory;
    }

    @SneakyThrows
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return properties;
    }

    @SneakyThrows
    @Bean("nbvMessageKafkaTemplate")
    public KafkaTemplate<String, NBVBatchMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
