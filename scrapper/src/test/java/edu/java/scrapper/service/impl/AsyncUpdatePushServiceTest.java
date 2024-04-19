package edu.java.scrapper.service.impl;

import edu.java.resilience.dto.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@EmbeddedKafka(partitions = 1, topics = "notifier.update.message")
class AsyncUpdatePushServiceTest {
    public static final String TOPIC = "notifier.update.message";

    @Test
    @DisplayName("Test AsyncUpdatePushService on sending updates")
    void sendUpdate(EmbeddedKafkaBroker broker) throws InterruptedException {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testT", "true", broker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        DefaultKafkaConsumerFactory<String, LinkUpdateRequest> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        ContainerProperties containerProperties = new ContainerProperties(TOPIC);
        KafkaMessageListenerContainer<String, LinkUpdateRequest> container =
            new KafkaMessageListenerContainer<>(cf, containerProperties);
        final BlockingQueue<ConsumerRecord<String, LinkUpdateRequest>> records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, LinkUpdateRequest>) records::add);
        container.setBeanName("templateTests");
        container.start();
        ContainerTestUtils.waitForAssignment(container, broker.getPartitionsPerTopic());

        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        DefaultKafkaProducerFactory<String, LinkUpdateRequest> producerFactory =
            new DefaultKafkaProducerFactory<>(producerProps);
        KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(TOPIC);

        AsyncUpdatePushService asyncUpdatePushService = new AsyncUpdatePushService(kafkaTemplate);

        asyncUpdatePushService.sendUpdate(new LinkUpdateRequest(
            1L, URI.create("http://github"), "",
            List.of()
        ));

        assertThat(records.poll(10, TimeUnit.SECONDS).value().id())
            .isEqualTo(1L);
    }
}
