package edu.java.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowAnswer(
    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivity,
    @JsonProperty("answer_id")
    String answerId,
    @JsonProperty("body_markdown")
    String message,
    @JsonProperty("title")
    String questionTitle,
    @JsonProperty("owner")
    StackOverflowUser owner
) {
}
