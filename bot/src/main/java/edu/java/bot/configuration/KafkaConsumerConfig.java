package edu.java.bot.configuration;

import edu.java.bot.dto.LinkUpdateDTO;
import edu.java.bot.exception.KafkaExceptionHandler;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableKafka
@ConditionalOnProperty(prefix = "app", value = "use-queue", havingValue = "true")
public class KafkaConsumerConfig {
    private final ApplicationConfig config;

    @Bean
    public ConsumerFactory<String, LinkUpdateDTO> consumerFactory() {
        ApplicationConfig.KafkaConsumer kafkaConsumer = config.kafkaConsumer();
        return new DefaultKafkaConsumerFactory<>(Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumer.servers(),
            ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumer.groupId(),
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumer.autoOffsetReset(),
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumer.enableAutoCommit(),
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateDTO.class,
            JsonDeserializer.TRUSTED_PACKAGES, "*",
            JsonDeserializer.USE_TYPE_INFO_HEADERS, false,
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int) kafkaConsumer.maxPollInterval().toMillis()
        ));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateDTO> listenerContainerFactory(
        ConsumerFactory<String, LinkUpdateDTO> factory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateDTO> containerFactory =
            new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(factory);
        containerFactory.setConcurrency(config.kafkaConsumer().concurrency());
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return containerFactory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaExceptionHandler handler) {
        ApplicationConfig.KafkaConsumer kafkaConsumerConfig = config.kafkaConsumer();
        BackOff backOff =
            new FixedBackOff(kafkaConsumerConfig.backoffInterval().toMillis(), kafkaConsumerConfig.backoffAttempts());
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(handler, backOff);
        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        return errorHandler;
    }
}
