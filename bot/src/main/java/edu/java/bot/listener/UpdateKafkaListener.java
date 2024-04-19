package edu.java.bot.listener;

import edu.java.bot.service.UpdateService;
import edu.java.resilience.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "app", value = "use-queue", havingValue = "true")
@RequiredArgsConstructor
public class UpdateKafkaListener {
    private final UpdateService updateService;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @RetryableTopic(dltTopicSuffix = ".dlq")
    @KafkaListener(topics = "${app.update-topic}")
    public void handleUpdateMessage(@Payload LinkUpdateRequest updateDTO, Acknowledgment ack) {
        log.info("Update message on link {} received.", updateDTO.url().toString());
        updateService.processUpdates(updateDTO);
        ack.acknowledge();
    }

    @DltHandler
    public void handleUpdateMessage(LinkUpdateRequest updateDto) {
        log.error("LinkUpdateRequest {} failed to be processed", updateDto.toString());
        try {
            kafkaTemplate.send("notifier.update.message.dlq", updateDto);
        } catch (Exception ignore) {
        }
    }
}
