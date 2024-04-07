package edu.java.bot.exception;

import edu.java.bot.dto.LinkUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaExceptionHandler implements ConsumerRecordRecoverer {
    private final KafkaTemplate<String, LinkUpdateDTO> kafkaTemplate;
    private final String topic = "notifier.update.dlq";

    @Override
    public void accept(ConsumerRecord<?, ?> consumerRecord, Exception e) {
        log.error("Couldn't process message: {}. Error: {}", consumerRecord.value().toString(), e.toString());
        try {
            kafkaTemplate.send(topic, (LinkUpdateDTO) consumerRecord.value());
        } catch (Exception exception) {
            log.error("Failed to send bad update message to {}.", topic);
        }
    }
}
