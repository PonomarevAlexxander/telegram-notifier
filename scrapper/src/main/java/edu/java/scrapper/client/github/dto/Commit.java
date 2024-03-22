package edu.java.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Commit(
    @JsonProperty("message")
    String message
) {
}
