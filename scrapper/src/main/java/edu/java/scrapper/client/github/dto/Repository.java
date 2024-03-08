package edu.java.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Repository(
    @JsonProperty("updated_at")
    OffsetDateTime lastUpdated
) {
}
