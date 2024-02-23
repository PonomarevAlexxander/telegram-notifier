package edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "app.scheduler.enable")
public class LinkScheduledUpdater {
    private final UpdateService updateService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkScheduledUpdater.class);

    @Scheduled(fixedDelayString = "#{@'app-edu.java.scrapper.configuration.ApplicationConfig'.scheduler().interval()}")
    public void update() {
        LOGGER.info("updating...");
    }
}
