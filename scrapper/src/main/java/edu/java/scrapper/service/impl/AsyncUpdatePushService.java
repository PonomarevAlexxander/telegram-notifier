package edu.java.scrapper.service.impl;

import edu.java.resilience.dto.LinkUpdateRequest;
import edu.java.scrapper.service.UpdatePushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
@Slf4j
public class AsyncUpdatePushService implements UpdatePushService {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final String topic = "notifier.update.message";

    @Override
    public void sendUpdate(LinkUpdateRequest update) {
        log.info("Sending update on link {} to the topic {}", update.url().toString(), topic);
        try {
            kafkaTemplate.send(topic, update);
        } catch (Exception ex) {
            log.error("Error while sending message to topic {}: {}", topic, ex);
        }
    }
}
