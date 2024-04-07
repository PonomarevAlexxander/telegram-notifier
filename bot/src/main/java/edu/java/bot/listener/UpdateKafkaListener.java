package edu.java.bot.listener;

import edu.java.bot.dto.LinkUpdateDTO;
import edu.java.bot.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "app", value = "use-queue", havingValue = "true")
@RequiredArgsConstructor
public class UpdateKafkaListener {
    private final UpdateService updateService;

    @KafkaListener(topics = "${app.update-topic}", containerFactory = "listenerContainerFactory")
    public void handleUpdateMessage(@Payload LinkUpdateDTO updateDTO, Acknowledgment ack) {
        log.info("Update message on link {} received.", updateDTO.url().toString());
        updateService.processUpdates(updateDTO);
        ack.acknowledge();
    }
}
