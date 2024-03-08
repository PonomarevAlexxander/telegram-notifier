package edu.java.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowAnswer(
    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivity
) {
}
