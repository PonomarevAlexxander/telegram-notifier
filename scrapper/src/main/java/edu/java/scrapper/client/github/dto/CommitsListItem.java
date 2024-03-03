package edu.java.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommitsListItem(
    @JsonProperty("commit")
    Commit commit
) {
}
