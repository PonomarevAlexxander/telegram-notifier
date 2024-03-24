package edu.java.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowUser(
    @JsonProperty("display_name")
    String name
) {
}
