package edu.java.bot.listener;

import edu.java.bot.app.NotifierBot;
import edu.java.bot.service.UpdateService;
import edu.java.resilience.dto.LinkUpdateRequest;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "app.use-queue=true",
    "spring.kafka.consumer.properties.max.poll.interval.ms=10000",
    "spring.kafka.retry.topic.attempts=1"
})
@EmbeddedKafka(partitions = 1, topics = {
    "notifier.update.message"
})
class UpdateKafkaListenerTest {
    private final String topic = "notifier.update.message";
    private final String dlqTopic = "notifier.update.message.dlq";
    @MockBean
    UpdateService updateService;
    @MockBean
    NotifierBot bot;

    @Autowired
    private KafkaTemplate<String, LinkUpdateRequest> template;

    @Autowired
    private ConsumerFactory<String, LinkUpdateRequest> consumerFactory;

    @Value("${spring.kafka.consumer.properties.max.poll.interval.ms}")
    long pollMs;

    @Test
    @DisplayName("Test updateKafkaListener with success update")
    void handleUpdateMessage_success() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Mockito.doAnswer(invocationOnMock -> {
            latch.countDown();
            return null;
        }).when(updateService).processUpdates(Mockito.any(LinkUpdateRequest.class));

        template.send(
            topic,
            new LinkUpdateRequest(1L, URI.create("site.com"), "sth updated", List.of(123L))
        );

        assertThat(latch.await(pollMs * 3, TimeUnit.MILLISECONDS))
            .isTrue();

        Mockito.verify(updateService, Mockito.times(1)).processUpdates(Mockito.any(LinkUpdateRequest.class));
    }

    @Test
    @DisplayName("Test updateKafkaListener with failed update")
    void handleUpdateMessage_fail() throws InterruptedException {
        Consumer<String, LinkUpdateRequest> consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(dlqTopic));

        CountDownLatch latch = new CountDownLatch(4);

        Mockito.doAnswer(invocationOnMock -> {
            latch.countDown();
            throw new RuntimeException("Some error from Telegram API");
        }).when(updateService).processUpdates(Mockito.any(LinkUpdateRequest.class));

        template.send(
            topic,
            new LinkUpdateRequest(1L, URI.create("site.com"), "sth updated", List.of(123L))
        );

        assertThat(latch.await(60, TimeUnit.SECONDS))
            .isTrue();

        Mockito.verify(updateService, Mockito.times(4)).processUpdates(Mockito.any(LinkUpdateRequest.class));
    }
}
