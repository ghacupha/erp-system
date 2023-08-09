package io.github.erp.web.rest;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.config.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ErpSystemKafkaResourceIT {

    private static boolean started = false;
    private static KafkaContainer kafkaContainer;

    private MockMvc restMockMvc;

    @BeforeAll
    static void startServer() {
        if (!started) {
            startTestcontainer();
            started = true;
        }
    }

    private static void startTestcontainer() {
        // TODO: withNetwork will need to be removed soon
        // See discussion at https://github.com/jhipster/generator-jhipster/issues/11544#issuecomment-609065206
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.5.5")).withNetwork(null);
        kafkaContainer.start();
    }

    // @BeforeEach
    void setup() {
        KafkaProperties kafkaProperties = new KafkaProperties();
        Map<String, String> producerProps = getProducerProps();
        kafkaProperties.setProducer(new HashMap<>(producerProps));

        Map<String, String> consumerProps = getConsumerProps("default-group");
        consumerProps.put("client.id", "default-client");
        kafkaProperties.setConsumer(consumerProps);

        ErpSystemKafkaResource kafkaResource = new ErpSystemKafkaResource(kafkaProperties);

        restMockMvc = MockMvcBuilders.standaloneSetup(kafkaResource).build();
    }

    // @Test
    void producesMessages() throws Exception {
        restMockMvc
            .perform(post("/api/erp-system-kafka/publish/topic-produce?message=value-produce"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/xml;charset=UTF-8"));

        Map<String, Object> consumerProps = new HashMap<>(getConsumerProps("group-produce"));
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList("topic-produce"));
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

        assertThat(records.count()).isEqualTo(1);
        ConsumerRecord<String, String> record = records.iterator().next();
        assertThat(record.value()).isEqualTo("value-produce");
    }

    // @Test
    void consumesMessages() throws Exception {
        Map<String, Object> producerProps = new HashMap<>(getProducerProps());
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        producer.send(new ProducerRecord<>("topic-consume", "value-consume"));

        MvcResult mvcResult = restMockMvc
            .perform(get("/api/erp-system-kafka/consume?topic=topic-consume"))
            .andExpect(status().isOk())
            .andExpect(request().asyncStarted())
            .andReturn();

        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            String content = mvcResult.getResponse().getContentAsString();
            if (content.contains("data:value-consume")) {
                return;
            }
        }
        fail("Expected content data:value-consume not received");
    }

    private Map<String, String> getProducerProps() {
        Map<String, String> producerProps = new HashMap<>();
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        return producerProps;
    }

    private Map<String, String> getConsumerProps(String group) {
        Map<String, String> consumerProps = new HashMap<>();
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        consumerProps.put("auto.offset.reset", "earliest");
        consumerProps.put("group.id", group);
        return consumerProps;
    }
}
