package edu.java.scrapper.configuration.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.LinkUpdateDTO;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final ApplicationConfig config;

    @Bean
    public ProducerFactory<String, LinkUpdateDTO> producerFactory() {
        ApplicationConfig.Kafka kafkaConfig = config.kafkaProducer();
        return new DefaultKafkaProducerFactory<>(Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.servers(),
            ProducerConfig.CLIENT_ID_CONFIG, kafkaConfig.clientId(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
            ProducerConfig.ACKS_CONFIG, kafkaConfig.acks(),
            ProducerConfig.LINGER_MS_CONFIG, kafkaConfig.lingerMs().toMillis(),
            ProducerConfig.BATCH_SIZE_CONFIG, kafkaConfig.batchSize()
        ));
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateDTO> kafkaTemplate(ProducerFactory<String, LinkUpdateDTO> factory) {
        return new KafkaTemplate<>(factory);
    }
}
